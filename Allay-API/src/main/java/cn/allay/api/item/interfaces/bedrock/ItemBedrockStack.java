package cn.allay.api.item.interfaces.bedrock;

import cn.allay.api.data.VanillaItemId;
import cn.allay.api.item.ItemStack;
import cn.allay.api.item.type.ItemType;
import cn.allay.api.item.type.ItemTypeBuilder;

/**
 * @author daoge_cmd <br>
 * Allay Project <br>
 */
public interface ItemBedrockStack extends ItemStack {
    ItemType<ItemBedrockStack> BEDROCK_TYPE = ItemTypeBuilder
            .builder(ItemBedrockStack.class)
            .vanillaItem(VanillaItemId.BEDROCK)
            .build();
}