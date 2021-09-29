package com.enderio.base.painted;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.data.IDynamicBakedModel;

public class PaintedSimpleModel extends PaintedModel implements IDynamicBakedModel {

    private final Block referenceModel;

    public PaintedSimpleModel(Block referenceModel, ItemTransforms transforms) {
        super(transforms);
        this.referenceModel = referenceModel;
    }

    @Override
    protected Block copyModelFromBlock() {
        return referenceModel;
    }
}