package org.allaymc.server.block.component.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.allaymc.api.block.poi.BlockStateWithPos;
import org.allaymc.api.block.poi.PlayerInteractInfo;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.eventbus.event.Event;

/**
 * Allay Project 2023/9/15
 *
 * @author daoge_cmd
 */
@Getter
@AllArgsConstructor
public class CBlockOnReplaceEvent extends Event {
    protected BlockStateWithPos currentBlockState;
    protected BlockState newBlockState;
    protected PlayerInteractInfo placementInfo; // Can be null
}