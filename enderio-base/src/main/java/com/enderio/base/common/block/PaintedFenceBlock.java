package com.enderio.base.common.block;

import com.enderio.base.EIOBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
        return EIOBlockEntities.PAINTED_FENCE.create(pos, state);
    }

    @Override
    public boolean connectsTo(BlockState pState, boolean pIsSideSolid, Direction pDirection) {
        return super.connectsTo(pState, pIsSideSolid, pDirection);
    }
}
