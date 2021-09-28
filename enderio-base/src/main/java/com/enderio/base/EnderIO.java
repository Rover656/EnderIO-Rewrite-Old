package com.enderio.base;

import com.enderio.base.data.recipe.standard.StandardRecipes;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import javax.annotation.Nonnull;

@Mod(EnderIO.MODID)
public class EnderIO {
    public static final @Nonnull String MODID = "enderio";
    public static final @Nonnull String DOMAIN = "enderio";

    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<>(() -> Registrate.create(DOMAIN));

    public EnderIO() {
        EIOItems.register();
        EIOBlocks.register();
        EIOBlockEntities.register();
        EIOEntities.register();

        IEventBus modEventBus = FMLJavaModLoadingContext
            .get()
            .getModEventBus();

        // Run datagen after registrate is finished.
        modEventBus.addListener(EventPriority.LOWEST, this::gatherData);
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            StandardRecipes.generate(generator);
        }
    }

    public static Registrate registrate() {
        return REGISTRATE.get();
    }
}
