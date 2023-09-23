package cn.allay.api.item.interfaces.emptymap;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemEmptyMapStack extends ItemStack {
    ItemType<ItemEmptyMapStack> EMPTY_MAP_TYPE = ItemTypeBuilder
            .builder(ItemEmptyMapStack.class)
            .vanillaItem(VanillaItemId.EMPTY_MAP)
            .build();
}