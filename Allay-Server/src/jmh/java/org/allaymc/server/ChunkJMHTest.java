package org.allaymc.server;

import org.allaymc.api.MissingImplementationException;
import org.allaymc.api.world.DimensionInfo;
import org.allaymc.api.world.chunk.Chunk;
import org.allaymc.server.world.chunk.AllayChunk;
import org.allaymc.server.world.chunk.AllayUnsafeChunk;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

import static org.allaymc.api.block.type.BlockTypes.OAK_WOOD;
import static org.allaymc.api.block.type.BlockTypes.STONE;

/**
 * @author Cool_Loong
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Threads(1)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ChunkJMHTest {
    private Chunk chunk;

    @Setup
    public void init() throws MissingImplementationException {
        Allay.initAllay();
        chunk = new AllayChunk(AllayUnsafeChunk.builder().emptyChunk(0, 0, DimensionInfo.OVERWORLD));
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = -64; k < 320; k++) {
                    chunk.setBlockState(i, k, j, OAK_WOOD.getDefaultState());
                }
            }
        }
    }

    @Benchmark
    public void test1() {
        chunk.setBlockState(0, 0, 0, STONE.getDefaultState());
    }

    @Benchmark
    public void test2(Blackhole blackhole) {
        blackhole.consume(chunk.getBlockState(0, 0, 0));
    }

    @Threads(Threads.MAX)
    @Benchmark
    public void test3() {
        chunk.setBlockState(0, 0, 0, STONE.getDefaultState());
    }

    @Threads(Threads.MAX)
    @Benchmark
    public void test4(Blackhole blackhole) {
        blackhole.consume(chunk.getBlockState(0, 0, 0));
    }
}
