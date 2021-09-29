package com.enderio.core.mixin.common;

import com.enderio.core.common.capability.MultiCapabilityProvider;
import com.enderio.core.common.capability.IMultiCapProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(IMultiCapProvider.class)
public interface MultiCapProviderMixin extends IForgeItem {
    @Shadow
    @Nullable
    MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider);

    @Nullable
    @Override
    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return initCapabilities(stack, nbt, new MultiCapabilityProvider());
    }
}
