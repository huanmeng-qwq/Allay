package cn.allay.api.world.chunk;

import cn.allay.api.block.type.BlockState;
import cn.allay.api.world.DimensionInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Allay Project 2023/5/30
 *
 * @author Cool_Loong
 */
public interface Chunk {

    void tick();

    DimensionInfo getDimensionInfo();

    @Nullable
    ChunkSection getSection(@Range(from = 0, to = 63) int y);

    @NotNull
    ChunkSection getOrCreateSection(@Range(from = 0, to = 63) int y);

    int getHeight(@Range(from = 0, to = 15) int x, @Range(from = 0, to = 15) int z);

    void setHeight(@Range(from = 0, to = 15) int x, @Range(from = 0, to = 15) int z, int height);

    BlockState getBlock(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, boolean layer);

    default BlockState getBlock(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z) {
        return getBlock(x, y, z, false);
    }

    void setBlock(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, boolean layer, BlockState blockState);

    default void setBlock(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, BlockState blockState) {
        setBlock(x, y, z, false, blockState);
    }

    @Range(from = 0, to = 15) byte getBlockLight(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z);

    void setBlockLight(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, byte light);

    @Range(from = 0, to = 15) byte getSkyLight(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z);

    void setSkyLight(@Range(from = 0, to = 15) int x, @Range(from = -512, to = 511) int y, @Range(from = 0, to = 15) int z, byte light);
}