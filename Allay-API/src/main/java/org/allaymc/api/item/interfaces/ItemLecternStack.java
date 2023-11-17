package org.allaymc.api.item.interfaces;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemLecternStack extends ItemStack {
  ItemType<ItemLecternStack> LECTERN_TYPE = ItemTypeBuilder
          .builder(ItemLecternStack.class)
          .vanillaItem(VanillaItemId.LECTERN)
          .build();
}