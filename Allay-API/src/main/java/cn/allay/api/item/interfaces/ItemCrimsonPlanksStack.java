package cn.allay.api.item.interfaces;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemCrimsonPlanksStack extends ItemStack {
    ItemType<ItemCrimsonPlanksStack> CRIMSON_PLANKS_TYPE = ItemTypeBuilder
            .builder(ItemCrimsonPlanksStack.class)
            .vanillaItem(VanillaItemId.CRIMSON_PLANKS)
            .build();
}