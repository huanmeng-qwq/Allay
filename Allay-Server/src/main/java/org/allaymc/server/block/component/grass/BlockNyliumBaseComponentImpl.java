package org.allaymc.server.block.component.grass;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.component.BlockLiquidComponent;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.data.VanillaBlockId;

/**
 * Allay Project 2024/8/21
 *
 * @author Dhaiven
 */
public class BlockNyliumBaseComponentImpl extends BlockGrassBaseComponentImpl {
    private boolean mustDied = false;

    public BlockNyliumBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType, VanillaBlockId.NETHERRACK);
    }

    @Override
    public boolean mustDied(BlockStateWithPos current, BlockState blockAbove) {
        // Nylium block don't died if blockAbove is water or lava
        if (!(blockAbove.getBehavior() instanceof BlockLiquidComponent)) {
            // Nylium block died when onRandomUpdate is called
            // So put mustDied to true and when onRandomUpdate is called, mycelium will died
            mustDied = super.mustDied(current, blockAbove);
        } else {
            mustDied = false;
        }

        return false;
    }

    @Override
    public void onRandomUpdate(BlockStateWithPos blockState) {
        if (mustDied) {
            onDied(blockState, diedId.getBlockType().getDefaultState());
            return;
        }
    }
}