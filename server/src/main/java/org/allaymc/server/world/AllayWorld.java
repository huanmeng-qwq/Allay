package org.allaymc.server.world;

import com.google.common.base.Preconditions;
import io.netty.util.internal.PlatformDependent;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.eventbus.event.world.WorldDataSaveEvent;
import org.allaymc.api.scheduler.Scheduler;
import org.allaymc.api.server.Server;
import org.allaymc.api.utils.GameLoop;
import org.allaymc.api.world.Dimension;
import org.allaymc.api.world.World;
import org.allaymc.api.world.WorldData;
import org.allaymc.api.world.gamerule.GameRule;
import org.allaymc.api.world.storage.WorldStorage;
import org.allaymc.server.entity.component.player.EntityPlayerNetworkComponentImpl;
import org.allaymc.server.scheduler.AllayScheduler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author daoge_cmd
 */
@Slf4j
public class AllayWorld implements World {
    // Send the time to client every 12 seconds
    public static final long TIME_SENDING_INTERVAL = 12 * 20;

    public static final int MAX_PACKETS_HANDLE_COUNT_AT_ONCE = Server.SETTINGS.networkSettings().maxSyncedPacketsHandleCountAtOnce();

    protected final Queue<PacketQueueEntry> packetQueue = PlatformDependent.newMpscQueue();
    protected final AtomicBoolean networkLock = new AtomicBoolean(false);
    protected final AtomicBoolean isRunning = new AtomicBoolean(true);
    @Getter
    protected final WorldStorage worldStorage;
    @Getter
    protected final WorldData worldData;
    @Getter
    protected final Int2ObjectOpenHashMap<Dimension> dimensionMap = new Int2ObjectOpenHashMap<>(3);
    @Getter
    protected final Scheduler scheduler = new AllayScheduler(Server.getInstance().getVirtualThreadPool());
    protected final GameLoop gameLoop;
    @Getter
    protected final Thread thread;
    protected final Thread networkThread;
    protected long nextTimeSendTick;

    public AllayWorld(WorldStorage worldStorage) {
        this.worldStorage = worldStorage;
        this.worldData = worldStorage.readWorldData();
        this.worldData.setWorld(this);

        this.gameLoop = GameLoop.builder().onTick(gameLoop -> {
            if (!isRunning.get()) {
                gameLoop.stop();
                return;
            }

            //noinspection StatementWithEmptyBody
            while (!networkLock.compareAndSet(false, true)) {
                // Spin
                // We don't use Thread.yield() here, because we don't want to block the world main thread
            }

            try {
                tick(gameLoop.getTick());
            } catch (Throwable throwable) {
                log.error("Error while ticking level {}", this.getWorldData().getName(), throwable);
            } finally {
                networkLock.set(false);
            }
        }).build();
        this.thread = Thread.ofPlatform()
                .name("World Thread - " + this.getWorldData().getName())
                .unstarted(gameLoop::startLoop);
        this.networkThread = Thread.ofPlatform()
                .name("World Network Thread - " + this.getWorldData().getName())
                .unstarted(this::networkTick);
    }

    protected void networkTick() {
        while (isRunning.get()) {
            if (!packetQueue.isEmpty()) {
                while (!networkLock.compareAndSet(false, true)) {
                    // Spin
                    Thread.yield();
                }
            } else {
                Thread.yield();
                continue;
            }

            try {
                PacketQueueEntry entry;
                int count = 0;
                while (count < MAX_PACKETS_HANDLE_COUNT_AT_ONCE && (entry = packetQueue.poll()) != null) {
                    entry.player().getManager().<EntityPlayerNetworkComponentImpl>getComponent(EntityPlayerNetworkComponentImpl.IDENTIFIER).handleDataPacket(entry.packet(), entry.time());
                    count++;
                }
            } catch (Throwable throwable) {
                log.error("Error while handling sync packet in world {}", this.getWorldData().getName(), throwable);
            } finally {
                networkLock.set(false);
            }
        }
    }

    public void addSyncPacketToQueue(EntityPlayer player, BedrockPacket packet, long time) {
        packetQueue.add(new PacketQueueEntry(player, packet, time));
    }

    protected void tick(long currentTick) {
        syncData();
        tickTime(currentTick);
        scheduler.tick();
        getDimensions().values().forEach(d -> ((AllayDimension) d).tick(currentTick));
        worldStorage.tick(currentTick);
    }

    @Override
    public long getTick() {
        return gameLoop.getTick();
    }

    @Override
    public float getTps() {
        return gameLoop.getTps();
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
        if (thread.getState() != Thread.State.NEW) {
            throw new IllegalStateException("World is already start ticking!");
        } else {
            thread.start();
            networkThread.start();
        }
    }

    protected void tickTime(long currentTick) {
        if (!worldData.<Boolean>getGameRule(GameRule.DO_DAYLIGHT_CYCLE)) return;

        if (currentTick >= nextTimeSendTick) {
            worldData.addTime(TIME_SENDING_INTERVAL);
            nextTimeSendTick = currentTick + TIME_SENDING_INTERVAL;
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

    public void setDimension(Dimension dimension) {
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
        isRunning.set(false);
        dimensionMap.values().forEach(dimension -> ((AllayDimension) dimension).shutdown());
        saveWorldData();
        worldStorage.shutdown();
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    protected record PacketQueueEntry(EntityPlayer player, BedrockPacket packet, long time) {}
}