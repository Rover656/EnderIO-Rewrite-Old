package com.enderio.base.common.capability;

import com.enderio.base.EnderIO;
import com.enderio.base.common.capability.entitycapture.IEntityStorage;
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
    public static Capability<IToggled> TOGGLED_ITEM;

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IEntityStorage.class);
        event.register(IToggled.class);
    }
}
