package com.enderio.machines;

import com.enderio.base.data.recipe.standard.StandardRecipes;
import com.enderio.machines.common.EIOMachinesBlockEntities;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import javax.annotation.Nonnull;

@Mod(EnderIOMachines.MODID)
@Mod.EventBusSubscriber()
public class EnderIOMachines {
    public static final @Nonnull String MODID = "enderio_machines";
    public static final @Nonnull String DOMAIN = "enderio";

    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<>(() -> Registrate.create(DOMAIN));

    public EnderIOMachines() {
        EIOMachinesItems.register();
        EIOMachinesBlocks.register();
        EIOMachinesBlockEntities.register();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Run datagen after registrate is finished.
        modEventBus.addListener(EventPriority.LOWEST, this::gatherData);
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent e) {
        if (e.getEntity() instanceof ServerPlayer player) {
            ItemStack painted = new ItemStack(EIOMachinesBlocks.PAINTED_FENCE.get(), 64);
            painted.getOrCreateTagElement("BlockEntityTag").putString("paint", "minecraft:grass_block");
            player.addItem(painted);
        }
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
