package org.allaymc.server.block.initializer.stainedglass;

import org.allaymc.api.block.interfaces.stainedglass.BlockHardYellowStainedGlassBehavior;
import org.allaymc.api.block.type.BlockTypeBuilder;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.data.VanillaBlockId;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface BlockHardYellowStainedGlassBehaviorInitializer {
  static void init() {
    BlockTypes.HARD_YELLOW_STAINED_GLASS_TYPE = BlockTypeBuilder
            .builder(BlockHardYellowStainedGlassBehavior.class)
            .vanillaBlock(VanillaBlockId.HARD_YELLOW_STAINED_GLASS)
            .build();
  }
}