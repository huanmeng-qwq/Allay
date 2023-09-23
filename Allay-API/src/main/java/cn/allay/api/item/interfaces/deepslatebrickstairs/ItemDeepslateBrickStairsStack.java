package cn.allay.api.item.interfaces.deepslatebrickstairs;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.component.base.ItemBaseComponentImpl;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemDeepslateBrickStairsStack extends ItemStack {
    ItemType<ItemDeepslateBrickStairsStack> DEEPSLATE_BRICK_STAIRS_TYPE = ItemTypeBuilder
            .builder(ItemDeepslateBrickStairsStack.class)
            .vanillaItem(VanillaItemId.DEEPSLATE_BRICK_STAIRS)
            .addComponent(ItemBaseComponentImpl::new, ItemBaseComponentImpl.class)
            .build();
}