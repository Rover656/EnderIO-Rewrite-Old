package com.enderio.base.painted;

import com.enderio.base.common.block.painted.DoublePaintedBlockEntity;
import com.enderio.base.common.block.painted.SinglePaintedBlockEntity;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaintedSlabModel extends PaintedModel implements IDynamicBakedModel {

    private final Block referenceModel;

    public PaintedSlabModel(Block referenceModel, ItemTransforms transforms) {
        super(transforms);
        this.referenceModel = referenceModel;
    }

    @Override
    protected Block copyModelFromBlock() {
        return referenceModel;
    }


    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        if (state.hasProperty(SlabBlock.TYPE)) {
            SlabType slabType = state.getValue(SlabBlock.TYPE);
            if (slabType == SlabType.BOTTOM || slabType == SlabType.DOUBLE) {
                Block paint = extraData.getData(DoublePaintedBlockEntity.PAINT);
                List<BakedQuad> shape = getModel(referenceModel.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM)).getQuads(state, side, rand, EmptyModelData.INSTANCE);
                quads.addAll(getQuadsUsingShape(paint, shape, side, rand, null));
            }
            if (slabType == SlabType.TOP || slabType == SlabType.DOUBLE) {
                Block paint = extraData.getData(DoublePaintedBlockEntity.PAINT2);
                List<BakedQuad> shape = getModel(referenceModel.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)).getQuads(state, side, rand, EmptyModelData.INSTANCE);
                quads.addAll(getQuadsUsingShape(paint, shape, side, rand, null));
            }
        }
        return quads;
    }
}
