package org.allaymc.api.eventbus.event.block;

import lombok.Getter;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.eventbus.event.CancellableEvent;
import org.joml.Vector3i;

/**
 * Allay Project 2024/8/23
 *
 * @author Dhaiven
 */
@Getter
public class BlockSpreadEvent extends BlockEvent implements CancellableEvent {
    protected Vector3i spreadPos;
    protected BlockState newBlockState;

    public BlockSpreadEvent(BlockStateWithPos block, Vector3i spreadPos, BlockState newBlockState) {
        super(block);
        this.spreadPos = spreadPos;
        this.newBlockState = newBlockState;
    }
}
