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
public interface ItemCoralBlock extends ItemStack {
    ItemType<ItemCoralBlock> TYPE = ItemTypeBuilder
            .builder(ItemCoralBlock.class)
            .vanillaItem(VanillaItemId.CORAL_BLOCK, true)
            .addBasicComponents()
            .build().register(ItemTypeRegistry.getRegistry());
}