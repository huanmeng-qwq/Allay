package cn.allay.api.block.interfaces.deny;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockDenyBehavior extends BlockBehavior {
    BlockType<BlockDenyBehavior> DENY_TYPE = BlockTypeBuilder
            .builder(BlockDenyBehavior.class)
            .vanillaBlock(VanillaBlockId.DENY)
            .build();
}