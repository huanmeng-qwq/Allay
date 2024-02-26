package org.allaymc.server.item.initializer.slab;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.interfaces.slab.ItemStoneBlockSlab2Stack;
import org.allaymc.api.item.type.ItemTypeBuilder;
import org.allaymc.api.item.type.ItemTypes;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemStoneBlockSlab2StackInitializer {
  static void init() {
    ItemTypes.STONE_BLOCK_SLAB2_TYPE = ItemTypeBuilder
            .builder(ItemStoneBlockSlab2Stack.class)
            .vanillaItem(VanillaItemId.STONE_BLOCK_SLAB2)
            .build();
  }
}