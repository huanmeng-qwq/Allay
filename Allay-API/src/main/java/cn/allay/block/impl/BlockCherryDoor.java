package cn.allay.block.impl;

import cn.allay.block.Block;
import cn.allay.block.data.VanillaBlockId;
import cn.allay.block.property.vanilla.VanillaBlockPropertyTypes;
import cn.allay.block.type.BlockType;
import cn.allay.block.type.BlockTypeBuilder;

/**
 * Author: daoge_cmd <br>
 * Allay Project <br>
 */
public interface BlockCherryDoor extends Block {
    BlockType<BlockCherryDoor> TYPE = BlockTypeBuilder
            .builder(BlockCherryDoor.class)
            .vanillaBlock(VanillaBlockId.CHERRY_DOOR)
            .property(VanillaBlockPropertyTypes.DIRECTION,
                    VanillaBlockPropertyTypes.DOOR_HINGE_BIT,
                    VanillaBlockPropertyTypes.OPEN_BIT,
                    VanillaBlockPropertyTypes.UPPER_BLOCK_BIT)
            .build();
}
