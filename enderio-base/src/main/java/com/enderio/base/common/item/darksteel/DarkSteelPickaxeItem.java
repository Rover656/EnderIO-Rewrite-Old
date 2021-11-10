package com.enderio.base.common.item.darksteel;

import com.enderio.base.common.capability.darksteel.DarkSteelUpgradeable;
import com.enderio.base.common.item.EIOItems;
import com.enderio.base.common.item.darksteel.upgrades.DarkSteelUpgradeRegistry;
import com.enderio.base.common.item.darksteel.upgrades.EmpoweredUpgrade;
import com.enderio.base.common.item.darksteel.upgrades.SpoonUpgrade;
import com.enderio.core.common.capability.MultiCapabilityProvider;
import com.enderio.core.common.util.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

//TODO: Use dual duration / energy bar
public class DarkSteelPickaxeItem extends PickaxeItem implements IDarkSteelItem {

    public static final String UPGRADE_SET_NAME = "DarkSteelPickaxeUpgrades";

    //TODO: Config
    private int obsianBreakPowerUse = 50;

    //TODO: Config
    private int speedBoostWhenObsidian = 50;

    public DarkSteelPickaxeItem(Properties pProperties) {
        super(EIOItems.DARK_STEEL_TIER, 1, -2.8F, pProperties);
        DarkSteelUpgradeRegistry.instance().addUpgradesToSet(UPGRADE_SET_NAME, EmpoweredUpgrade.NAME, SpoonUpgrade.NAME);
    }

    @Override
    public void setDamage(final ItemStack stack, final int newDamage) {
        int finalDamage = getEmpoweredUpgrade(stack).map(empoweredUpgrade -> empoweredUpgrade.adjustDamage(getDamage(stack), newDamage)).orElse(newDamage);
        super.setDamage(stack, finalDamage);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        final float baseSpeed = canHarvest(pStack, pState) ? speed : 1.0f;
        float adjustedSpeed = getEmpoweredUpgrade(pStack).map(empoweredUpgrade -> empoweredUpgrade.adjustDestroySpeed(baseSpeed, pState)).orElse(baseSpeed);
        if (useObsidianMining(pState, pStack)) {
            adjustedSpeed += speedBoostWhenObsidian;
        }
        return adjustedSpeed;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (useObsidianMining(pState, pStack)) {
            EnergyUtil.extractEnergy(pStack, obsianBreakPowerUse, false);
        }
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return canHarvest(stack, state) && TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(hasSpoon(pContext.getItemInHand())) {
            return Items.DIAMOND_SHOVEL.useOn(pContext);
        }
        return super.useOn(pContext);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return super.canPerformAction(stack,toolAction) || (hasSpoon(stack) && ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction));
    }

    private boolean canHarvest(ItemStack stack, BlockState state) {
        return BlockTags.MINEABLE_WITH_PICKAXE.contains(state.getBlock()) || (state.is(BlockTags.MINEABLE_WITH_SHOVEL) && hasSpoon(stack));
    }

    private boolean hasSpoon(ItemStack stack) {
        return DarkSteelUpgradeable.hasUpgrade(stack, SpoonUpgrade.NAME);
    }

    private boolean useObsidianMining(BlockState pState, ItemStack stack) {
        //TODO: Check for blocks with hardness > 50 as well as obsidian
        return EnergyUtil.getEnergyStored(stack) >= obsianBreakPowerUse && pState.getBlock() == Blocks.OBSIDIAN;
    }


    //------------ Common for all tools

    @Override
    public boolean isFoil(ItemStack pStack) {
        return DarkSteelUpgradeable.hasUpgrade(pStack, EmpoweredUpgrade.NAME);
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider) {
        return initDarkSteelCapabilities(provider, UPGRADE_SET_NAME);
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


}
