package org.allaymc.api.item.interfaces.stairs;

import org.allaymc.api.data.VanillaItemId;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemType;
import org.allaymc.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemEndBrickStairsStack extends ItemStack {
    ItemType<ItemEndBrickStairsStack> END_BRICK_STAIRS_TYPE = ItemTypeBuilder
            .builder(ItemEndBrickStairsStack.class)
            .vanillaItem(VanillaItemId.END_BRICK_STAIRS)
            .build();
}
