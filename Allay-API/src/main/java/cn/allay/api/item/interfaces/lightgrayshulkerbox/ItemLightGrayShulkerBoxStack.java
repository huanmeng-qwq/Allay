package cn.allay.api.item.interfaces.lightgrayshulkerbox;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemLightGrayShulkerBoxStack extends ItemStack {
    ItemType<ItemLightGrayShulkerBoxStack> LIGHT_GRAY_SHULKER_BOX_TYPE = ItemTypeBuilder
            .builder(ItemLightGrayShulkerBoxStack.class)
            .vanillaItem(VanillaItemId.LIGHT_GRAY_SHULKER_BOX)
            .build();
}