package com.enderio.base.common.item.darksteel;

import com.enderio.base.common.capability.darksteel.DarkSteelUpgradeable;
import com.enderio.base.common.item.EIOItems;
import com.enderio.base.common.item.darksteel.upgrades.EmpoweredUpgrade;
import com.enderio.base.common.item.darksteel.upgrades.ForkUpgrade;
import com.enderio.core.common.util.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.UP;

public class DarkSteelAxeItem extends AxeItem implements IDarkSteelItem {

    public DarkSteelAxeItem(Properties pProperties) {
        super(EIOItems.DARK_STEEL_TIER, 5, -3, pProperties);
    }

    @Override
    public void setDamage(final ItemStack stack, final int newDamage) {
        int finalDamage = getEmpoweredUpgrade(stack).map(empoweredUpgrade -> empoweredUpgrade.adjustDamage(getDamage(stack), newDamage)).orElse(newDamage);
        super.setDamage(stack, finalDamage);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        final float baseSpeed = canHarvest(pStack, pState) ? speed : 1.0f;
        return getEmpoweredUpgrade(pStack).map(empoweredUpgrade -> empoweredUpgrade.adjustDestroySpeed(baseSpeed, pState)).orElse(baseSpeed);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if(pEntityLiving instanceof Player player) {
            if(pEntityLiving.isCrouching() && pState.is(BlockTags.LOGS) && EnergyUtil.getEnergyStored(pStack) > 0) {

                Set<BlockPos> chopCandidates = new HashSet<>();
                fellTree(pLevel, pPos, new HashSet<>(), chopCandidates, 400, pState.getBlock());
                chopCandidates.remove(pPos); // dont double harvest this guy

                int energyPerBlock = 1500; //TODO: Config "powerUsePerDamagePointMultiHarvest", 1500
                int maxBlocks = EnergyUtil.getEnergyStored(pStack)/energyPerBlock;

                Collection<BlockPos> toChop = chopCandidates;
                if(maxBlocks < chopCandidates.size()) {
                    //If not enough power to get them all cut top to bottom to avoid floating logs
                    List<BlockPos> orderedChopList = new ArrayList<>(chopCandidates);
                    orderedChopList.sort((o1, o2) -> Integer.compare(o2.getY(), o1.getY()));
                    toChop = orderedChopList;
                }

                int chopCount = 0;
                int energyUse = 0;
                for(BlockPos chopPos : toChop) {
                    if (removeBlock(pLevel, player, pStack, chopPos)) {
                        energyUse += energyPerBlock;
                        chopCount++;
                        if(chopCount >= maxBlocks) {
                            break;
                        }
                    }
                }
                if (energyUse  > 0) {
                    EnergyUtil.extractEnergy(pStack, energyUse, false);
                }
            }
        }
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return canHarvest(stack, state) && TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(hasFork(pContext.getItemInHand())) {
            return Items.DIAMOND_HOE.useOn(pContext);
        }
        return super.useOn(pContext);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return super.canPerformAction(stack,toolAction) || (hasFork(stack) && ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction));
    }

    private boolean canHarvest(ItemStack stack, BlockState state) {
        return BlockTags.MINEABLE_WITH_AXE.contains(state.getBlock()) || (state.is(BlockTags.MINEABLE_WITH_HOE) && hasFork(stack));
    }

    private boolean hasFork(ItemStack stack) {
        return DarkSteelUpgradeable.hasUpgrade(stack, ForkUpgrade.NAME);
    }

    private void fellTree(Level level, BlockPos pos, Set<BlockPos> checkedPos, Set<BlockPos> toChop, int maxBlocks, Block targetBock) {
        if(toChop.size() >= maxBlocks || checkedPos.contains(pos)) {
            return;
        }
        checkedPos.add(pos);
        BlockState checkState = level.getBlockState(pos);
        if (checkState.is(targetBock)) {
            toChop.add(pos);

            Set<BlockPos> toCheck = new HashSet<>();
            surrounding(toCheck, pos);
            surrounding(toCheck, pos.above());
            toCheck.add(pos.above());
            for(BlockPos newPos : toCheck) {
                fellTree(level, newPos, checkedPos, toChop, maxBlocks, targetBock);
            }
        }
    }

    private void surrounding(Set<BlockPos> res, BlockPos pos) {
        for(Direction dir : Direction.values()) {
            if(dir != DOWN && dir != UP) {
                res.add(pos.relative(dir));
            }
        }
        res.add(pos.north().east());
        res.add(pos.north().west());
        res.add(pos.south().east());
        res.add(pos.south().west());
    }

    private boolean removeBlock(Level level, Player player, ItemStack tool, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        boolean removed = state.removedByPlayer(level, pos, player, true, level.getFluidState(pos));
        if (removed) {
            state.getBlock().destroy(level, pos, state);
            state.getBlock().playerDestroy(level, player, pos, state, null, tool);
        }
        return removed;
    }

    // region Common for all tools

    @Override
    public boolean isFoil(ItemStack pStack) {
        return DarkSteelUpgradeable.hasUpgrade(pStack, EmpoweredUpgrade.NAME);
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab pCategory, @Nonnull NonNullList<ItemStack> pItems) {
        if (allowdedIn(pCategory)) {
            addCreativeItems(pItems, this);
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack pStack, @Nullable Level pLevel, @Nonnull List<Component> pTooltipComponents,
        @Nonnull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        addUpgradeHoverTest(pStack, pTooltipComponents);
    }

    // endregion
}
