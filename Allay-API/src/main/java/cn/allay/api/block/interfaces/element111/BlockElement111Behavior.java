package cn.allay.api.block.interfaces.element111;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockElement111Behavior extends BlockBehavior {
    BlockType<BlockElement111Behavior> ELEMENT_111_TYPE = BlockTypeBuilder
            .builder(BlockElement111Behavior.class)
            .vanillaBlock(VanillaBlockId.ELEMENT_111)
            .build();
}