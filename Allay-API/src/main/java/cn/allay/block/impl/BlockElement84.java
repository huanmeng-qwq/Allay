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
public interface BlockElement84 extends Block {
    BlockType<BlockElement84> TYPE = BlockTypeBuilder
            .builder(BlockElement84.class)
            .vanillaBlock(VanillaBlockId.ELEMENT_84, true)
            .addBasicComponents()
            .build().register(BlockTypeRegistry.getRegistry());
}