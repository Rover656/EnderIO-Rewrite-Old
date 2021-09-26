package com.enderio.base.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PaintedFenceBlockEntity extends BlockEntity {

    private Block paint;

    public Block getPaint() {
        return paint;
    }

    public void setPaint(Block paint) {
        this.paint = paint;
    }

    public static final ModelProperty<Block> PAINT = new ModelProperty<>();

    public PaintedFenceBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(PAINT, paint).build();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag tag = new CompoundTag();
        writePaint(tag);
        return new ClientboundBlockEntityDataPacket(worldPosition, -1, tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        Block oldPaint = paint;
        CompoundTag tag = pkt.getTag();
        handleUpdateTag(tag);
        if (oldPaint != paint) {
            ModelDataManager.requestModelDataRefresh(this);
            level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()), 9);
        }
    }



    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        readPaint(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        writePaint(nbt);
        return nbt;
    }


    private void readPaint(CompoundTag tag) {
        if (tag.contains("paint")) {
            paint = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.get("paint").getAsString()));
            if (level != null) {
                if(level.isClientSide) {
                    ModelDataManager.requestModelDataRefresh(this);
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
                }
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        writePaint(tag);
        return super.save(tag);
    }

    private void writePaint(CompoundTag tag) {
        if (paint != null) {
            tag.putString("paint", paint.getRegistryName().toString());
        }
    }
}
