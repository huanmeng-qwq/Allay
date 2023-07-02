package cn.allay.server.world.chunk;

import cn.allay.api.block.Block;
import cn.allay.api.block.type.BlockState;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.world.chunk.ChunkSection;
import cn.allay.api.world.palette.Palette;
import cn.allay.server.utils.NibbleArray;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import org.jetbrains.annotations.Range;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Allay Project 2023/5/30
 *
 * @author Cool_Loong
 */
@NotThreadSafe
public class AllayChunkSection implements ChunkSection {
    private static final int SUB_CHUNK_VERSION = 9;
    @Getter
    private final int sectionY;
    private final Palette<BlockState> blockLayer0;
    private final Palette<BlockState> blockLayer1;
    private final NibbleArray blockLights;
    private final NibbleArray skyLights;
    private final ReadWriteLock parentChunkLock;
    //todo biome

    public AllayChunkSection(ReadWriteLock parentChunkLock, int sectionY) {
        this.sectionY = sectionY;
        var airState = BlockType.AIR.getDefaultState();
        blockLayer0 = new Palette<>(airState);
        blockLayer1 = new Palette<>(airState);
        blockLights = new NibbleArray(2048);
        skyLights = new NibbleArray(2048);
        this.parentChunkLock = parentChunkLock;
    }

    @Override
    public BlockState getBlock(int x, int y, int z, boolean layer) {
        try {
            parentChunkLock.readLock().lock();
            if (layer) {
                return blockLayer1.get(index(x, y, z));
            } else {
                return blockLayer0.get(index(x, y, z));
            }
        } finally {
            parentChunkLock.readLock().unlock();
        }
    }

    @Override
    public void setBlock(int x, int y, int z, boolean layer, BlockState blockState) {
        try {
            parentChunkLock.writeLock().lock();
            if (layer) {
                blockLayer1.set(index(x, y, z), blockState);
            } else {
                blockLayer0.set(index(x, y, z), blockState);
            }
        } finally {
            parentChunkLock.writeLock().unlock();
        }
    }

    @Override
    public byte getBlockLight(int x, int y, int z) {
        try {
            parentChunkLock.readLock().lock();
            return blockLights.get(index(x, y, z));
        } finally {
            parentChunkLock.readLock().unlock();
        }
    }

    @Override
    public byte getSkyLight(int x, int y, int z) {
        try {
            parentChunkLock.readLock().lock();
            return skyLights.get(index(x, y, z));
        } finally {
            parentChunkLock.readLock().unlock();
        }
    }

    @Override
    public void setBlockLight(int x, int y, int z, byte light) {
        try {
            parentChunkLock.writeLock().lock();
            blockLights.set(index(x, y, z), light);
        } finally {
            parentChunkLock.writeLock().unlock();
        }
    }

    @Override
    public void setSkyLight(int x, int y, int z, byte light) {
        try {
            parentChunkLock.writeLock().lock();
            skyLights.set(index(x, y, z), light);
        } finally {
            parentChunkLock.writeLock().unlock();
        }
    }

    @Override
    public void writeToNetwork(ByteBuf byteBuf) {
        byteBuf.writeByte(SUB_CHUNK_VERSION);
        byteBuf.writeByte(2);
        byteBuf.writeByte(sectionY);
        blockLayer0.writeToNetwork(byteBuf, BlockState::blockStateHash);
        blockLayer1.writeToNetwork(byteBuf, BlockState::blockStateHash);
    }

    private @Range(from = 0, to = 4095) int index(@Range(from = 0, to = 15) int x, @Range(from = 0, to = 15) int y, @Range(from = 0, to = 15) int z) {
        //The chunk block order of je 1.19 is yzx
        return (y << 8) + (z << 4) + x;
    }
}
