package com.enderio.base.common.block.painted;

import com.enderio.base.EIOBlockEntities;
import com.enderio.base.EIOBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PaintedStairBlock extends StairBlock implements EntityBlock {

    public PaintedStairBlock(Properties properties) {
        super( () -> EIOBlocks.PAINTED_SAND.get().defaultBlockState(), properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return EIOBlockEntities.SINGLE_PAINTED.create(pos, state);
    }
}
