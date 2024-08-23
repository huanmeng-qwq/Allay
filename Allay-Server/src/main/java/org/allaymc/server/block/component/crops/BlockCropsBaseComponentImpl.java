package org.allaymc.server.block.component.crops;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.component.RequireBlockProperty;
import org.allaymc.api.block.component.PlayerInteractInfo;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.property.type.BlockPropertyType;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.data.BlockFace;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.interfaces.ItemBoneMealStack;
import org.allaymc.api.math.position.Position3ic;
import org.allaymc.server.block.component.BlockBaseComponentImpl;

import java.util.concurrent.ThreadLocalRandom;

import static org.allaymc.api.block.type.BlockTypes.FARMLAND;
import static org.allaymc.api.data.VanillaBlockPropertyTypes.GROWTH;

/**
 * Allay Project 2024/8/15
 *
 * @author Dhaiven
 */
// TODO
@RequireBlockProperty(type = BlockPropertyType.Type.INT, name = "growth")
public class BlockCropsBaseComponentImpl extends BlockBaseComponentImpl {
    public BlockCropsBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType);
    }

    @Override
    public boolean canKeepExisting(BlockStateWithPos current, BlockStateWithPos neighbor, BlockFace face) {
        if (face != BlockFace.DOWN) return true;
        return neighbor.blockState().getBlockType() == FARMLAND;
    }

    @Override
    public void onRandomUpdate(BlockStateWithPos blockState) {
        if (blockState.pos().dimension().getInternalLightLevel(blockState.pos()) < 9) return;
        if (ThreadLocalRandom.current().nextInt(2) != 1) return;

        var oldGrowth = blockState.blockState().getPropertyValue(GROWTH);
        if (oldGrowth < GROWTH.getMax()) {
            grow(blockState.pos(), oldGrowth + 1);
        }
    }

    @Override
    public boolean onInteract(BlockStateWithPos current, ItemStack itemStack, PlayerInteractInfo interactInfo) {
        if (super.onInteract(current, itemStack, interactInfo)) return true;

        if (itemStack instanceof ItemBoneMealStack) {
            if (current.blockState().getPropertyValue(GROWTH) < GROWTH.getMax()) {
                int newAge = ThreadLocalRandom.current().nextInt(3) + 2; //Between 2 and 5
                grow(current.pos(), newAge);
                //TODO: BoneMeal particle
                interactInfo.player().tryConsumeItemInHand();
            }
        }

        return true;
    }

    public void grow(Position3ic pos, Integer newAge) {
        if (newAge < GROWTH.getMin()) newAge = GROWTH.getMin();
        if (newAge > GROWTH.getMax()) newAge = GROWTH.getMax();

        //TODO: event
        updateBlockProperty(GROWTH, newAge, pos);
    }
}
