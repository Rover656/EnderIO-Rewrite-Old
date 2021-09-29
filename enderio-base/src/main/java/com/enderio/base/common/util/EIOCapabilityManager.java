package com.enderio.base.common.util;

import javax.annotation.Nonnull;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class EIOCapabilityManager {
    
    @CapabilityInject(IGraveCap.class)
    @Nonnull
    public static Capability<IGraveCap> EIO_GRAVE_CAP;
    
    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IGraveCap.class);
        System.out.println("reg");
    }

}
