package cn.allay.api.container.processor;

import cn.allay.api.client.Client;
import cn.allay.api.container.Container;
import lombok.extern.slf4j.Slf4j;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.DropAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseContainer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static cn.allay.api.container.Container.AIR_STACK;
import static org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseStatus.OK;

/**
 * Allay Project 2023/9/23
 *
 * @author daoge_cmd
 */
@Slf4j
public class DropActionProcessor implements ContainerActionProcessor<DropAction> {
    @Override
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.DROP;
    }

    @Override
    public List<ItemStackResponse> handle(DropAction action, Client client, int requestId) {
        Container container = Objects.requireNonNull(client.getPlayerEntity().getReachableContainerBySlotType(action.getSource().getContainer()));
        var count = action.getCount();
        var slot = action.getSource().getSlot();
        var item = container.getItemStack(slot);
        if (item.getStackNetworkId() != action.getSource().getStackNetworkId()) {
            log.warn("mismatch stack network id!");
            return error(requestId);
        }
        if (item == AIR_STACK) {
            log.warn("cannot throw an air!");
            return error(requestId);
        }
        if (item.getCount() < count) {
            log.warn("cannot throw more items than the current amount!");
            return error(requestId);
        }
        client.getPlayerEntity().dropItemWithOutCheck(container, slot, count);
        return Collections.singletonList(
                new ItemStackResponse(
                        OK,
                        requestId,
                        Collections.singletonList(
                                new ItemStackResponseContainer(
                                        container.getSlotType(slot),
                                        Collections.singletonList(
                                                new ItemStackResponseSlot(
                                                        slot,
                                                        slot,
                                                        item.getCount(),
                                                        item.getStackNetworkId(),
                                                        "",
                                                        0
                                                )
                                        )
                                )
                        )
                )
        );
    }
}