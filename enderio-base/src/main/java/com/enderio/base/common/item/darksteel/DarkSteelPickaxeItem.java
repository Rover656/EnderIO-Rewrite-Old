package com.enderio.base.common.item.darksteel;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.darksteel.DarkSteelUpgradeable;
import com.enderio.base.common.item.EIOItems;
import com.enderio.base.common.item.darksteel.upgrades.DarkSteelUpgrades;
import com.enderio.base.common.item.darksteel.upgrades.EmpoweredUpgrade;
import com.enderio.base.common.item.darksteel.upgrades.EnergyDelegator;
import com.enderio.base.common.item.darksteel.upgrades.SpoonUpgrade;
import com.enderio.base.common.item.util.IEnergyBar;
import com.enderio.core.common.capability.IMultiCapabilityItem;
import com.enderio.core.common.capability.INamedNBTSerializable;
import com.enderio.core.common.capability.MultiCapabilityProvider;
import com.enderio.core.common.util.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

//TODO: Use dual duration / energy bar
public class DarkSteelPickaxeItem extends PickaxeItem implements IEnergyBar, IMultiCapabilityItem {

    //TODO: Config
    private int obsianBreakPowerUse = 50;

    //TODO: Config
    private int speedBoostWhenPowered = 2;

    //TODO: Config
    private int speedBoostWhenObsidian = 50;

    //TODO: Config
    private int powerUsePerDamagePoint = 750;

    public DarkSteelPickaxeItem(Properties pProperties) {
        super(EIOItems.DARK_STEEL_TIER, 1, -2.8F, pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return DarkSteelUpgrades.hasUpgrade(pStack, EmpoweredUpgrade.NAME);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //TODO: Right cick placement of blocks
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void setDamage(ItemStack stack, int newDamage) {
        //TODO: Damage Reduction based on energy upgrade
        int oldDamage = getDamage(stack);
        if (newDamage <= oldDamage) {
            super.setDamage(stack, newDamage);
        } else {
            int damage = newDamage - oldDamage;
            if (!absorbDamageWithEnergy(stack, damage * powerUsePerDamagePoint)) {
                super.setDamage(stack, newDamage);
            }
        }
    }

    private boolean absorbDamageWithEnergy(@Nonnull ItemStack stack, int amount) {
        //        EnergyUpgradeHolder eu = EnergyUpgradeManager.loadFromItem(stack);
        //        if (eu != null && eu.isAbsorbDamageWithPower() && eu.getEnergy() > 0) {
        //            eu.extractEnergy(amount, false);
        //            eu.writeToItem();
        //            return true;
        //        } else {
        //            return false;
        //        }
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        int energy = EnergyUtil.getEnergyStored(pStack);
        float result = 1.0f;
        if (BlockTags.MINEABLE_WITH_PICKAXE.contains(pState.getBlock()) || canSpoon(pStack, pState)) {
            result = speed;
        }
        if (energy > 0) {
            result += speedBoostWhenPowered;
        }
        if (energy >= obsianBreakPowerUse && pState.getBlock() == Blocks.OBSIDIAN) {
            //TODO: Check for blocks with hardness > 50 as well as obsidian
            result += speedBoostWhenObsidian;
        }
        return result;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        //TODO: Use energy for obsidian

        //TODO: Expolisive upgrade
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return super.isCorrectToolForDrops(stack, state) || (canSpoon(stack, state) && TierSortingRegistry.isCorrectTierForDrops(getTier(), state));
    }

    private boolean canSpoon(ItemStack stack, BlockState state) {
        return DarkSteelUpgrades.hasUpgrade(stack, SpoonUpgrade.NAME) && state.is(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.addSerialized(EIOCapabilities.DARK_STEEL_UPGRADABLE, LazyOptional.of(DarkSteelUpgradeable::new));
        provider.addSerialized("Energy", CapabilityEnergy.ENERGY, LazyOptional.of(() -> new EnergyDelegator(provider)));
        return provider;
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab pCategory, @Nonnull NonNullList<ItemStack> pItems) {
        if (allowdedIn(pCategory)) {
            ItemStack is = new ItemStack(this);
            pItems.add(is.copy());

            DarkSteelUpgrades.addUpgrade(is, SpoonUpgrade::new);
            DarkSteelUpgrades.addUpgrade(is, EmpoweredUpgrade.EMPOWERED_1);
            EnergyUtil.setFull(is);
            pItems.add(is.copy());

            DarkSteelUpgrades.addUpgrade(is, SpoonUpgrade::new);
            DarkSteelUpgrades.addUpgrade(is, EmpoweredUpgrade.EMPOWERED_2);
            EnergyUtil.setFull(is);
            pItems.add(is.copy());
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack pStack, @Nullable Level pLevel, @Nonnull List<Component> pTooltipComponents,
        @Nonnull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        if (DarkSteelUpgrades.hasUpgrade(pStack, EmpoweredUpgrade.NAME)) {
            String tip = EnergyUtil.getEnergyStored(pStack) + "/" + EnergyUtil.getMaxEnergyStored(pStack);
            pTooltipComponents.add(new TranslatableComponent(tip));
        }

        var upgrades = DarkSteelUpgrades.getUpgrades(pStack);
        upgrades
            .stream()
            .sorted(Comparator.comparing(INamedNBTSerializable::getSerializedName))
            .forEach(upgrade -> pTooltipComponents.add(new TranslatableComponent(upgrade.getDisplayName())));

    }
}
