package cn.allay.api.container.impl;

import cn.allay.api.container.BaseContainer;
import cn.allay.api.container.FullContainerType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;

/**
 * Allay Project 2023/7/22
 *
 * @author daoge_cmd
 */
public class PlayerInventoryContainer extends BaseContainer {
    public PlayerInventoryContainer() {
        super(36);
    }

    @Override
    public ContainerSlotType getSlotType(int slot) {
        if (slot >= 0 && slot <= 8) {
            return ContainerSlotType.HOTBAR;
        } else if (slot >= 9 && slot <= 35) {
            return ContainerSlotType.INVENTORY;
        } else {
            throw new IndexOutOfBoundsException("Slot must be between 0 and 35");
        }
    }

    @Override
    public FullContainerType getContainerType() {
        return FullContainerType.PLAYER_INVENTORY;
    }
}