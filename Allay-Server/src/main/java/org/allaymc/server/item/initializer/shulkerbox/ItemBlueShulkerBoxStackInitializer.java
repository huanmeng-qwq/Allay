package org.allaymc.server.item.initializer.shulkerbox;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.interfaces.shulkerbox.ItemBlueShulkerBoxStack;
import org.allaymc.api.item.type.ItemTypeBuilder;
import org.allaymc.api.item.type.ItemTypes;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemBlueShulkerBoxStackInitializer {
  static void init() {
    ItemTypes.BLUE_SHULKER_BOX_TYPE = ItemTypeBuilder
            .builder(ItemBlueShulkerBoxStack.class)
            .vanillaItem(VanillaItemId.BLUE_SHULKER_BOX)
            .build();
  }
}