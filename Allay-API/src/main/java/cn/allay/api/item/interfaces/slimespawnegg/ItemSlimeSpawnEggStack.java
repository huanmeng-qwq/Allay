package cn.allay.api.item.interfaces.slimespawnegg;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemSlimeSpawnEggStack extends ItemStack {
    ItemType<ItemSlimeSpawnEggStack> SLIME_SPAWN_EGG_TYPE = ItemTypeBuilder
            .builder(ItemSlimeSpawnEggStack.class)
            .vanillaItem(VanillaItemId.SLIME_SPAWN_EGG)
            .build();
}