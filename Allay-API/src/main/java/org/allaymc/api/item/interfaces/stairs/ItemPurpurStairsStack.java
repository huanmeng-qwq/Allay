package org.allaymc.api.item.interfaces.stairs;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemPurpurStairsStack extends ItemStack {
    ItemType<ItemPurpurStairsStack> PURPUR_STAIRS_TYPE = ItemTypeBuilder
            .builder(ItemPurpurStairsStack.class)
            .vanillaItem(VanillaItemId.PURPUR_STAIRS)
            .build();
}
