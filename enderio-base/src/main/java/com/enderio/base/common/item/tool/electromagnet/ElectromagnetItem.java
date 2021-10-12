package com.enderio.base.common.item.tool.electromagnet;

import com.enderio.base.common.item.util.IEnergyBar;
import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.toggled.IToggled;
import com.enderio.base.common.capability.toggled.Toggled;
import com.enderio.core.common.capability.MultiCapabilityProvider;
import com.enderio.core.common.capability.IMultiCapability;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nullable;

// TODO: Behaviours
public class ElectromagnetItem extends Item implements IEnergyBar, IMultiCapability {
    public ElectromagnetItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.getCapability(EIOCapabilities.TOGGLED).map(IToggled::isEnabled).orElse(false);
    }

    @Override
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if (allowdedIn(pCategory)) {
            ItemStack is = new ItemStack(this);
            pItems.add(is.copy());

            // TODO: EnergyUtils for this, its a mess...
            is.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> energyStorage.receiveEnergy(energyStorage.getMaxEnergyStored(), false));
            pItems.add(is);
        }
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.add(EIOCapabilities.TOGGLED, LazyOptional.of(Toggled::new));
        provider.add(CapabilityEnergy.ENERGY, LazyOptional.of(() -> new EnergyStorage(1000)));
        return provider;
    }
}