package com.enderio.base.common.item.darksteel;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.darksteel.DarkSteelUpgradeable;
import com.enderio.base.common.capability.darksteel.IDarkSteelUpgrade;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

//TODO: Use dual duration / energy bar
public class DarkSteelPickaxeItem extends PickaxeItem implements IEnergyBar, IMultiCapabilityItem {

    public static final String UPGRADE_SET_NAME = "DarkSteelPickaxeItem";

    public DarkSteelPickaxeItem(Properties pProperties) {
        super(EIOItems.DARK_STEEL_TIER, 1, -2.8F, pProperties);
        DarkSteelUpgrades
            .instance()
            .addUpgradesToSet(new DarkSteelUpgrades.UpgradeSet(UPGRADE_SET_NAME).addUpgrades(EmpoweredUpgrade.NAME, SpoonUpgrade.NAME));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //TODO: Right cick placement of blocks
        return super.use(pLevel, pPlayer, pUsedHand);
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
        getEmpoweredUpgrade(pStack).ifPresent(empoweredUpgrade -> empoweredUpgrade.onMineBlock(pState));
        //TODO: Expolisive upgrade
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return canHarvest(stack, state) && TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

    private boolean canHarvest(ItemStack stack, BlockState state) {
        return BlockTags.MINEABLE_WITH_PICKAXE.contains(state.getBlock()) || (state.is(BlockTags.MINEABLE_WITH_SHOVEL) && DarkSteelUpgradeable.hasUpgrade(stack,
            SpoonUpgrade.NAME));
    }

    private static Optional<EmpoweredUpgrade> getEmpoweredUpgrade(ItemStack stack) {
        return DarkSteelUpgradeable.getUpgradeAs(stack, EmpoweredUpgrade.NAME);
    }

    //------------ Common for all tools

    @Override
    public boolean isFoil(ItemStack pStack) {
        return DarkSteelUpgradeable.hasUpgrade(pStack, EmpoweredUpgrade.NAME);
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.addSerialized(EIOCapabilities.DARK_STEEL_UPGRADABLE, LazyOptional.of(() -> new DarkSteelUpgradeable(UPGRADE_SET_NAME)));
        provider.addSerialized("Energy", CapabilityEnergy.ENERGY, LazyOptional.of(() -> new EnergyDelegator(provider)));
        return provider;
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab pCategory, @Nonnull NonNullList<ItemStack> pItems) {
        if (allowdedIn(pCategory)) {
            ItemStack is = new ItemStack(this);
            pItems.add(is.copy());

            //Just empowered
            is = new ItemStack(this);
            DarkSteelUpgradeable.addUpgrade(is,EmpoweredUpgrade.getUpgradeForTier(0).get());
            pItems.add(is.copy());

            //All the upgrades
            is = new ItemStack(this);
            Collection<? extends IDarkSteelUpgrade> ups = DarkSteelUpgradeable.getAllPossibleUpgrades(is);
            for(IDarkSteelUpgrade upgrade : ups) {
                IDarkSteelUpgrade maxTier = upgrade;
                Optional<? extends IDarkSteelUpgrade> nextTier = maxTier.getNextTier();
                while(nextTier.isPresent()) {
                    maxTier = nextTier.get();
                    nextTier = maxTier.getNextTier();
                }
                DarkSteelUpgradeable.addUpgrade(is,maxTier);
            }
            EnergyUtil.setFull(is);
            pItems.add(is.copy());
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack pStack, @Nullable Level pLevel, @Nonnull List<Component> pTooltipComponents,
        @Nonnull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        if (DarkSteelUpgradeable.hasUpgrade(pStack, EmpoweredUpgrade.NAME)) {
            String tip = EnergyUtil.getEnergyStored(pStack) + "/" + EnergyUtil.getMaxEnergyStored(pStack);
            pTooltipComponents.add(new TranslatableComponent(tip));
        }

        var upgrades = DarkSteelUpgradeable.getUpgrades(pStack);
        upgrades
            .stream()
            .sorted(Comparator.comparing(INamedNBTSerializable::getSerializedName))
            .forEach(upgrade -> pTooltipComponents.add(new TranslatableComponent(upgrade.getDisplayName())));



        var availUpgrades = DarkSteelUpgradeable.getUpgradesThatCanBeAppliedAtTheMoment(pStack);
        if(!availUpgrades.isEmpty()) {
            pTooltipComponents.add(new TranslatableComponent("Available Upgrades:"));
            availUpgrades
                .stream()
                .sorted(Comparator.comparing(INamedNBTSerializable::getSerializedName))
                .forEach(upgrade -> pTooltipComponents.add(new TranslatableComponent(upgrade.getDisplayName())));
        }


    }
}
