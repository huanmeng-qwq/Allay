package cn.allay.api.block.interfaces.lightgraycandlecake;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;
import cn.allay.api.data.VanillaBlockPropertyTypes;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockLightGrayCandleCakeBehavior extends BlockBehavior {
    BlockType<BlockLightGrayCandleCakeBehavior> LIGHT_GRAY_CANDLE_CAKE_TYPE = BlockTypeBuilder
            .builder(BlockLightGrayCandleCakeBehavior.class)
            .vanillaBlock(VanillaBlockId.LIGHT_GRAY_CANDLE_CAKE)
            .setProperties(VanillaBlockPropertyTypes.LIT)
            .build();
}