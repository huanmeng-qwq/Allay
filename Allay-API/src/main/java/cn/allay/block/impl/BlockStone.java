package cn.allay.block.impl;

import cn.allay.block.Block;
import cn.allay.block.data.VanillaBlockId;
import cn.allay.block.property.vanilla.VanillaBlockPropertyTypes;
import cn.allay.block.type.BlockType;
import cn.allay.block.type.BlockTypeBuilder;
import cn.allay.block.type.BlockTypeRegistry;

/**
 * Author: daoge_cmd <br>
 * Allay Project <br>
 */
public interface BlockStone extends Block {
    BlockType<BlockStone> TYPE = BlockTypeBuilder
            .builder(BlockStone.class)
            .vanillaBlock(VanillaBlockId.STONE, true)
            .withProperties(VanillaBlockPropertyTypes.STONE_TYPE)
            .addBasicComponents()
            .build().register(BlockTypeRegistry.getRegistry());
}