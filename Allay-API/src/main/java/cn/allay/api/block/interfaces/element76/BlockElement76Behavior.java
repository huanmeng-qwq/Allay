package cn.allay.api.block.interfaces.element76;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockElement76Behavior extends BlockBehavior {
    BlockType<BlockElement76Behavior> ELEMENT_76_TYPE = BlockTypeBuilder
            .builder(BlockElement76Behavior.class)
            .vanillaBlock(VanillaBlockId.ELEMENT_76)
            .build();
}