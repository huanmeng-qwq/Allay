package cn.allay.api.item.interfaces.element81;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemElement81Stack extends ItemStack {
    ItemType<ItemElement81Stack> ELEMENT_81_TYPE = ItemTypeBuilder
            .builder(ItemElement81Stack.class)
            .vanillaItem(VanillaItemId.ELEMENT_81)
            .build();
}