package com.enderio.base.painted;

import com.enderio.base.common.block.painted.SinglePaintedBlockEntity;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class PaintedModel implements IDynamicBakedModel {

    protected abstract Block copyModelFromBlock();

    protected BakedModel getModel(BlockState paint) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(paint);
    }

    protected BakedModel getModelFromOwn(BlockState ownBlockState) {
        return getModel(copyBlockState(ownBlockState));
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> shape = getModelFromOwn(state).getQuads(copyBlockState(state), side, rand);

        Block paint = extraData.getData(SinglePaintedBlockEntity.PAINT);
        if (paint != null) {
            BakedModel model = getModel(paint.defaultBlockState());
            Optional<Pair<TextureAtlasSprite, Boolean>> spriteOptional = getSpriteData(paint, side, rand);
            List<BakedQuad> returnQuads = new ArrayList<>();
            for (BakedQuad shapeQuad : shape) {
                Pair<TextureAtlasSprite, Boolean> spriteData = spriteOptional.orElseGet(() -> getSpriteFromModel(shapeQuad, model, paint));
                returnQuads.add(copyQuad(shapeQuad, spriteData.getLeft(), spriteData.getRight()));
            }
            return returnQuads;
        }
        return List.of();
    }
    Optional<Pair<TextureAtlasSprite, Boolean>> getSpriteData(Block paint, Direction side, Random rand) {
        for (BakedQuad quad : getModel(paint.defaultBlockState()).getQuads(paint.defaultBlockState(), side, rand, EmptyModelData.INSTANCE)) {
            return Optional.of(Pair.of(quad.getSprite(), quad.isTinted()));
        }
        return Optional.empty();
    }

    protected BlockState copyBlockState(BlockState ownBlockState) {
        BlockState toState = copyModelFromBlock().defaultBlockState();
        if (ownBlockState == null)
            return toState;
        for (Property<?> property : ownBlockState.getProperties()) {
            if (property instanceof BooleanProperty booleanProperty && toState.hasProperty(booleanProperty)) {
                toState = toState.setValue(booleanProperty, ownBlockState.getValue(booleanProperty));
            }
            if (property instanceof DirectionProperty directionProperty && toState.hasProperty(directionProperty)) {
                toState = toState.setValue(directionProperty, ownBlockState.getValue(directionProperty));
            }
        }
        return toState;
    }


    protected Pair<TextureAtlasSprite, Boolean> getSpriteFromModel(BakedQuad shape, BakedModel model, Block paint) {
        float[] normalData = new float[4];
        LightUtil.unpack(shape.getVertices() , normalData, DefaultVertexFormat.BLOCK, 0, 4);
        Direction normal = Direction.getNearest(normalData[0], normalData[1], normalData[2]);
        List<BakedQuad> quads = model.getQuads(paint.defaultBlockState(), normal, new Random());
        return quads.isEmpty() ? Pair.of(getMissingTexture(), false) : Pair.of(quads.get(0).getSprite(), quads.get(0).isTinted());
    }

    protected BakedQuad copyQuad(BakedQuad toCopy, TextureAtlasSprite sprite, boolean shouldTint) {
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

    public TextureAtlasSprite getMissingTexture() {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "missingno"));
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
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
        Block paint = data.getData(SinglePaintedBlockEntity.PAINT);
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
}
