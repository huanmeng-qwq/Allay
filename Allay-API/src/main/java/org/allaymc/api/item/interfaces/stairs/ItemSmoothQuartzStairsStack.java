package org.allaymc.api.item.interfaces.stairs;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemSmoothQuartzStairsStack extends ItemStack {
    ItemType<ItemSmoothQuartzStairsStack> SMOOTH_QUARTZ_STAIRS_TYPE = ItemTypeBuilder
            .builder(ItemSmoothQuartzStairsStack.class)
            .vanillaItem(VanillaItemId.SMOOTH_QUARTZ_STAIRS)
            .build();
}
