package org.allaymc.server.item.initializer.element;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.interfaces.element.ItemElement74Stack;
import org.allaymc.api.item.type.ItemTypeBuilder;
import org.allaymc.api.item.type.ItemTypes;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemElement74StackInitializer {
  static void init() {
    ItemTypes.ELEMENT_74_TYPE = ItemTypeBuilder
            .builder(ItemElement74Stack.class)
            .vanillaItem(VanillaItemId.ELEMENT_74)
            .build();
  }
}