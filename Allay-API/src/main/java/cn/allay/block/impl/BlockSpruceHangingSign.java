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
public interface BlockSpruceHangingSign extends Block {
    BlockType<BlockSpruceHangingSign> TYPE = BlockTypeBuilder
            .builder(BlockSpruceHangingSign.class)
            .vanillaBlock(VanillaBlockId.SPRUCE_HANGING_SIGN, true)
            .withProperties(VanillaBlockPropertyTypes.ATTACHED_BIT,
                    VanillaBlockPropertyTypes.FACING_DIRECTION,
                    VanillaBlockPropertyTypes.GROUND_SIGN_DIRECTION,
                    VanillaBlockPropertyTypes.HANGING)
            .addBasicComponents()
            .build().register(BlockTypeRegistry.getRegistry());
}