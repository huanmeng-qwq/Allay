package org.allaymc.api.item.interfaces.coral;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemFireCoralStack extends ItemStack {
  ItemType<ItemFireCoralStack> FIRE_CORAL_TYPE = ItemTypeBuilder
          .builder(ItemFireCoralStack.class)
          .vanillaItem(VanillaItemId.FIRE_CORAL)
          .build();
}