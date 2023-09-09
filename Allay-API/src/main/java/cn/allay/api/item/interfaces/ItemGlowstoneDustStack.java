package cn.allay.api.item.interfaces;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemGlowstoneDustStack extends ItemStack {
    ItemType<ItemGlowstoneDustStack> GLOWSTONE_DUST_TYPE = ItemTypeBuilder
            .builder(ItemGlowstoneDustStack.class)
            .vanillaItem(VanillaItemId.GLOWSTONE_DUST)
            .build();
}