package cn.allay.api.item.interfaces.bed;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemBedStack extends ItemStack {
    ItemType<ItemBedStack> BED_TYPE = ItemTypeBuilder
            .builder(ItemBedStack.class)
            .vanillaItem(VanillaItemId.BED)
            .build();
}