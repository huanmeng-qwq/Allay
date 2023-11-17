package org.allaymc.api.item.interfaces.candle;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemOrangeCandleStack extends ItemStack {
  ItemType<ItemOrangeCandleStack> ORANGE_CANDLE_TYPE = ItemTypeBuilder
          .builder(ItemOrangeCandleStack.class)
          .vanillaItem(VanillaItemId.ORANGE_CANDLE)
          .build();
}