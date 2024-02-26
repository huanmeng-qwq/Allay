package org.allaymc.server.block.initializer.element;

import org.allaymc.api.block.interfaces.element.BlockElement79Behavior;
import org.allaymc.api.block.type.BlockTypeBuilder;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.data.VanillaBlockId;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface BlockElement79BehaviorInitializer {
  static void init() {
    BlockTypes.ELEMENT_79_TYPE = BlockTypeBuilder
            .builder(BlockElement79Behavior.class)
            .vanillaBlock(VanillaBlockId.ELEMENT_79)
            .build();
  }
}