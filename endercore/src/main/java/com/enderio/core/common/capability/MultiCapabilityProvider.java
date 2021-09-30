package com.enderio.core.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Automatically combines multiple capabilities together; handling serialization too.
 */
public class MultiCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    // TODO: Allow non-serialized too...
    private final Map<Capability<?>, LazyOptional<? extends INBTSerializable<Tag>>> capabilities;

    public MultiCapabilityProvider() {
        capabilities = new HashMap<>();
    }

    @Nonnull
    public <T> MultiCapabilityProvider add(@Nonnull Capability<T> cap, @Nonnull LazyOptional<? extends INBTSerializable<Tag>> optional) {
        capabilities.putIfAbsent(cap, optional);
        return this;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return capabilities
            .getOrDefault(cap, LazyOptional.empty())
            .cast();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        for (var entry : capabilities.entrySet()) {
            entry
                .getValue()
                .ifPresent(capability -> {
                    tag.put(entry
                        .getKey()
                        .getName(), capability.serializeNBT());
                });
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (var entry : capabilities.entrySet()) {
            entry
                .getValue()
                .ifPresent(capability -> {
                    String key = entry
                        .getKey()
                        .getName(); // TODO: More robust keys? Moving a package or renaming a class will break this completely.
                    if (nbt.contains(key)) {
                        capability.deserializeNBT(nbt.get(key));
                    }
                });
        }
    }
}
