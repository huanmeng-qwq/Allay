package org.allaymc.api.eventbus.event.block;

import lombok.Getter;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.eventbus.event.CancellableEvent;

/**
 * Allay Project 2024/8/23
 *
 * @author Dhaiven
 */
@Getter
public class BlockDiedEvent extends BlockEvent implements CancellableEvent {
    protected BlockState newBlockState;

    public BlockDiedEvent(BlockStateWithPos block, BlockState newBlockState) {
        super(block);
        this.newBlockState = newBlockState;
    }
}
