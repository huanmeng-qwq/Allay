package org.allaymc.server.block.component;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.dto.PlayerInteractInfo;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.container.FullContainerType;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.world.Dimension;

/**
 * @author daoge_cmd
 */
public class BlockCraftingTableBaseComponentImpl extends BlockBaseComponentImpl {
    public BlockCraftingTableBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType);
    }

    @Override
    public boolean onInteract(ItemStack itemStack, Dimension dimension, PlayerInteractInfo interactInfo) {
        if (super.onInteract(itemStack, dimension, interactInfo)) return true;

        var player = interactInfo.player();
        var craftingTableContainer = player.getContainer(FullContainerType.CRAFTING_TABLE);
        craftingTableContainer.setBlockPos(interactInfo.clickBlockPos());
        craftingTableContainer.addViewer(player);
        return true;
    }
}