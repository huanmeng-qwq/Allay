package cn.allay.api.item.interfaces.leaves;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemLeavesStack extends ItemStack {
    ItemType<ItemLeavesStack> LEAVES_TYPE = ItemTypeBuilder
            .builder(ItemLeavesStack.class)
            .vanillaItem(VanillaItemId.LEAVES)
            .build();
}