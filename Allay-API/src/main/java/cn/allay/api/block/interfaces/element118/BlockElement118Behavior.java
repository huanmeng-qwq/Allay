package cn.allay.api.block.interfaces.element118;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockElement118Behavior extends BlockBehavior {
    BlockType<BlockElement118Behavior> ELEMENT_118_TYPE = BlockTypeBuilder
            .builder(BlockElement118Behavior.class)
            .vanillaBlock(VanillaBlockId.ELEMENT_118)
            .build();
}