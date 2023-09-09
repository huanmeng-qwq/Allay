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
public interface BlockDoubleStoneBlockSlabBehavior extends BlockBehavior {
    BlockType<BlockDoubleStoneBlockSlabBehavior> DOUBLE_STONE_BLOCK_SLAB_TYPE = BlockTypeBuilder
            .builder(BlockDoubleStoneBlockSlabBehavior.class)
            .vanillaBlock(VanillaBlockId.DOUBLE_STONE_BLOCK_SLAB)
            .setProperties(VanillaBlockPropertyTypes.STONE_SLAB_TYPE, VanillaBlockPropertyTypes.TOP_SLOT_BIT)
            .build();
}