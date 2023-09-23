package cn.allay.api.block.interfaces.detectorrail;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;
import cn.allay.api.data.VanillaBlockPropertyTypes;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockDetectorRailBehavior extends BlockBehavior {
    BlockType<BlockDetectorRailBehavior> DETECTOR_RAIL_TYPE = BlockTypeBuilder
            .builder(BlockDetectorRailBehavior.class)
            .vanillaBlock(VanillaBlockId.DETECTOR_RAIL)
            .setProperties(VanillaBlockPropertyTypes.RAIL_DATA_BIT, VanillaBlockPropertyTypes.RAIL_DIRECTION_6)
            .build();
}