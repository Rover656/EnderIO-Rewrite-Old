package com.enderio.base.painted;

import com.enderio.base.EIOBlocks;
import com.enderio.base.EnderIO;
import com.enderio.base.common.block.painted.SinglePaintedBlockEntity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void modelInit(final ModelRegistryEvent e) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(EnderIO.MODID, "painted_fence"), wrapperForModel(() -> new PaintedSimpleModel(Blocks.OAK_FENCE)));
        ModelLoaderRegistry.registerLoader(new ResourceLocation(EnderIO.MODID, "painted_fence_gate"), wrapperForModel(() -> new PaintedSimpleModel(Blocks.OAK_FENCE_GATE)));
        ModelLoaderRegistry.registerLoader(new ResourceLocation(EnderIO.MODID, "painted_sand"), wrapperForModel(() -> new PaintedSimpleModel(Blocks.SAND)));
    }

    @SubscribeEvent
    public static void colorInit(final ColorHandlerEvent.Block e) {
        e.getBlockColors().register(new PaintedBlockColor(), EIOBlocks.PAINTED_FENCE.get(), EIOBlocks.PAINTED_FENCE_GATE.get(), EIOBlocks.PAINTED_SAND.get());
    }

    @SubscribeEvent
    public static void init(FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(EIOBlocks.PAINTED_FENCE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(EIOBlocks.PAINTED_FENCE_GATE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(EIOBlocks.PAINTED_SAND.get(), RenderType.translucent());
        });
    }

    private static class PaintedBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
            if (level != null) {
                BlockEntity entity = level.getBlockEntity(pos);
                if (entity instanceof SinglePaintedBlockEntity paintedBlockEntity) {
                    Block paint = paintedBlockEntity.getPaint();
                    if (paint == null)
                        return 0;
                    BlockState paintState = paint.defaultBlockState();
                    return Minecraft.getInstance().getBlockColors().getColor(paintState, level, pos, tintIndex);
                }
            }
            return 0;
        }
    }

    private static IModelLoader<?> wrapperForModel(Supplier<IDynamicBakedModel> model) {
        return new WrappedModelLoader(model);
    }
    private static class WrappedModelLoader implements IModelLoader<Geometry> {

        private final Supplier<IDynamicBakedModel> modelFactory;

        private WrappedModelLoader(Supplier<IDynamicBakedModel> modelFactory) {
            this.modelFactory = modelFactory;
        }

        @Override
        public Geometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
            return new Geometry(modelFactory);
        }

        @Override
        public void onResourceManagerReload(ResourceManager pResourceManager) {}

        @Nullable
        @Override
        public IResourceType getResourceType() {
            return VanillaResourceType.MODELS;
        }
    }
    private static class Geometry implements IModelGeometry<Geometry> {

        final Supplier<IDynamicBakedModel> modelFactory;

        private Geometry(Supplier<IDynamicBakedModel> modelFactory) {
            this.modelFactory = modelFactory;
        }

        @Override
        public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
            return modelFactory.get();
        }

        @Override
        public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            return Collections.singletonList(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("minecraft", "missingno")));
        }
    }
}
