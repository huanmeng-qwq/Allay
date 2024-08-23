package org.allaymc.server.block.component.grass;

import org.allaymc.api.block.BlockBehavior;
import org.allaymc.api.block.BlockStateWithPos;
import org.allaymc.api.block.interfaces.liquid.BlockFlowingWaterBehavior;
import org.allaymc.api.block.interfaces.liquid.BlockWaterBehavior;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.block.type.BlockType;
import org.allaymc.api.data.VanillaBlockId;
import org.allaymc.api.math.position.Position3i;
import org.joml.Vector3i;

import static org.allaymc.api.data.VanillaBlockPropertyTypes.MOISTURIZED_AMOUNT;

/**
 * Allay Project 2024/8/21
 *
 * @author Dhaiven
 */
public class BlockFarmlandBaseComponentImpl extends BlockGrassBaseComponentImpl {
    private Integer WATER_SEARCH_HORIZONTAL_LENGTH = 9;
	private Integer WATER_SEARCH_VERTICAL_LENGTH = 2;

    private BlockStateWithPos waterPosition = null;

    public BlockFarmlandBaseComponentImpl(BlockType<? extends BlockBehavior> blockType) {
        super(blockType, VanillaBlockId.DIRT);
    }

    @Override
    public boolean mustDied(BlockStateWithPos current, BlockState blockAbove) {
        return blockAbove.getBlockType().getMaterial().isSolid();
    }

    @Override
    public void onRandomUpdate(BlockStateWithPos blockState) {
        var actualHydratation = blockState.blockState().getPropertyValue(MOISTURIZED_AMOUNT);
        if (isHydrate(blockState)) {
            if (actualHydratation != MOISTURIZED_AMOUNT.getMax()) {
                changeHydratation(blockState, MOISTURIZED_AMOUNT.getMax());
            }
        } else {
            if (actualHydratation == MOISTURIZED_AMOUNT.getMin()) {
                died(blockState, diedId.getBlockType().getDefaultState());
            } else {
                changeHydratation(blockState, actualHydratation - 1);
            }
        }
    }

    public boolean isHydrate(BlockStateWithPos current) {
        var pos = current.pos();
        if (waterPosition != null) {
            var actualBlockState = pos.dimension().getBlockState(
                waterPosition.pos(), waterPosition.layer()
            );
            if (actualBlockState.getBehavior() == waterPosition.blockState().getBehavior()) {
                return true;
            }

            waterPosition = null;
        }

        var x = pos.x() - Math.round(WATER_SEARCH_HORIZONTAL_LENGTH / 2);
        var y = pos.y();
        var z = pos.z() - Math.round(WATER_SEARCH_HORIZONTAL_LENGTH / 2);
        for (var yRange = 0; yRange < WATER_SEARCH_VERTICAL_LENGTH; yRange++) {
            for (var xRange = 0; xRange < WATER_SEARCH_HORIZONTAL_LENGTH; xRange++) {
                for (var zRange = 0; zRange < WATER_SEARCH_HORIZONTAL_LENGTH; zRange++) {
                    var calculatePos = new Vector3i(x + xRange, y + yRange, z + zRange);
                    for (var layer : new int[] { 0, 1 }) {
                        var blockStateAtPos = pos.dimension().getBlockState(calculatePos, layer);
                        var blockBehaviorAtPos = blockStateAtPos.getBehavior();
                        if (blockBehaviorAtPos instanceof BlockWaterBehavior || blockBehaviorAtPos instanceof BlockFlowingWaterBehavior) {
                            waterPosition = new BlockStateWithPos(blockStateAtPos, new Position3i(calculatePos, pos.dimension()), layer);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void changeHydratation(BlockStateWithPos current, Integer newHydratation) {
        if (newHydratation < MOISTURIZED_AMOUNT.getMin()) newHydratation = MOISTURIZED_AMOUNT.getMin();
        if (newHydratation > MOISTURIZED_AMOUNT.getMax()) newHydratation = MOISTURIZED_AMOUNT.getMax();

        //TODO: event
        updateBlockProperty(MOISTURIZED_AMOUNT, newHydratation, current.pos(), current.pos().dimension());
    }

    // TODO: died when player fall on farmland
    // TODO: died when piston put down farmland block
}
