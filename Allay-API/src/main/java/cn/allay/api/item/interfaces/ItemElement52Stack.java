package cn.allay.api.item.interfaces;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemElement52Stack extends ItemStack {
    ItemType<ItemElement52Stack> ELEMENT_52_TYPE = ItemTypeBuilder
            .builder(ItemElement52Stack.class)
            .vanillaItem(VanillaItemId.ELEMENT_52)
            .build();
}