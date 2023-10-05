package cn.allay.api.block.interfaces.wood;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;
import cn.allay.api.data.VanillaBlockPropertyTypes;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockStrippedMangroveWoodBehavior extends BlockBehavior {
  BlockType<BlockStrippedMangroveWoodBehavior> STRIPPED_MANGROVE_WOOD_TYPE = BlockTypeBuilder
          .builder(BlockStrippedMangroveWoodBehavior.class)
          .vanillaBlock(VanillaBlockId.STRIPPED_MANGROVE_WOOD)
          .setProperties(VanillaBlockPropertyTypes.PILLAR_AXIS)
          .build();
}