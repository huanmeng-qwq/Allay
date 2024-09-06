package org.allaymc.api.block.dto;

import org.allaymc.api.world.World;

/**
 * Represents a block in the world.
 * <p>
 * Allay Project 2023/8/11
 *
 * @author daoge_cmd
 */
public record BlockPos(
        World world,
        int x,
        int y,
        int z,
        boolean layer
) {
}
