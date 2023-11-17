package org.allaymc.api.item.interfaces.concrete;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemPurpleConcreteStack extends ItemStack {
  ItemType<ItemPurpleConcreteStack> PURPLE_CONCRETE_TYPE = ItemTypeBuilder
          .builder(ItemPurpleConcreteStack.class)
          .vanillaItem(VanillaItemId.PURPLE_CONCRETE)
          .build();
}