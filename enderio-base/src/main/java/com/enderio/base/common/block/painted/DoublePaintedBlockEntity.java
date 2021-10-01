package com.enderio.base.common.block.painted;

import com.enderio.base.common.util.PaintUtils;
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

public class DoublePaintedBlockEntity extends SinglePaintedBlockEntity {

    private Block paint2;

    public Block getPaint2() {
        return paint2;
    }

    public static final ModelProperty<Block> PAINT2 = new ModelProperty<>();

    public DoublePaintedBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(PAINT, getPaint()).withInitial(PAINT2, paint2).build();
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
        Block oldPaint = getPaint2();
        super.onDataPacket(net, pkt);
        if (oldPaint != paint2) {
            ModelDataManager.requestModelDataRefresh(this);
            level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()), 9);
        }
    }

    @Override
    protected void readPaint(CompoundTag tag) {
        super.readPaint(tag);
        if (tag.contains("paint2")) {
            paint2 = PaintUtils.getBlockFromRL(tag.getString("paint2"));
            if (level != null) {
                if (level.isClientSide) {
                    ModelDataManager.requestModelDataRefresh(this);
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
                }
            }
        }
    }

    @Override
    protected void writePaint(CompoundTag tag) {
        super.writePaint(tag);
        if (paint2 != null) {
            tag.putString("paint2", paint2.getRegistryName().toString());
        }
    }
}
