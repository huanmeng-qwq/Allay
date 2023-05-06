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
public interface BlockBeehive extends Block {
    BlockType<BlockBeehive> TYPE = BlockTypeBuilder
            .builder(BlockBeehive.class)
            .vanillaBlock(VanillaBlockId.BEEHIVE, true)
            .withProperties(VanillaBlockPropertyTypes.DIRECTION,
                    VanillaBlockPropertyTypes.HONEY_LEVEL)
            .addBasicComponents()
            .build().register(BlockTypeRegistry.getRegistry());
}