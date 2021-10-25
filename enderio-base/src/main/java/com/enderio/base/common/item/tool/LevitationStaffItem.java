package com.enderio.base.common.item.tool;

import com.enderio.base.common.item.util.IEnergyBar;
import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.toggled.IToggled;
import com.enderio.base.common.capability.toggled.Toggled;
import com.enderio.core.common.capability.MultiCapabilityProvider;
import com.enderio.core.common.capability.IMultiCapabilityItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LevitationStaffItem extends Item implements IEnergyBar, IMultiCapabilityItem {
    public LevitationStaffItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.getCapability(EIOCapabilities.TOGGLED).map(IToggled::isEnabled).orElse(false);
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab pCategory, @Nonnull NonNullList<ItemStack> pItems) {
        if (allowdedIn(pCategory)) {
            ItemStack is = new ItemStack(this);
            pItems.add(is.copy());

            // TODO: EnergyUtils for this, its a mess...
            is.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> energyStorage.receiveEnergy(energyStorage.getMaxEnergyStored(), false));
            pItems.add(is);
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level pLevel, Player pPlayer, @Nonnull InteractionHand pUsedHand) {
        if (pPlayer.isCrouching()) {
            // TODO: Check we have enough energy and fluid.
            toggleEnabled(pPlayer.getItemInHand(pUsedHand));
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack pStack, @Nonnull Level pLevel, @Nonnull Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            pStack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                if (isEnabled(pStack)) {
                    if (energyStorage.extractEnergy(1, true) > 0) {
                        energyStorage.extractEnergy(1, false);
                        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 1)); // TODO: An upgrade to make it faster?
                    } else {
                        toggleEnabled(pStack);
                    }
                }
            });
        }
    }

    private boolean isEnabled(@Nonnull ItemStack itemStack) {
        return itemStack.getCapability(EIOCapabilities.TOGGLED).map(IToggled::isEnabled).orElse(false);
    }

    private void toggleEnabled(ItemStack itemStack) {
        itemStack.getCapability(EIOCapabilities.TOGGLED).ifPresent(IToggled::toggle);
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.addSerialized(EIOCapabilities.TOGGLED, LazyOptional.of(Toggled::new));
        provider.addSerialized("Energy", CapabilityEnergy.ENERGY, LazyOptional.of(() -> new EnergyStorage(1000)));
        return provider;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged)
            return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
        return oldStack.getItem() != newStack.getItem() || isEnabled(oldStack) != isEnabled(newStack);
    }
}
