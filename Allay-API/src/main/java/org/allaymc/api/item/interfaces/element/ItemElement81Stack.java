package org.allaymc.api.item.interfaces.element;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemElement81Stack extends ItemStack {
  ItemType<ItemElement81Stack> ELEMENT_81_TYPE = ItemTypeBuilder
          .builder(ItemElement81Stack.class)
          .vanillaItem(VanillaItemId.ELEMENT_81)
          .build();
}