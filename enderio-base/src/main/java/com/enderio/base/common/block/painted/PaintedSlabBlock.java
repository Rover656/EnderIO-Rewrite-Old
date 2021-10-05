package com.enderio.base.common.block.painted;

import com.enderio.base.common.blockentity.EIOBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PaintedSlabBlock extends SlabBlock implements EntityBlock {

    public PaintedSlabBlock(Properties p_56359_) {
        super(p_56359_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return EIOBlockEntities.DOUBLE_PAINTED.create(pos, state);
    }

}
