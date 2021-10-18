package com.enderio.base.common.capability;

import com.enderio.base.EnderIO;
import com.enderio.base.common.capability.capacitors.ICapacitorData;
import com.enderio.base.common.capability.entity.IEntityStorage;
import com.enderio.base.common.capability.owner.IOwner;
import com.enderio.base.common.capability.toggled.IToggled;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnderIO.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EIOCapabilities {
    @CapabilityInject(IEntityStorage.class)
    public static Capability<IEntityStorage> ENTITY_STORAGE;

    @CapabilityInject(IToggled.class)
    public static Capability<IToggled> TOGGLED;

    @CapabilityInject(IOwner.class)
    public static Capability<IOwner> OWNER;
    
    @CapabilityInject(ICapacitorData.class)
    public static Capability<ICapacitorData> CAPACITOR;

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IEntityStorage.class);
        event.register(IToggled.class);
        event.register(IOwner.class);
        event.register(ICapacitorData.class);
    }
}
