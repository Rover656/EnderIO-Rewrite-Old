package com.enderio.machines.common.block;

import com.enderio.machines.common.EIOMachinesBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PaintedFenceBlock extends FenceBlock implements EntityBlock {

    public PaintedFenceBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return EIOMachinesBlockEntities.PAINTED_FENCE.create(pos, state);
    }

}
