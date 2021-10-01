package com.enderio.base.common.block.painted;

import com.enderio.base.common.util.PaintUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import javax.annotation.Nullable;

public class PaintedSlabBlockItem extends BlockItem {

    public PaintedSlabBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    //copied with hate, but I need to deal special with tileentitydata in that item
    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {
        MinecraftServer minecraftserver = pLevel.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CompoundTag compoundtag = pStack.getTagElement("BlockEntityTag");
            if (compoundtag != null) {
                BlockEntity blockentity = pLevel.getBlockEntity(pPos);
                if (blockentity != null) {
                    if (!pLevel.isClientSide && blockentity.onlyOpCanSetNbt() && (pPlayer == null || !pPlayer.canUseGameMasterBlocks())) {
                        return false;
                    }

                    CompoundTag compoundtag1 = blockentity.save(new CompoundTag());
                    CompoundTag compoundtag2 = compoundtag1.copy();

                    Block oldPaint = PaintUtils.getBlockFromRL(compoundtag1.getString("paint"));
                    Block oldPaint2 = PaintUtils.getBlockFromRL(compoundtag1.getString("paint2"));

                    if (pState.getValue(SlabBlock.TYPE) != SlabType.BOTTOM) {
                        if (oldPaint2 == null || oldPaint2 == Blocks.AIR) {
                            compoundtag1.putString("paint2", compoundtag.getString("paint"));
                        }
                    }
                    if (pState.getValue(SlabBlock.TYPE) != SlabType.TOP) {
                        if (oldPaint == null || oldPaint == Blocks.AIR) {
                            compoundtag1.putString("paint", compoundtag.getString("paint"));
                        }
                    }
                    compoundtag1.putInt("x", pPos.getX());
                    compoundtag1.putInt("y", pPos.getY());
                    compoundtag1.putInt("z", pPos.getZ());
                    if (!compoundtag1.equals(compoundtag2)) {
                        blockentity.load(compoundtag1);
                        blockentity.setChanged();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
