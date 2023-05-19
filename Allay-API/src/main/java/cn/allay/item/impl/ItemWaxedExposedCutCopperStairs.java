package cn.allay.item.impl;

import cn.allay.item.ItemStack;
import cn.allay.item.data.VanillaItemId;
import cn.allay.item.type.ItemType;
import cn.allay.item.type.ItemTypeBuilder;
import cn.allay.item.type.ItemTypeRegistry;

/**
 * Author: daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemWaxedExposedCutCopperStairs extends ItemStack {
    ItemType<ItemWaxedExposedCutCopperStairs> TYPE = ItemTypeBuilder
            .builder(ItemWaxedExposedCutCopperStairs.class)
            .vanillaItem(VanillaItemId.WAXED_EXPOSED_CUT_COPPER_STAIRS, true)
            .addBasicComponents()
            .build().register(ItemTypeRegistry.getRegistry());
}