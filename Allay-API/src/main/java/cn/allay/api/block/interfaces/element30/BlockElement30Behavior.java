package cn.allay.api.block.interfaces.element30;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockElement30Behavior extends BlockBehavior {
    BlockType<BlockElement30Behavior> ELEMENT_30_TYPE = BlockTypeBuilder
            .builder(BlockElement30Behavior.class)
            .vanillaBlock(VanillaBlockId.ELEMENT_30)
            .build();
}