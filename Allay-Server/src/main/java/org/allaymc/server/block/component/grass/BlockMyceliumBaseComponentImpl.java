package org.allaymc.server.block.component.grass;

import java.util.concurrent.ThreadLocalRandom;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.interfaces.BlockDirtBehavior;
import org.allaymc.api.block.property.enums.DirtType;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.data.VanillaBlockId;
import org.joml.Vector3i;

import static org.allaymc.api.data.VanillaBlockPropertyTypes.DIRT_TYPE;

/**
 * Allay Project 2024/8/21
 *
 * @author Dhaiven
 */
public class BlockMyceliumBaseComponentImpl extends BlockGrassBaseComponentImpl {
    private boolean mustDied = false;

    public BlockMyceliumBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType, VanillaBlockId.DIRT);
    }

    @Override
    public boolean mustDied(BlockStateWithPos current, BlockState blockAbove) {
        // Mycelium block died when onRandomUpdate is called
        // So put mustDied to true and when onRandomUpdate is called, mycelium will died
        mustDied = super.mustDied(current, blockAbove);
        return false;
    }

    @Override
    public void onRandomUpdate(BlockStateWithPos blockState) {
        if (mustDied) {
            onDied(blockState, diedId.getBlockType().getDefaultState());
            return;
        }
        
        /**
         * Spread logic
         * See https://minecraft.wiki/w/Mycelium#Spread
         */
        var random = ThreadLocalRandom.current();
        var pos = blockState.pos();
        var spreadPos = new Vector3i(
            random.nextInt(pos.x() - 1, pos.x() + 1),
            random.nextInt(pos.y() - 3, pos.y() + 1),
            random.nextInt(pos.z() - 1, pos.z() + 1)
        );
        
		BlockState spreadBlockState = pos.dimension().getBlockState(spreadPos);
        if (spreadBlockState.getBehavior() instanceof BlockDirtBehavior && spreadBlockState.getPropertyValue(DIRT_TYPE) == DirtType.NORMAL) {
            var abovePos = spreadPos.add(0, 1, 0, new Vector3i());
            BlockState blockStateAbove = pos.dimension().getBlockState(abovePos);
            if (blockStateAbove.getBlockType().getMaterial().isTransparent()) {
                pos.dimension().setBlockState(spreadPos, BlockTypes.MYCELIUM.getDefaultState());
            }
        }
    }
}