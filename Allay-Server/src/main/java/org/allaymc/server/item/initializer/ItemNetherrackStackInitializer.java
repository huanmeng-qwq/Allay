package org.allaymc.server.item.initializer;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.interfaces.ItemNetherrackStack;
import org.allaymc.api.item.type.ItemTypeBuilder;
import org.allaymc.api.item.type.ItemTypes;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemNetherrackStackInitializer {
  static void init() {
    ItemTypes.NETHERRACK_TYPE = ItemTypeBuilder
            .builder(ItemNetherrackStack.class)
            .vanillaItem(VanillaItemId.NETHERRACK)
            .build();
  }
}