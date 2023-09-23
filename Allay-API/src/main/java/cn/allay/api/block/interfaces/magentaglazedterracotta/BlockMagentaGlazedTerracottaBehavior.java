package cn.allay.api.block.interfaces.magentaglazedterracotta;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;
import cn.allay.api.data.VanillaBlockPropertyTypes;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockMagentaGlazedTerracottaBehavior extends BlockBehavior {
    BlockType<BlockMagentaGlazedTerracottaBehavior> MAGENTA_GLAZED_TERRACOTTA_TYPE = BlockTypeBuilder
            .builder(BlockMagentaGlazedTerracottaBehavior.class)
            .vanillaBlock(VanillaBlockId.MAGENTA_GLAZED_TERRACOTTA)
            .setProperties(VanillaBlockPropertyTypes.FACING_DIRECTION)
            .build();
}