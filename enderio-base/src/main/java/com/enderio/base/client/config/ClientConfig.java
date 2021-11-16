package com.enderio.base.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public final ForgeConfigSpec.ConfigValue<Boolean> MACHINE_PARTICLES;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("visual");
        MACHINE_PARTICLES = builder.comment("Enable machine particles").define("machineParticles", true);
        builder.pop();
    }
}
