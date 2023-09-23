package cn.allay.api.item.interfaces.enderdragonspawnegg;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemEnderDragonSpawnEggStack extends ItemStack {
    ItemType<ItemEnderDragonSpawnEggStack> ENDER_DRAGON_SPAWN_EGG_TYPE = ItemTypeBuilder
            .builder(ItemEnderDragonSpawnEggStack.class)
            .vanillaItem(VanillaItemId.ENDER_DRAGON_SPAWN_EGG)
            .build();
}