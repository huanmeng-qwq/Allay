package org.allaymc.server.block.component.grass;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.component.PlayerInteractInfo;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.data.BlockFace;
import org.allaymc.api.data.VanillaBlockId;
import org.allaymc.api.eventbus.event.block.BlockDiedEvent;
import org.allaymc.server.block.component.BlockBaseComponentImpl;
import org.joml.Vector3i;

/**
 * Allay Project 2024/6/21
 *
 * @author Dhaiven
 */
public class BlockGrassBaseComponentImpl extends BlockBaseComponentImpl {
    protected final VanillaBlockId diedId;

    public BlockGrassBaseComponentImpl(BlockType<? extends BlockBehavior> blockType, VanillaBlockId diedId) {
        super(blockType);
        this.diedId = diedId;
    }

    @Override
    public void onPlace(BlockStateWithPos currentBlockState, BlockState newBlockState, PlayerInteractInfo placementInfo) {
        var posAbove = currentBlockState.pos().add(0, 1, 0, new Vector3i());
        var aboveBlockState = currentBlockState.pos().dimension().getBlockState(posAbove);
        var newBlockStateWithPos = new BlockStateWithPos(newBlockState, currentBlockState.pos(), currentBlockState.layer());
        if (mustDied(newBlockStateWithPos, aboveBlockState)) {
            died(newBlockStateWithPos, diedId.getBlockType().getDefaultState());
        }
    }

    @Override
    public boolean canKeepExisting(BlockStateWithPos current, BlockStateWithPos neighbor, BlockFace face) {
        if (face == BlockFace.UP && mustDied(current, neighbor.blockState())) {
            died(current, diedId.getBlockType().getDefaultState());
        }

        return true;
    }

    public boolean mustDied(BlockStateWithPos current, BlockState blockAbove) {
        var pos = current.pos();
        var lightAbove = pos.dimension().getInternalLightLevel(pos.x(), pos.y() + 1, pos.z());
        return lightAbove <= 4 && blockAbove.getBlockStateData().light() >= 2;
    }
    
    /**
     * @return {@code true} if the block died else {@code false}
     */
    public boolean died(BlockStateWithPos current, BlockState newBlockState) {
        var event = new BlockDiedEvent(current, newBlockState);
        event.call();
        if (event.isCancelled()) return false;

        current.pos().dimension().setBlockState(current.pos(), event.getNewBlockState());
        return true;
    }
}
