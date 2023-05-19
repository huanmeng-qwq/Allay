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
public interface ItemBirchLog extends ItemStack {
    ItemType<ItemBirchLog> TYPE = ItemTypeBuilder
            .builder(ItemBirchLog.class)
            .vanillaItem(VanillaItemId.BIRCH_LOG, true)
            .addBasicComponents()
            .build().register(ItemTypeRegistry.getRegistry());
}