package cn.allay.api.item.interfaces.skullbannerpattern;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemSkullBannerPatternStack extends ItemStack {
    ItemType<ItemSkullBannerPatternStack> SKULL_BANNER_PATTERN_TYPE = ItemTypeBuilder
            .builder(ItemSkullBannerPatternStack.class)
            .vanillaItem(VanillaItemId.SKULL_BANNER_PATTERN)
            .build();
}