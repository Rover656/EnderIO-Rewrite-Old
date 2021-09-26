package com.enderio.base.painted;

import com.enderio.base.common.block.PaintedFenceBlockEntity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.client.model.pipeline.LightUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class PaintedBakedModel implements IDynamicBakedModel {

    BakedModel getModel(BlockState paint) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(paint);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> shape = getFenceModel(state).getQuads(fenceState(state), side, rand);

        Block paint = extraData.getData(PaintedFenceBlockEntity.PAINT);
        if (paint != null) {
            BakedModel model = getModel(paint.defaultBlockState());
            BakedQuad textureQuad = null;
            for (BakedQuad quad : model.getQuads(paint.defaultBlockState(), side, rand, EmptyModelData.INSTANCE)) {
                textureQuad = quad;
                break;
            }
            List<BakedQuad> returnQuads = new ArrayList<>();
            for (BakedQuad shapeQuad : shape) {
                TextureAtlasSprite sprite = textureQuad != null ? textureQuad.getSprite() : getSpriteFromModel(shapeQuad, model, paint);
                boolean shouldBeTinted = textureQuad != null ? textureQuad.isTinted() : getTintFromModel(shapeQuad, model, paint);
                returnQuads.add(copyQuad(shapeQuad, sprite, shouldBeTinted));
            }
            return returnQuads;
        }
        return List.of();
    }

    private TextureAtlasSprite getSpriteFromModel(BakedQuad shape, BakedModel model, Block paint) {
        float[] normalData = new float[4];
        LightUtil.unpack(shape.getVertices() , normalData,DefaultVertexFormat.BLOCK, 0, 4);
        Direction normal = Direction.getNearest(normalData[0], normalData[1], normalData[2]);
        List<BakedQuad> quads = model.getQuads(paint.defaultBlockState(), normal, new Random());
        return quads.isEmpty() ? getMissingTexture() : quads.get(0).getSprite();
    }
    private boolean getTintFromModel(BakedQuad shape, BakedModel model, Block paint) {
        float[] normalData = new float[4];
        LightUtil.unpack(shape.getVertices() , normalData,DefaultVertexFormat.BLOCK, 0, 4);
        Direction normal = Direction.getNearest(normalData[0], normalData[1], normalData[2]);
        List<BakedQuad> quads = model.getQuads(paint.defaultBlockState(), normal, new Random());
        return !quads.isEmpty() && quads.get(0).isTinted();
    }

    private BakedQuad copyQuad(BakedQuad toCopy, TextureAtlasSprite sprite, boolean shouldTint) {
        BakedQuad copied = new BakedQuad(Arrays.copyOf(toCopy.getVertices(), 32), shouldTint ? 1 : -1, toCopy.getDirection(), sprite, toCopy.isShade());

        for (int i = 0; i < 4; i++) {
            float[] textureData = new float[2]; // uv data pair
            LightUtil.unpack(copied.getVertices(), textureData, DefaultVertexFormat.BLOCK, i, 2);
            textureData[0] = (textureData[0] - toCopy.getSprite().getU0()) * sprite.getWidth() / toCopy.getSprite().getWidth() + sprite.getU0();
            textureData[1] = (textureData[1] - toCopy.getSprite().getV0()) * sprite.getHeight() / toCopy.getSprite().getHeight() + sprite.getV0();
            int[] packedTextureData = new int[8];
            LightUtil.pack(textureData, packedTextureData, DefaultVertexFormat.BLOCK, 0,0);
            //put uv data back
            copied.getVertices()[4 + i*8] = packedTextureData[0];
            copied.getVertices()[5 + i*8] = packedTextureData[1];
        }
        return copied;
    }

    private BlockState fenceState(@Nullable BlockState paintedFenceState) {
        BlockState oakFence = Blocks.OAK_FENCE.defaultBlockState();
        if (paintedFenceState == null)
            return oakFence;
        for (Property<?> property : paintedFenceState.getProperties()) {
            if (property instanceof BooleanProperty booleanProperty && oakFence.hasProperty(property)) {
                oakFence = oakFence.setValue(booleanProperty, paintedFenceState.getValue(booleanProperty));
            }
        }
        return oakFence;
    }

    private BakedModel getFenceModel(BlockState state) {
        return getModel(fenceState(state));
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@Nonnull IModelData data) {
        Block paint = data.getData(PaintedFenceBlockEntity.PAINT);
        if (paint != null) {
            BakedModel model = getModel(paint.defaultBlockState());
            for (BakedQuad quad : model.getQuads(paint.defaultBlockState(), Direction.UP, new Random(), EmptyModelData.INSTANCE)) {
                return quad.getSprite();
            }
        }
        return getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return getMissingTexture();
    }

    public TextureAtlasSprite getMissingTexture() {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "missingno"));
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
    public static class PaintedFenceModelLoader implements IModelLoader<PaintedFenceGeometry> {
        @Override
        public PaintedFenceGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
            return new PaintedFenceGeometry();
        }

        @Override
        public void onResourceManagerReload(ResourceManager pResourceManager) {

        }
    }

    private static class PaintedFenceGeometry implements IModelGeometry<PaintedFenceGeometry> {

        @Override
        public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform,
            ItemOverrides overrides, ResourceLocation modelLocation) {
            return new PaintedBakedModel();
        }

        @Override
        public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            return Collections.singletonList(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("minecraft", "missingno")));
        }
    }
}
