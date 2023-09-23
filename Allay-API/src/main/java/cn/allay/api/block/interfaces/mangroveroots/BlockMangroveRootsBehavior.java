package cn.allay.api.block.interfaces.mangroveroots;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockMangroveRootsBehavior extends BlockBehavior {
    BlockType<BlockMangroveRootsBehavior> MANGROVE_ROOTS_TYPE = BlockTypeBuilder
            .builder(BlockMangroveRootsBehavior.class)
            .vanillaBlock(VanillaBlockId.MANGROVE_ROOTS)
            .build();
}