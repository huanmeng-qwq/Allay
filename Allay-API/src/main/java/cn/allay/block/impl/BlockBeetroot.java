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
public interface BlockBeetroot extends Block {
    BlockType<BlockBeetroot> TYPE = BlockTypeBuilder
            .builder(BlockBeetroot.class)
            .vanillaBlock(VanillaBlockId.BEETROOT, true)
            .withProperties(VanillaBlockPropertyTypes.GROWTH)
            .addBasicComponents()
            .build().register(BlockTypeRegistry.getRegistry());
}