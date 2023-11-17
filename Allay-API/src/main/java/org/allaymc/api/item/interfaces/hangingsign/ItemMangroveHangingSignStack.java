package org.allaymc.api.item.interfaces.hangingsign;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemMangroveHangingSignStack extends ItemStack {
  ItemType<ItemMangroveHangingSignStack> MANGROVE_HANGING_SIGN_TYPE = ItemTypeBuilder
          .builder(ItemMangroveHangingSignStack.class)
          .vanillaItem(VanillaItemId.MANGROVE_HANGING_SIGN)
          .build();
}