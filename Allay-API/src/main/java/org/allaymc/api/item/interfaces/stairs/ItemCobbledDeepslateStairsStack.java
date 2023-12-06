package org.allaymc.api.item.interfaces.stairs;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemCobbledDeepslateStairsStack extends ItemStack {
    ItemType<ItemCobbledDeepslateStairsStack> COBBLED_DEEPSLATE_STAIRS_TYPE = ItemTypeBuilder
            .builder(ItemCobbledDeepslateStairsStack.class)
            .vanillaItem(VanillaItemId.COBBLED_DEEPSLATE_STAIRS)
            .build();
}
