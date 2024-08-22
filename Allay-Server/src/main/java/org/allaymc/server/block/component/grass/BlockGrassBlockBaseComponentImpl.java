package org.allaymc.server.block.component.grass;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.data.VanillaBlockId;

import static org.allaymc.api.block.type.BlockTypes.POWDER_SNOW;
import static org.allaymc.api.block.type.BlockTypes.SNOW;
import static org.allaymc.api.block.type.BlockTypes.SNOW_LAYER;

/**
 * Allay Project 2024/8/21
 *
 * @author Dhaiven
 */
public class BlockGrassBlockBaseComponentImpl extends BlockGrassBaseComponentImpl {
    private boolean mustDied = false;

    public BlockGrassBlockBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType, VanillaBlockId.DIRT);
    }

    @Override
    public boolean mustDied(BlockStateWithPos current, BlockState blockAbove) {
        var blockAboveType = blockAbove.getBlockType();
        // Grass block don't died if blockAbove is snow
        if (blockAboveType != SNOW && blockAboveType != POWDER_SNOW && blockAboveType != SNOW_LAYER) {
            // Grass block died when onRandomUpdate is called
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
        
        /**
         * TODO: spread logic
         */
    }
}