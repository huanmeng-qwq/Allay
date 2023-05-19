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
public interface ItemElement64 extends ItemStack {
    ItemType<ItemElement64> TYPE = ItemTypeBuilder
            .builder(ItemElement64.class)
            .vanillaItem(VanillaItemId.ELEMENT_64, true)
            .addBasicComponents()
            .build().register(ItemTypeRegistry.getRegistry());
}