package com.enderio.base.common.util;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Constants;

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
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (!stacks.get(i).isEmpty())
            {
                CompoundTag itemTag = new CompoundTag();
                stacks.get(i).save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putUUID("owner", uuid);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundTag itemTags = tagList.getCompound(i);
            stacks.add(ItemStack.of(itemTags));
        }
        try {
            this.uuid = nbt.getUUID("owner");
            System.out.println(this.uuid);
        }catch (Exception e) {
            //null uuid
        }
    }
    
    @Override
    public NonNullList<ItemStack> getItems(){
        return this.stacks;
    }
    
    @Override
    public void setItems(Collection<ItemStack> stacks) {
        this.stacks.clear();
        this.stacks.addAll(stacks);
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
