package org.allaymc.api.item.interfaces;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemNetheriteBootsStack extends ItemStack {
  ItemType<ItemNetheriteBootsStack> NETHERITE_BOOTS_TYPE = ItemTypeBuilder
          .builder(ItemNetheriteBootsStack.class)
          .vanillaItem(VanillaItemId.NETHERITE_BOOTS)
          .build();
}