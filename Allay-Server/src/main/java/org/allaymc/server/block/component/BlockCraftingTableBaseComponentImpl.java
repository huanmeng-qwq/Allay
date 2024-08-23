package org.allaymc.server.block.component;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.component.PlayerInteractInfo;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.container.FullContainerType;
import org.allaymc.api.item.ItemStack;

/**
 * Allay Project 2023/12/6
 *
 * @author daoge_cmd
 */
public class BlockCraftingTableBaseComponentImpl extends BlockBaseComponentImpl {
    public BlockCraftingTableBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType);
    }

    @Override
    public boolean onInteract(BlockStateWithPos current, ItemStack itemStack, PlayerInteractInfo interactInfo) {
        if (super.onInteract(current, itemStack, interactInfo)) return true;

        var player = interactInfo.player();
        var craftingTableContainer = player.getContainer(FullContainerType.CRAFTING_TABLE);
        craftingTableContainer.setBlockPos(interactInfo.clickBlockPos());
        craftingTableContainer.addViewer(player);
        return true;
    }
}
