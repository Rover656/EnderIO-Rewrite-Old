package com.enderio.base.common.util;

import java.util.UUID;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class GraveCap implements IGraveCap{
    private NonNullList<ItemStack> stacks = NonNullList.create();
    private UUID uuid;
    
    public GraveCap() {
        
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return stacks.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack;
        
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag nbt = new CompoundTag();
        ContainerHelper.saveAllItems(nbt, stacks);
        try {
            this.uuid = nbt.getUUID("owner");
            System.out.println(uuid + " load");
        }catch (Exception e) {
            //null uuid
        }
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ContainerHelper.loadAllItems(nbt, stacks);
        nbt.putUUID("owner", uuid);
    }
    
    @Override
    public NonNullList<ItemStack> getItems(){
        return this.stacks;
    }
    
    @Override
    public void setItems(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
        serializeNBT();
    }
    
    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
