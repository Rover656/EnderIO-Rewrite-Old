package com.enderio.base.painted;

import com.enderio.base.EIOBlocks;
import com.enderio.base.EnderIO;
import com.enderio.base.common.block.PaintedFenceBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void modelInit(final ModelRegistryEvent setupEvent) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(EnderIO.MODID, "painted_fence"), new PaintedBakedModel.PaintedFenceModelLoader());
    }

    @SubscribeEvent
    public static void colorInit(final ColorHandlerEvent.Block e) {
        e.getBlockColors().register(new PaintedBlockColor(), EIOBlocks.PAINTED_FENCE.get());
    }

    private static class PaintedBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
            if (level != null) {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity instanceof PaintedFenceBlockEntity paintedFence) {
                    BlockState paintState = paintedFence.getPaint().defaultBlockState();
                    return Minecraft.getInstance().getBlockColors().getColor(paintState, level, pos, tintIndex);
                }
            }
            return 0;
        }
    }
}
