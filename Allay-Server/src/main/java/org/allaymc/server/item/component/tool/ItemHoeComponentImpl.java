package org.allaymc.server.item.component.tool;

import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.item.component.event.CItemRightClickOnBlockEvent;
import org.allaymc.api.item.component.tool.ItemHoeComponent;
import org.allaymc.api.item.type.ItemTypes;
import org.allaymc.api.math.position.Position3f;
import org.joml.Vector3f;

/**
 * Allay Project 2024/6/23
 *
 * @author daoge_cmd
 */
public class ItemHoeComponentImpl extends ItemBlockBreakingToolComponent implements ItemHoeComponent {

    @EventHandler
    protected void onRightClickOnBlock(CItemRightClickOnBlockEvent event) {
        var clickedBlockPos = event.getInteractInfo().clickBlockPos();
        var dimension = event.getDimension();
        var blockState = dimension.getBlockState(clickedBlockPos);
        var blockType = blockState.getBlockType();
        var player = event.getInteractInfo().player();
        if (blockType == BlockTypes.DIRT_WITH_ROOTS) {
            dimension.setBlockState(clickedBlockPos, BlockTypes.DIRT.getDefaultState());
            dimension.dropItem(ItemTypes.HANGING_ROOTS.createItemStack(), new Vector3f(clickedBlockPos));
            player.onUseItemInHandOn(new Position3f(clickedBlockPos, dimension), blockState);
        } else if (blockType == BlockTypes.GRASS_BLOCK || blockType == BlockTypes.GRASS_PATH) {
            dimension.setBlockState(clickedBlockPos, BlockTypes.FARMLAND.getDefaultState());
            player.onUseItemInHandOn(new Position3f(clickedBlockPos, dimension), blockState);
        }
    }
}
