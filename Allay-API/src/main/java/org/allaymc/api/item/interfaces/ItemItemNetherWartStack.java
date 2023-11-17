package org.allaymc.api.item.interfaces;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemItemNetherWartStack extends ItemStack {
  ItemType<ItemItemNetherWartStack> ITEM_NETHER_WART_TYPE = ItemTypeBuilder
          .builder(ItemItemNetherWartStack.class)
          .vanillaItem(VanillaItemId.ITEM_NETHER_WART)
          .build();
}