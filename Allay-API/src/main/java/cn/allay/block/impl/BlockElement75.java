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
public interface BlockElement75 extends Block {
    BlockType<BlockElement75> TYPE = BlockTypeBuilder
            .builder(BlockElement75.class)
            .vanillaBlock(VanillaBlockId.ELEMENT_75, true)
            .addBasicComponents()
            .build().register(BlockTypeRegistry.getRegistry());
}