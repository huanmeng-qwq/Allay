package org.allaymc.server.block.component.grass;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.data.VanillaBlockId;

/**
 * Allay Project 2024/8/21
 *
 * @author Dhaiven
 */
public class BlockGrassPathBaseComponentImpl extends BlockGrassBaseComponentImpl {
    public BlockGrassPathBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType, VanillaBlockId.DIRT);
    }

    @Override
    public boolean mustDied(BlockStateWithPos current, BlockState blockAbove) {
        return blockAbove.getBlockType().getMaterial().isSolid();
    }
}
