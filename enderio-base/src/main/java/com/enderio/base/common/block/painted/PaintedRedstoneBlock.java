package com.enderio.base.common.block.painted;

import com.enderio.base.EIOBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PaintedRedstoneBlock extends PoweredBlock implements EntityBlock {

    public PaintedRedstoneBlock(Properties p_52225_) {
        super(p_52225_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return EIOBlockEntities.SINGLE_PAINTED.create(pos, state);
    }
}
