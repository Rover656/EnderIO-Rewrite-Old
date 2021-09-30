package com.enderio.base.common.util;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

// A capability for the Grave BE
public interface IGraveCap extends IItemHandler, INBTSerializable<CompoundTag>{
    
    public NonNullList<ItemStack> getItems();
    void setItems(Collection<ItemStack> stacks);
    UUID getUuid();
    void setUuid(UUID uuid);
    
   
}
