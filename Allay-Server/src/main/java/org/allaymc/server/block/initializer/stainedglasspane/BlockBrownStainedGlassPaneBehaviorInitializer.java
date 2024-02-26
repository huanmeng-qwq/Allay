package org.allaymc.server.block.initializer.stainedglasspane;

import org.allaymc.api.block.interfaces.stainedglasspane.BlockBrownStainedGlassPaneBehavior;
import org.allaymc.api.block.type.BlockTypeBuilder;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.data.VanillaBlockId;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface BlockBrownStainedGlassPaneBehaviorInitializer {
  static void init() {
    BlockTypes.BROWN_STAINED_GLASS_PANE_TYPE = BlockTypeBuilder
            .builder(BlockBrownStainedGlassPaneBehavior.class)
            .vanillaBlock(VanillaBlockId.BROWN_STAINED_GLASS_PANE)
            .build();
  }
}