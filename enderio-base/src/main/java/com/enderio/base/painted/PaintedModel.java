package com.enderio.base.painted;

import com.enderio.base.common.block.painted.SinglePaintedBlockEntity;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.pipeline.LightUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class PaintedModel implements IDynamicBakedModel {

    private static Map<ItemStack, List<Pair<BakedModel, RenderType>>> itemRenderCache = new HashMap<>();

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
        Direction direction = null;
        if (state != null) {
            for (Property<?> property : state.getProperties()) {
                if (property instanceof DirectionProperty directionProperty) {
                    direction = state.getValue(directionProperty).getOpposite();
                }
            }
        }
        return getQuadsUsingShape(shape, side, rand, extraData, direction);
    }

    public List<BakedQuad> getQuadsUsingShape(List<BakedQuad> shape, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, @Nullable Direction rotation) {
        Block paint = extraData.getData(SinglePaintedBlockEntity.PAINT);
        if (paint != null) {
            BakedModel model = getModel(paintWithRotation(paint, rotation));
            Optional<Pair<TextureAtlasSprite, Boolean>> spriteOptional = getSpriteData(paint, side, rand, rotation);
            List<BakedQuad> returnQuads = new ArrayList<>();
            for (BakedQuad shapeQuad : shape) {
                Pair<TextureAtlasSprite, Boolean> spriteData = spriteOptional.orElseGet(() -> getSpriteFromModel(shapeQuad, model, paint, rotation));
                returnQuads.add(copyQuad(shapeQuad, spriteData.getFirst(), spriteData.getSecond()));
            }
            return returnQuads;
        }
        return List.of();
    }

    private BlockState paintWithRotation(Block paint, Direction rotation) {
        BlockState state = paint.defaultBlockState();
        if (rotation != null) {
            for (Property<?> property : state.getProperties()) {
                if (property instanceof DirectionProperty directionProperty && directionProperty.getPossibleValues().contains(rotation)) {
                    state = state.setValue(directionProperty, rotation);
                }
            }
        }
        return state;
    }

    private Optional<Pair<TextureAtlasSprite, Boolean>> getSpriteData(Block paint, Direction side, Random rand, Direction rotation) {
        BlockState state = paintWithRotation(paint, rotation);
        for (BakedQuad quad : getModel(state).getQuads(state, side, rand, EmptyModelData.INSTANCE)) {
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
            if (property instanceof EnumProperty enumProperty && toState.hasProperty(enumProperty)) {
                toState = toState.setValue(enumProperty, ownBlockState.getValue(enumProperty));
            }
        }
        return toState;
    }


    protected Pair<TextureAtlasSprite, Boolean> getSpriteFromModel(BakedQuad shape, BakedModel model, Block paint, Direction rotation) {
        float[] normalData = new float[4];
        BlockState state = paintWithRotation(paint, rotation);
        LightUtil.unpack(shape.getVertices() , normalData, DefaultVertexFormat.BLOCK, 0, 4);
        Direction normal = Direction.getNearest(normalData[0], normalData[1], normalData[2]);
        List<BakedQuad> quads = model.getQuads(state, normal, new Random());
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

    // used to modify model based on ItemStack data
    @Override
    public boolean isLayered() {
        return true;
    }

    //modify model and return this here
    public List<Pair<BakedModel, RenderType>> getLayerModels(ItemStack itemStack, boolean fabulous) {
        return itemRenderCache.computeIfAbsent(itemStack, itemStack1 -> createLayerModels(itemStack, fabulous));

    }

    private List<Pair<BakedModel, RenderType>> createLayerModels(ItemStack itemStack, boolean fabulous) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains("BlockEntityTag")) {
            CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("paint")) {
                Block paint = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockEntityTag.getString("paint")));
                return List.of(Pair.of(new ItemPaintedModel(paint, copyModelFromBlock()), ItemBlockRenderTypes.getRenderType(itemStack, fabulous)));
            }
        }
        return List.of(Pair.of(this, ItemBlockRenderTypes.getRenderType(itemStack, fabulous)));
    }


    @Override
    public ItemTransforms getTransforms() {
        return ItemPaintedModel.TRANSFORMS;
    }

    private class ItemPaintedModel implements IDynamicBakedModel {

        private final Block paint;
        private final Block shapeFrom;
        private Map<Direction, List<BakedQuad>> bakedQuads = new HashMap<>();

        private ItemPaintedModel(Block paint, Block shapeFrom) {
            this.paint = paint;
            this.shapeFrom = shapeFrom;
        }

        @Nonnull
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
            return bakedQuads.computeIfAbsent(side, side1 -> {
                IModelData data = new ModelDataMap.Builder().withInitial(SinglePaintedBlockEntity.PAINT, paint).build();
                return PaintedModel.this.getQuadsUsingShape(Minecraft.getInstance().getItemRenderer().getModel(shapeFrom.asItem().getDefaultInstance(), null, null, 0).getQuads(state, side, rand, EmptyModelData.INSTANCE), side, rand, data, null);
            });
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
        public TextureAtlasSprite getParticleIcon() {
            return PaintedModel.this.getParticleIcon();
        }

        @Override
        public ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }

        @Override
        public ItemTransforms getTransforms() {
            return TRANSFORMS;
        }
        private static ItemTransforms TRANSFORMS = new ItemTransforms(
            new ItemTransform(new Vector3f(75,45,0), new Vector3f(0, 2.5f/16, 0), new Vector3f(0.375f, 0.375f, 0.375f)),
            new ItemTransform(new Vector3f(75,45,0), new Vector3f(0, 2.5f/16, 0), new Vector3f(0.375f, 0.375f, 0.375f)),
            new ItemTransform(new Vector3f(0,225,0), new Vector3f(0, 0, 0), new Vector3f(0.4f, 0.4f, 0.4f)),
            new ItemTransform(new Vector3f(0,45,0), new Vector3f(0, 0, 0), new Vector3f(0.4f, 0.4f, 0.4f)),
            new ItemTransform(new Vector3f(0,0,0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)),
            new ItemTransform(new Vector3f(30,225,0), new Vector3f(0, 0, 0), new Vector3f(0.625f, 0.625f, 0.625f)),
            new ItemTransform(new Vector3f(0,0,0), new Vector3f(0, 3f/16, 0), new Vector3f(0.25f, 0.25f, 0.25f)),
            new ItemTransform(new Vector3f(0,0,0), new Vector3f(0, 0, 0), new Vector3f(0.5f, 0.5f, 0.5f)));
    }
}
