package cn.allay.api.item.interfaces.element53;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemElement53Stack extends ItemStack {
    ItemType<ItemElement53Stack> ELEMENT_53_TYPE = ItemTypeBuilder
            .builder(ItemElement53Stack.class)
            .vanillaItem(VanillaItemId.ELEMENT_53)
            .build();
}