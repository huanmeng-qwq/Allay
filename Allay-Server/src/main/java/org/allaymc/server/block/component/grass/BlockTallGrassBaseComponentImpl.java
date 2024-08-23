package org.allaymc.server.block.component.grass;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.component.PlayerInteractInfo;
import org.allaymc.api.block.component.RequireBlockProperty;
import org.allaymc.api.data.BlockFace;
import org.allaymc.api.entity.Entity;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.type.ItemTypes;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.property.type.BlockPropertyType;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.world.Dimension;
import org.joml.Vector3ic;

import static org.allaymc.api.data.VanillaBlockPropertyTypes.UPPER_BLOCK_BIT;

import java.util.Set;

/**
 * Suitable for two-block high plants that can drop wheat seeds
 * <p>
 * Allay Project 2024/6/18
 *
 * @author daoge_cmd
 */
@RequireBlockProperty(type = BlockPropertyType.Type.BOOLEAN, name = "upper_block_bit")
public class BlockTallGrassBaseComponentImpl extends BlockShortGrassBaseComponentImpl {
    public BlockTallGrassBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType);
    }

    @Override
    public boolean place(Dimension dimension, BlockState blockState, Vector3ic placeBlockPos, PlayerInteractInfo placementInfo) {
        checkPlaceMethodParam(dimension, blockState, placeBlockPos, placementInfo);
        /**
         * In vanilla, block place but break at the next onRandomUpdate or onNeighborUpdate
         * Here, we don't reproduce this comportement so we cancel block placement
         */
        if (placeBlockPos.y() >= dimension.getDimensionInfo().maxHeight()) {
            return false;
        }

        dimension.setBlockState(
                placeBlockPos.x(), placeBlockPos.y(), placeBlockPos.z(),
                blockState,
                placementInfo
        );
        dimension.setBlockState(
                placeBlockPos.x(), placeBlockPos.y() + 1, placeBlockPos.z(),
                blockState.setProperty(UPPER_BLOCK_BIT, true),
                placementInfo
        );
        return true;
    }

    @Override
    public boolean canKeepExisting(BlockStateWithPos current, BlockStateWithPos neighbor, BlockFace face) {
        if (face == BlockFace.UP) {
            if (!current.blockState().getPropertyValue(UPPER_BLOCK_BIT)) {
                return !notSamePlant(neighbor.blockState());
            }
        } else if (face == BlockFace.DOWN) {
            if (current.blockState().getPropertyValue(UPPER_BLOCK_BIT)) {
                return !notSamePlant(neighbor.blockState());
            }
            return canPlaceOn(neighbor.blockState().getBlockType());
        }

        return true;
    }

    protected boolean notSamePlant(BlockState otherBlock) {
        return otherBlock.getBlockType() != blockType;
    }

    @Override
    public boolean isDroppable(BlockStateWithPos blockState, ItemStack usedItem, Entity entity) {
        // Don't drop if entity is null
        return entity != null && super.isDroppable(blockState, usedItem, entity);
    }

    @Override
    public Set<ItemStack> getDrops(BlockStateWithPos blockState, ItemStack usedItem, Entity entity) {
        if (usedItem.getItemType() == ItemTypes.SHEARS) {
            return Set.of(ItemTypes.SHORT_GRASS.createItemStack(2));
        }

        return super.getDrops(blockState, usedItem, entity);
    }
}
