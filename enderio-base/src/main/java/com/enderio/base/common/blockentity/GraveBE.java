package com.enderio.base.common.blockentity;

import java.util.Collection;
import java.util.UUID;

import com.enderio.base.common.util.EIOCapabilityManager;
import com.enderio.base.common.util.GraveCap;
import com.enderio.base.common.util.IGraveCap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class GraveBE extends BlockEntity{    
    private GraveCap graveCap = new GraveCap();
    private LazyOptional<IGraveCap> graveHandler = LazyOptional.of(() -> graveCap);

    public GraveBE(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }
    
    public void makeGrave(Collection<ItemEntity> drops, Player player) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        setUuid(player.getUUID());
        graveCap.setItems(stacks);
    }
    
    public NonNullList<ItemStack> getItems() {
        return  graveCap.getItems();
    }
    
    public UUID getUuid() {
        return this.graveCap.getUuid();
    }
    
    public void setUuid(UUID uuid) {
        this.graveCap.setUuid(uuid);
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == EIOCapabilityManager.EIO_GRAVE_CAP) {
            this.graveHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
