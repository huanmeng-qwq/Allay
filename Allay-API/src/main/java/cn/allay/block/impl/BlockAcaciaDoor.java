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
public interface BlockAcaciaDoor extends Block {
  BlockType<BlockAcaciaDoor> TYPE = BlockTypeBuilder
          .builder(BlockAcaciaDoor.class)
          .vanillaBlock(VanillaBlockId.ACACIA_DOOR, true)
          .withProperties(VanillaBlockPropertyTypes.DIRECTION,
                  VanillaBlockPropertyTypes.DOOR_HINGE_BIT,
                  VanillaBlockPropertyTypes.OPEN_BIT,
                  VanillaBlockPropertyTypes.UPPER_BLOCK_BIT)
          .addBasicComponents()
          .build().register(BlockTypeRegistry.getRegistry());
}