package com.enderio.base.common.item.capacitors;

import javax.annotation.Nullable;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.capacitors.CapacitorData;
import com.enderio.base.common.capability.capacitors.MachinePropertie;
import com.enderio.core.common.capability.IMultiCapability;
import com.enderio.core.common.capability.MultiCapabilityProvider;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.util.LazyOptional;

public class Capacitor extends Item implements IMultiCapability{
    private CapacitorData data = new CapacitorData();
    
    public Capacitor(Properties pProperties, MachinePropertie machinePropertie) {
        super(pProperties);
        this.data.update(machinePropertie);
    }
    
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        pContext.getItemInHand().getCapability(EIOCapabilities.CAPACITOR).ifPresent(cap -> {
            System.out.println(cap.getMachinePropertie().getBase());
        });
        return super.useOn(pContext);
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.add(EIOCapabilities.CAPACITOR, LazyOptional.of(() -> data));
        return provider;
    }

}
