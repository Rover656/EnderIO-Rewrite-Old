package com.enderio.machines;

import com.enderio.base.EIOBlocks;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

@Mod(EnderIOMachines.MODID)
@Mod.EventBusSubscriber()
public class EnderIOMachines {
    public static final @Nonnull String MODID = "enderio_machines";
    public static final @Nonnull String DOMAIN = "enderio";

    private static List<Block> blocks;

    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<>(() -> Registrate.create(DOMAIN));

    public EnderIOMachines() {
        EIOMachinesItems.register();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent e) {
        if (e.getEntity() instanceof ServerPlayer player) {
            Block paint = getRandomBlock();
            for (Block block : EIOBlocks.getPainted()) {
                ItemStack painted = new ItemStack(block, 64);
                painted.getOrCreateTagElement("BlockEntityTag").putString("paint", paint.getRegistryName().toString());
                player.addItem(painted);
            }
            for (Block block : EIOBlocks.getDoublePainted()) {
                ItemStack painted = new ItemStack(block, 64);
                painted.getOrCreateTagElement("BlockEntityTag").putString("paint", paint.getRegistryName().toString());
                player.addItem(painted);
            }
        }
    }

    private static Block getRandomBlock() {
        if (blocks == null)
            blocks = ForgeRegistries.BLOCKS.getValues().stream().filter(block -> block.defaultBlockState().canOcclude()).filter(block -> {
                try {
                    return Block.isShapeFullBlock(block.getShape(block.defaultBlockState(), null, null, CollisionContext.empty()));
                } catch (Exception e) {
                    return false;
                }
            }).toList();
        return blocks.get(new Random().nextInt(blocks.size()));
    }

    public static Registrate registrate() {
        return REGISTRATE.get();
    }
}
