package cn.allay.api.block.interfaces;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;
import cn.allay.api.data.VanillaBlockPropertyTypes;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockNetherWartBehavior extends BlockBehavior {
  BlockType<BlockNetherWartBehavior> NETHER_WART_TYPE = BlockTypeBuilder
          .builder(BlockNetherWartBehavior.class)
          .vanillaBlock(VanillaBlockId.NETHER_WART)
          .setProperties(VanillaBlockPropertyTypes.AGE_4)
          .build();
}