package com.enderio.core.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Implement for an item that should use the {@link MultiCapabilityProvider} when initializing capabilities.
 */
public interface IMultiCapability {
    @Nullable
    MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider);
}
