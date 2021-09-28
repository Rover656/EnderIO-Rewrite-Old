package com.enderio.base.common.blockentity;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GraveBE extends BlockEntity{
    private NonNullList<ItemStack> items = NonNullList.create();
    private ItemStackHandler itemHandler = createHandler(items);//TODO test cap with other mods.
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private UUID uuid ;
    private Direction deadDir;

    public GraveBE(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }
    
    public void addInventory(Player player) {
        this.items.addAll(player.getInventory().items);
        this.items.addAll(player.getInventory().armor);
        this.items.addAll(player.getInventory().offhand);
        this.itemHandler = createHandler(items);
        
        setUuid(player.getUUID());
        this.deadDir = player.getMotionDirection();
    }
    
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
    public Direction getDeadDirection() {
        return this.deadDir;
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            handler.cast();
        }
        return super.getCapability(cap, side);
    }
    
    private ItemStackHandler createHandler(NonNullList<ItemStack> items) {
        return new ItemStackHandler(items) {
            
            @Override
            public void setSize(int size) {
                
            }
            
            @Override
            public void setStackInSlot(int slot, ItemStack stack) {

            }
            
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
            
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return stack;
            }
        };
    }

}
