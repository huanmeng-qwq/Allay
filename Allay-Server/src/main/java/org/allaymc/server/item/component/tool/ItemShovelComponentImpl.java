package org.allaymc.server.item.component.tool;

import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.item.component.event.CItemRightClickOnBlockEvent;
import org.allaymc.api.item.component.tool.ItemShovelComponent;
import org.allaymc.api.math.position.Position3f;

/**
 * Allay Project 2024/6/23
 *
 * @author daoge_cmd
 */
public class ItemShovelComponentImpl extends ItemBlockBreakingToolComponent implements ItemShovelComponent {
    @EventHandler
    protected void onRightClickOnBlock(CItemRightClickOnBlockEvent event) {
        var clickedBlockPos = event.getInteractInfo().clickBlockPos();
        var dimension = event.getDimension();
        var blockState = dimension.getBlockState(clickedBlockPos);
        var blockType = blockState.getBlockType();
        if (blockType != BlockTypes.GRASS_BLOCK && blockType != BlockTypes.MYCELIUM && blockType != BlockTypes.PODZOL && blockType != BlockTypes.DIRT_WITH_ROOTS) {
            return;
        }
        
        dimension.setBlockState(clickedBlockPos, BlockTypes.GRASS_PATH.getDefaultState());
        var player = event.getInteractInfo().player();
        player.onUseItemInHandOn(new Position3f(clickedBlockPos, dimension), blockState);
    }
}
