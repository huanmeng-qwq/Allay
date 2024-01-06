package org.allaymc.api.item.interfaces.trapdoor;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemWeatheredCopperTrapdoorStack extends ItemStack {
  ItemType<ItemWeatheredCopperTrapdoorStack> WEATHERED_COPPER_TRAPDOOR_TYPE = ItemTypeBuilder
          .builder(ItemWeatheredCopperTrapdoorStack.class)
          .vanillaItem(VanillaItemId.WEATHERED_COPPER_TRAPDOOR)
          .build();
}