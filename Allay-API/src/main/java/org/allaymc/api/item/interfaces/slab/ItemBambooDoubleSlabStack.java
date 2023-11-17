package org.allaymc.api.item.interfaces.slab;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemBambooDoubleSlabStack extends ItemStack {
  ItemType<ItemBambooDoubleSlabStack> BAMBOO_DOUBLE_SLAB_TYPE = ItemTypeBuilder
          .builder(ItemBambooDoubleSlabStack.class)
          .vanillaItem(VanillaItemId.BAMBOO_DOUBLE_SLAB)
          .build();
}