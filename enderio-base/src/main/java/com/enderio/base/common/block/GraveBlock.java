package com.enderio.base.common.block;

import com.enderio.base.EIOBlockEntities;
import com.enderio.base.common.blockentity.GraveBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

public class GraveBlock extends Block implements EntityBlock{

    public GraveBlock() {
        super(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noDrops().noCollission());
    }
    
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof GraveBE grave) {
            if (pPlayer.getUUID().equals(grave.getUuid())) {
                grave.getItems().forEach(item ->{
                    if (!pPlayer.addItem(item)) {
                        Containers.dropItemStack(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), item);
                        pLevel.removeBlock(grave.getBlockPos(), false);
                        pLevel.removeBlockEntity(grave.getBlockPos());
                    }
                });
            } else {
                //TODO message
            }
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return EIOBlockEntities.GRAVEBE.create(pPos, pState);
    }
    
    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (pLevel.getBlockEntity(pPos) instanceof GraveBE grave) {
            grave.setUuid(pPlacer.getUUID());
        }
    }
}
