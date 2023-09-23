package cn.allay.api.item.interfaces.beespawnegg;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemBeeSpawnEggStack extends ItemStack {
    ItemType<ItemBeeSpawnEggStack> BEE_SPAWN_EGG_TYPE = ItemTypeBuilder
            .builder(ItemBeeSpawnEggStack.class)
            .vanillaItem(VanillaItemId.BEE_SPAWN_EGG)
            .build();
}