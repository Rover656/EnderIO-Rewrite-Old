package com.enderio.base.mixin.client;

import com.enderio.base.common.item.util.IEnergyBar;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.energy.CapabilityEnergy;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IEnergyBar.class)
public interface EnergyBarMixin extends IForgeItem {
    @Override
    default boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    default double getDurabilityForDisplay(ItemStack stack) {
        return stack
            .getCapability(CapabilityEnergy.ENERGY)
            .map(energyStorage -> 1.0d - (double) energyStorage.getEnergyStored() / (double) energyStorage.getMaxEnergyStored())
            .orElse(0d);
    }

    @Override
    default int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0x00B168E4; // A nice purple
    }
}
