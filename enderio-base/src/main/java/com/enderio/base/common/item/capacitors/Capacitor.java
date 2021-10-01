package com.enderio.base.common.item.capacitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.capacitors.CapacitorData;
import com.enderio.base.common.capability.capacitors.ICapacitorData;
import com.enderio.core.common.capability.IMultiCapability;
import com.enderio.core.common.capability.MultiCapabilityProvider;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public class Capacitor extends Item implements IMultiCapability{
    private CapacitorData data = new CapacitorData();
    public static Map<String, Float> BASIC = new HashMap<>();
    public static Map<String, Float> DOUBLE = new HashMap<>();
    public static Map<String, Float> OCTADIC = new HashMap<>();
    static {
        BASIC.put(ICapacitorData.ALL_ENERGY_CONSUMPSTION, 1.0F);
        BASIC.put(ICapacitorData.ALL_PRODUCTION_SPEED, 1.0F);
        DOUBLE.put(ICapacitorData.ALL_ENERGY_CONSUMPSTION, 2.0F);
        DOUBLE.put(ICapacitorData.ALL_PRODUCTION_SPEED, 2.0F);
        OCTADIC.put(ICapacitorData.ALL_ENERGY_CONSUMPSTION, 4.0F);
        OCTADIC.put(ICapacitorData.ALL_PRODUCTION_SPEED, 4.0F);
    }
    
    public Capacitor(Properties pProperties, Map<String, Float> specialization) {
        super(pProperties);
        this.data.addAllSpecialization(specialization);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
            TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TranslatableComponent("Stats:"));
        pStack.getCapability(EIOCapabilities.CAPACITOR).ifPresent(cap ->{
            pTooltipComponents.add(new TranslatableComponent(cap.getBase() + ""));
        });
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.add(EIOCapabilities.CAPACITOR, LazyOptional.of(() -> data));
        return provider;
    }

}
