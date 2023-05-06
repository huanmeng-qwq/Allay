package cn.allay.block.impl;

import cn.allay.block.Block;
import cn.allay.block.data.VanillaBlockId;
import cn.allay.block.type.BlockType;
import cn.allay.block.type.BlockTypeBuilder;
import cn.allay.block.type.BlockTypeRegistry;

/**
 * Author: daoge_cmd <br>
 * Allay Project <br>
 */
public interface BlockDiamondBlock extends Block {
    BlockType<BlockDiamondBlock> TYPE = BlockTypeBuilder
            .builder(BlockDiamondBlock.class)
            .vanillaBlock(VanillaBlockId.DIAMOND_BLOCK, true)
            .addBasicComponents()
            .build().register(BlockTypeRegistry.getRegistry());
}