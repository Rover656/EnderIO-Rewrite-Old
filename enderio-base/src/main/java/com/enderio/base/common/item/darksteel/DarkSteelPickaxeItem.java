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

    public DarkSteelPickaxeItem(Properties pProperties) {
        super(EIOItems.DARK_STEEL_TIER, 1, -2.8F, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //TODO: Right cick placement of blocks
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void setDamage(final ItemStack stack, final int newDamage) {
        int finalDamage = DarkSteelUpgradeable
            .getUpgrade(stack, EmpoweredUpgrade.class)
            .map(empoweredUpgrade -> empoweredUpgrade.adjustDamage(getDamage(stack), newDamage))
            .orElse(newDamage);
         super.setDamage(stack, finalDamage);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        final float baseSpeed = canHarvest(pStack, pState) ? speed : 1.0f;
        return DarkSteelUpgradeable
            .getUpgrade(pStack, EmpoweredUpgrade.class)
            .map(empoweredUpgrade -> empoweredUpgrade.adjustDestroySpeed(baseSpeed, pState))
            .orElse(baseSpeed);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        DarkSteelUpgradeable
            .getUpgrade(pStack, EmpoweredUpgrade.class).ifPresent(empoweredUpgrade -> empoweredUpgrade.onMineBlock(pState));

        //TODO: Expolisive upgrade
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return canHarvest(stack, state) && TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

    private boolean canHarvest(ItemStack stack, BlockState state) {
        return BlockTags.MINEABLE_WITH_PICKAXE.contains(state.getBlock()) || (state.is(BlockTags.MINEABLE_WITH_SHOVEL) && DarkSteelUpgradeable.hasUpgrade(stack, SpoonUpgrade.NAME));
    }


    //------------ Common for all tools

    @Override
    public boolean isFoil(ItemStack pStack) {
        return DarkSteelUpgradeable.hasUpgrade(pStack, EmpoweredUpgrade.NAME);
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

            DarkSteelUpgradeable.addUpgrade(is, SpoonUpgrade::new);
            DarkSteelUpgradeable.addUpgrade(is, EmpoweredUpgrade.EMPOWERED_1);
            EnergyUtil.setFull(is);
            pItems.add(is.copy());

            DarkSteelUpgradeable.addUpgrade(is, SpoonUpgrade::new);
            DarkSteelUpgradeable.addUpgrade(is, EmpoweredUpgrade.EMPOWERED_2);
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

    }
}
