package cn.allay.server.world.chunk;

import cn.allay.api.block.type.BlockState;
import cn.allay.api.data.VanillaBlockTypes;
import cn.allay.api.datastruct.NibbleArray;
import cn.allay.api.world.DimensionInfo;
import cn.allay.api.world.biome.BiomeType;
import cn.allay.api.world.chunk.UnsafeChunk;
import lombok.Getter;
import lombok.Setter;
import org.cloudburstmc.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.concurrent.atomic.AtomicReferenceArray;

@NotThreadSafe
public class AllayUnsafeChunk implements UnsafeChunk {
    @Getter
    @Setter
    protected int chunkX;
    @Getter
    @Setter
    protected int chunkZ;
    protected final AtomicReferenceArray<ChunkSection> sections;
    protected final NibbleArray heightMap;
    protected final DimensionInfo dimensionInfo;

    public AllayUnsafeChunk(int chunkX, int chunkZ, DimensionInfo dimensionInfo) {
        this(chunkX, chunkZ, dimensionInfo, NbtMap.EMPTY);
    }

    public AllayUnsafeChunk(int chunkX, int chunkZ, DimensionInfo dimensionInfo, NbtMap data) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.sections = new AtomicReferenceArray<>(dimensionInfo.chunkSectionSize());
        this.heightMap = new NibbleArray(256);
        this.dimensionInfo = dimensionInfo;
    }

    protected @Nullable ChunkSection getSection(int y) {
        return sections.get(y);
    }

    protected @NotNull ChunkSection getOrCreateSection(int y) {
        for (var i = 0; i <= y; i++) {
            sections.compareAndSet(i, null, new ChunkSection(i));
        }
        return sections.get(y);
    }

    public DimensionInfo getDimensionInfo() {
        return dimensionInfo;
    }

    public int getHeight(@Range(from = 0, to = 15) int x, @Range(from = 0, to = 15) int z) {
        return this.heightMap.get((z << 4) + x);
    }

    public void setHeight(@Range(from = 0, to = 15) int x, @Range(from = 0, to = 15) int z, int height) {
        this.heightMap.set((z << 4) + x, (byte) height);
    }

    public BlockState getBlock(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, boolean layer) {
        ChunkSection section = this.getSection(normalY(y) >>> 4);
        BlockState blockState;
        if (section == null) {
            blockState = VanillaBlockTypes.AIR_TYPE.getDefaultState();
        } else {
            blockState = section.getBlock(x, y & 0xf, z, layer);
        }
        return blockState;
    }

    public void setBlock(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, boolean layer, BlockState blockState) {
        int sectionY = normalY(y) >>> 4;
        ChunkSection section = this.getSection(sectionY);
        if (section == null) {
            section = this.getOrCreateSection(sectionY);
        }
        section.setBlock(x, y & 0xf, z, layer, blockState);
    }

    public @Range(from = 0, to = 15) int getBlockLight(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z) {
        ChunkSection section = this.getSection(normalY(y) >>> 4);
        return section == null ? 0 : section.getBlockLight(x, y & 0xf, z);
    }

    public @Range(from = 0, to = 15) int getSkyLight(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z) {
        ChunkSection section = this.getSection(normalY(y) >>> 4);
        return section == null ? 0 : section.getSkyLight(x, y & 0xf, z);
    }

    public void setBlockLight(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, int light) {
        this.getOrCreateSection(normalY(y) >>> 4).setBlockLight(x, y & 0xf, z, (byte) light);
    }

    public void setSkyLight(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, int light) {
        this.getOrCreateSection(normalY(y) >>> 4).setSkyLight(x, y & 0xf, z, (byte) light);
    }

    protected int normalY(int y) {
        return y - getDimensionInfo().minHeight();
    }

    @Override
    public void setBiomeType(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, BiomeType biomeType) {
        this.getOrCreateSection(normalY(y) >>> 4).setBiomeType(x, y & 0xf, z, biomeType);
    }

    @Override
    public BiomeType getBiomeType(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z) {
        return this.getOrCreateSection(normalY(y) >>> 4).getBiomeType(x, y & 0xf, z);
    }
}