package com.enderio.base.common.blockentity;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
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
    private ItemStackHandler itemHandler = new ItemStackHandler();//TODO test cap with other mods.
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private UUID uuid;

    public GraveBE(BlockEntityType<?> type, BlockPos pWorldPosition, BlockState pBlockState) {
        super(type, pWorldPosition, pBlockState);
    }
    
    public void makeGrave(Collection<ItemEntity> drops, Player player) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        drops.forEach(entity -> stacks.add(entity.getItem()));
        this.items.addAll(stacks);
        this.itemHandler = createHandler(items);
        
        setUuid(player.getUUID());
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
    
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        try {
            this.uuid = pTag.getUUID("owner");
            System.out.println(uuid + " load");
        }catch (Exception e) {
            //null uuid
        }
        ContainerHelper.loadAllItems(pTag, items);
        
    }
    
    @Override
    public CompoundTag save(CompoundTag pTag) {
        super.save(pTag);
        ContainerHelper.saveAllItems(pTag, items);
        pTag.putUUID("owner", uuid);
        System.out.println(pTag.toString());
        return pTag;
    }

}
