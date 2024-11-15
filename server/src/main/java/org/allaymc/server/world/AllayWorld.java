package org.allaymc.server.world;

import com.google.common.base.Preconditions;
import io.netty.util.internal.PlatformDependent;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.entity.Entity;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.eventbus.event.world.WeatherChangeEvent;
import org.allaymc.api.eventbus.event.world.WorldDataSaveEvent;
import org.allaymc.api.i18n.I18n;
import org.allaymc.api.i18n.TrKeys;
import org.allaymc.api.math.location.Location3f;
import org.allaymc.api.math.location.Location3fc;
import org.allaymc.api.math.position.Position3i;
import org.allaymc.api.math.position.Position3ic;
import org.allaymc.api.scheduler.Scheduler;
import org.allaymc.api.server.Server;
import org.allaymc.api.utils.GameLoop;
import org.allaymc.api.world.Dimension;
import org.allaymc.api.world.Weather;
import org.allaymc.api.world.World;
import org.allaymc.api.world.WorldData;
import org.allaymc.api.world.chunk.Chunk;
import org.allaymc.api.world.chunk.ChunkLoader;
import org.allaymc.api.world.gamerule.GameRule;
import org.allaymc.api.world.storage.WorldStorage;
import org.allaymc.server.datastruct.collections.queue.BlockingQueueWrapper;
import org.allaymc.server.entity.component.player.EntityPlayerNetworkComponentImpl;
import org.allaymc.server.scheduler.AllayScheduler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.jetbrains.annotations.UnmodifiableView;
import org.joml.Vector3i;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author daoge_cmd
 */
@Slf4j
public class AllayWorld implements World {
    // Send the time to client every 12 seconds
    protected static final long TIME_SENDING_INTERVAL = 12 * 20;
    protected static final int MAX_PACKETS_HANDLE_COUNT_AT_ONCE = Server.SETTINGS.networkSettings().maxSyncedPacketsHandleCountAtOnce();
    protected static final boolean ENABLE_INDEPENDENT_NETWORK_THREAD = Server.SETTINGS.networkSettings().enableIndependentNetworkThread();

    @Getter
    protected final WorldStorage worldStorage;
    @Getter
    protected final WorldData worldData;
    protected final BlockingQueueWrapper<PacketQueueEntry> packetQueue;
    protected final Semaphore networkSemaphore;
    protected final AtomicBoolean isRunning;
    @Getter
    protected final Int2ObjectOpenHashMap<Dimension> dimensionMap;
    @Getter
    protected final Scheduler scheduler;
    protected final GameLoop gameLoop;
    @Getter
    protected final Thread worldThread;
    protected final Thread networkThread;

    protected long nextTimeSendTick;
    protected int rainTimer;
    protected int thunderTimer;
    protected boolean isRaining = false;
    protected boolean isThundering = false;
    protected final Set<Weather> effectiveWeathers;
    protected boolean isFirstTick = false;

    public AllayWorld(WorldStorage worldStorage) {
        this.worldStorage = worldStorage;
        this.worldData = worldStorage.readWorldData();
        this.worldData.setWorld(this);
        this.packetQueue = BlockingQueueWrapper.wrap(PlatformDependent.newMpscQueue());
        this.networkSemaphore = ENABLE_INDEPENDENT_NETWORK_THREAD ? new Semaphore(1) : null;
        this.isRunning = new AtomicBoolean(true);
        this.dimensionMap = new Int2ObjectOpenHashMap<>(3);
        this.scheduler = new AllayScheduler(Server.getInstance().getVirtualThreadPool());
        this.gameLoop = GameLoop.builder().onTick(this::worldThreadMain).build();
        this.worldThread = Thread.ofPlatform()
                .name("World Thread - " + this.getWorldData().getName())
                .unstarted(gameLoop::startLoop);
        this.networkThread = ENABLE_INDEPENDENT_NETWORK_THREAD ? Thread.ofPlatform()
                .name("World Network Thread - " + this.getWorldData().getName())
                .unstarted(this::networkThreadMain) : null;
        this.rainTimer = Weather.CLEAR.generateRandomTimeLength();
        this.thunderTimer = Weather.CLEAR.generateRandomTimeLength();
        this.effectiveWeathers = new HashSet<>();
    }

    @SneakyThrows
    protected void networkThreadMain() {
        while (isRunning.get()) {
            // Block until there are packets to handle (up to 1 second)
            var firstEntry = packetQueue.tryPoll(1, TimeUnit.SECONDS);
            if (firstEntry == null) {
                continue;
            }
            // Get the permit to handle packets
            networkSemaphore.acquire();
            handleSyncPackets(firstEntry);
            // Return the permit, so that the world thread can run
            networkSemaphore.release();
        }
    }

    @SneakyThrows
    private void worldThreadMain(GameLoop gameLoop) {
        if (!isRunning.get()) {
            gameLoop.stop();
            log.info(I18n.get().tr(TrKeys.A_WORLD_UNLOADED, this.getWorldData().getName()));
            return;
        }

        if (ENABLE_INDEPENDENT_NETWORK_THREAD) {
            networkSemaphore.acquire();
        }

        try {
            tick(gameLoop.getTick());
        } catch (Throwable throwable) {
            log.error("Error while ticking level {}", this.getWorldData().getName(), throwable);
        } finally {
            if (ENABLE_INDEPENDENT_NETWORK_THREAD) {
                networkSemaphore.release();
            }
        }
    }

    protected void handleSyncPackets(PacketQueueEntry firstEntry) {
        try {
            PacketQueueEntry entry = firstEntry;
            int count = 0;
            do {
                if (entry == null) continue;
                if (entry.player.getWorld() != this) {
                    log.warn("Trying to handle sync packet in world {} which the player {} is not in!", this.getWorldData().getName(), entry.player.getOriginName());
                }
                entry.player.getManager().<EntityPlayerNetworkComponentImpl>getComponent(EntityPlayerNetworkComponentImpl.IDENTIFIER).handleDataPacket(entry.packet(), entry.time());
                count++;

            } while (count < MAX_PACKETS_HANDLE_COUNT_AT_ONCE && (entry = packetQueue.pollNow()) != null);
        } catch (Throwable throwable) {
            log.error("Error while handling sync packet in world {}", this.getWorldData().getName(), throwable);
        }
    }

    protected void tick(long currentTick) {
        checkFirstTick();
        if (!ENABLE_INDEPENDENT_NETWORK_THREAD) {
            handleSyncPackets(null);
        }
        syncData();
        tickTime(currentTick);
        tickWeather();
        scheduler.tick();
        getDimensions().values().forEach(d -> ((AllayDimension) d).tick(currentTick));
        worldStorage.tick(currentTick);
    }

    protected void checkFirstTick() {
        if (isFirstTick) {
            return;
        }
        isFirstTick = true;

        if (Server.SETTINGS.worldSettings().loadSpawnPointChunks()) {
            // Add spawn point chunk loader
            getOverWorld().getChunkService().addChunkLoader(new SpawnPointChunkLoader());
        }

        if (!isSafeStandingPos(new Position3i(worldData.getSpawnPoint(), getOverWorld()))) {
            var newSpawnPoint = getOverWorld().findSuitableGroundPosAround(this::isSafeStandingPos, 0, 0, 32);
            if (newSpawnPoint == null) {
                log.warn("Cannot find a safe spawn point in the overworld dimension of world {}", worldData.getName());
                newSpawnPoint = new Vector3i(0, getOverWorld().getHeight(0, 0) + 1, 0);
            }
            worldData.setSpawnPoint(newSpawnPoint);
        }
    }

    protected boolean isSafeStandingPos(Position3ic pos) {
        var blockUnder = pos.dimension().getBlockState(pos.x(), pos.y() - 1, pos.z());
        var blockTypeUnder = blockUnder.getBlockType();
        if (!blockTypeUnder.getMaterial().isSolid()) {
            return false;
        }
        return pos.dimension().getBlockState(pos.x(), pos.y(), pos.z()).getBlockType() == BlockTypes.AIR &&
               pos.dimension().getBlockState(pos.x(), pos.y() + 1, pos.z()).getBlockType() == BlockTypes.AIR;
    }

    public void addSyncPacketToQueue(EntityPlayer player, BedrockPacket packet, long time) {
        packetQueue.offer(new PacketQueueEntry(player, packet, time));
    }

    protected void tickTime(long currentTick) {
        if (!worldData.<Boolean>getGameRule(GameRule.DO_DAYLIGHT_CYCLE)) return;

        if (currentTick >= nextTimeSendTick) {
            worldData.addTime(TIME_SENDING_INTERVAL);
            nextTimeSendTick = currentTick + TIME_SENDING_INTERVAL;
        }
    }

    protected void tickWeather() {
        if (!worldData.<Boolean>getGameRule(GameRule.DO_WEATHER_CYCLE)) {
            return;
        }

        Set<Weather> weatherAdded = new HashSet<>();
        Set<Weather> weatherRemoved = new HashSet<>();
        rainTimer--;
        if (rainTimer == 0) {
            if (isRaining) {
                isRaining = false;
                rainTimer = Weather.CLEAR.generateRandomTimeLength();
                weatherRemoved.add(Weather.RAIN);
                effectiveWeathers.remove(Weather.RAIN);
            } else {
                isRaining = true;
                rainTimer = Weather.RAIN.generateRandomTimeLength();
                weatherAdded.add(Weather.RAIN);
                effectiveWeathers.add(Weather.RAIN);
            }
        }

        thunderTimer--;
        if (thunderTimer == 0) {
            if (isThundering) {
                isThundering = false;
                thunderTimer = Weather.CLEAR.generateRandomTimeLength();
                weatherRemoved.add(Weather.THUNDER);
                effectiveWeathers.remove(Weather.THUNDER);
            } else {
                isThundering = true;
                thunderTimer = Weather.THUNDER.generateRandomTimeLength();
                weatherAdded.add(Weather.THUNDER);
                effectiveWeathers.add(Weather.THUNDER);
            }
        }

        var newEffectiveWeathers = new HashSet<>(effectiveWeathers);
        newEffectiveWeathers.removeAll(weatherRemoved);
        newEffectiveWeathers.addAll(weatherAdded);
        var event = new WeatherChangeEvent(this, Collections.unmodifiableSet(effectiveWeathers), Collections.unmodifiableSet(newEffectiveWeathers));
        event.call();
        if (event.isCancelled()) {
            return;
        }

        effectiveWeathers.removeAll(weatherRemoved);
        effectiveWeathers.addAll(weatherAdded);
        onWeatherUpdate(weatherRemoved, weatherAdded);
    }

    @Override
    public long getTick() {
        return gameLoop.getTick();
    }

    @Override
    public float getTPS() {
        return gameLoop.getTPS();
    }

    @Override
    public float getMSPT() {
        return gameLoop.getMSPT();
    }

    @Override
    public float getTickUsage() {
        return gameLoop.getTickUsage();
    }

    protected void syncData() {
        worldData.getGameRules().sync(this);
    }

    public void startTick() {
        if (worldThread.getState() != Thread.State.NEW) {
            throw new IllegalStateException("World is already start ticking!");
        } else {
            worldThread.start();
            dimensionMap.values().forEach(dimension -> ((AllayDimension) dimension).startTick());
            if (ENABLE_INDEPENDENT_NETWORK_THREAD) {
                networkThread.start();
            }
        }
    }

    @Override
    public Dimension getDimension(int dimensionId) {
        return dimensionMap.get(dimensionId);
    }

    @Override
    public @UnmodifiableView Map<Integer, Dimension> getDimensions() {
        return Collections.unmodifiableMap(dimensionMap);
    }

    @Override
    public Collection<EntityPlayer> getPlayers() {
        return Collections.unmodifiableCollection(
                getDimensions().values().stream()
                        .map(Dimension::getPlayers)
                        .reduce(new HashSet<>(), (entityPlayers, entityPlayers2) -> {
                            entityPlayers.addAll(entityPlayers2);
                            return entityPlayers;
                        })
        );
    }

    public void addDimension(Dimension dimension) {
        Preconditions.checkArgument(!this.dimensionMap.containsKey(dimension.getDimensionInfo().dimensionId()));
        this.dimensionMap.put(dimension.getDimensionInfo().dimensionId(), dimension);
    }

    @Override
    public void saveWorldData() {
        var event = new WorldDataSaveEvent(this);
        event.call();
        getWorldStorage().writeWorldData(worldData);
    }

    public void shutdown() {
        log.info(I18n.get().tr(TrKeys.A_WORLD_UNLOADING, worldData.getName()));
        getPlayers().forEach(EntityPlayer::disconnect);
        isRunning.set(false);
        scheduler.shutdown();
        dimensionMap.values().forEach(dimension -> ((AllayDimension) dimension).shutdown());
        saveWorldData();
        worldStorage.shutdown();
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public Set<Weather> getWeathers() {
        return effectiveWeathers.isEmpty() ?
                Set.of(Weather.CLEAR) :
                Collections.unmodifiableSet(effectiveWeathers);
    }

    @Override
    public void addWeather(Weather weather) {
        if (weather == Weather.CLEAR) {
            throw new IllegalArgumentException("Weather.CLEAR shouldn't be used here.");
        }
        if (effectiveWeathers.contains(weather)) {
            return;
        }

        var newEffectiveWeathers = new HashSet<>(effectiveWeathers);
        newEffectiveWeathers.add(weather);
        var event = new WeatherChangeEvent(this, Collections.unmodifiableSet(effectiveWeathers), Collections.unmodifiableSet(newEffectiveWeathers));
        event.call();
        if (event.isCancelled()) {
            return;
        }

        effectiveWeathers.add(weather);
        onWeatherUpdate(Set.of(), Set.of(weather));
    }

    @Override
    public void removeWeather(Weather weather) {
        if (weather == Weather.CLEAR) {
            throw new IllegalArgumentException("Weather.CLEAR shouldn't be used here.");
        }
        if (!effectiveWeathers.contains(weather)) {
            return;
        }

        var newEffectiveWeathers = new HashSet<>(effectiveWeathers);
        newEffectiveWeathers.remove(weather);
        var event = new WeatherChangeEvent(this, Collections.unmodifiableSet(effectiveWeathers), Collections.unmodifiableSet(newEffectiveWeathers));
        event.call();
        if (event.isCancelled()) {
            return;
        }

        effectiveWeathers.remove(weather);
        onWeatherUpdate(Set.of(weather), Set.of());
    }

    @Override
    public void clearWeather() {
        onWeatherUpdate(effectiveWeathers, Set.of());
        effectiveWeathers.clear();
    }

    public void clearWeather(EntityPlayer player) {
        effectiveWeathers.forEach(weather -> player.sendPacket(weather.createStopLevelEventPacket()));
    }

    public void sendWeather(EntityPlayer player) {
        effectiveWeathers.forEach(weather -> player.sendPacket(weather.createStartLevelEventPacket()));
    }

    protected void onWeatherUpdate(Set<Weather> weatherRemoved, Set<Weather> weatherAdded) {
        getPlayers().forEach(player -> {
            weatherRemoved.forEach(weather -> player.sendPacket(weather.createStopLevelEventPacket()));
            weatherAdded.forEach(weather -> player.sendPacket(weather.createStartLevelEventPacket()));
        });
    }

    protected record PacketQueueEntry(EntityPlayer player, BedrockPacket packet, long time) {}

    protected class SpawnPointChunkLoader implements ChunkLoader {

        @Override
        public Location3fc getLocation() {
            var spawnPoint = worldData.getSpawnPoint();
            return new Location3f(spawnPoint.x(), spawnPoint.y(), spawnPoint.z(), getOverWorld());
        }

        @Override
        public boolean isLoaderActive() {
            return true;
        }

        @Override
        public int getChunkLoadingRadius() {
            return Server.SETTINGS.worldSettings().spawnPointChunkRadius();
        }

        @Override
        public void setChunkLoadingRadius(int radius) {}

        @Override
        public int getChunkTrySendCountPerTick() {
            return Server.SETTINGS.worldSettings().chunkTrySendCountPerTick();
        }

        @Override
        public void sendPacket(BedrockPacket packet) {
            if (packet instanceof LevelChunkPacket lcp) {
                lcp.release();
            }
        }

        @Override
        public void beforeSendChunks() {}

        @Override
        public void onChunkInRangeSend(Chunk chunk) {}

        @Override
        public void spawnEntity(Entity entity) {}

        @Override
        public void despawnEntity(Entity entity) {}

        @Override
        public void onChunkOutOfRange(Set<Long> chunkHashes) {}

        @Override
        public void sendPacketImmediately(BedrockPacket packet) {}
    }
}
