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
    private final Map<Capability<?>, LazyOptional<?>> capabilities;
    private final Map<String, LazyOptional<? extends INBTSerializable<Tag>>> serializedCaps;

    public MultiCapabilityProvider() {
        capabilities = new HashMap<>();
        serializedCaps = new HashMap<>();
    }

    public <T> void addSimple(@Nonnull Capability<T> cap, @Nonnull LazyOptional<?> optional) {
        capabilities.putIfAbsent(cap, optional);
    }

    public <T> void addSerialized(@Nonnull Capability<T> cap, @Nonnull LazyOptional<? extends INamedNBTSerializable<Tag>> optional) {
        capabilities.putIfAbsent(cap, optional);
        serializedCaps.putIfAbsent("", optional);
    }

    public <T> void addSerialized(String serializedName, @Nonnull Capability<T> cap, @Nonnull LazyOptional<? extends INBTSerializable<Tag>> optional) {
        capabilities.putIfAbsent(cap, optional);
        serializedCaps.putIfAbsent(serializedName, optional);
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

        for (var entry : serializedCaps.entrySet()) {
            entry
                .getValue()
                .ifPresent(capability -> {
                    String key = entry.getKey();
                    if (capability instanceof INamedNBTSerializable<Tag> named) {
                        key = named.getSerializedName();
                    }

                    if (!"".equals(key)) {
                        tag.put(key, capability.serializeNBT());
                    }
                });
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (var entry : serializedCaps.entrySet()) {
            entry
                .getValue()
                .ifPresent(capability -> {
                    String key = entry.getKey();
                    if (capability instanceof INamedNBTSerializable<Tag> named) {
                        key = named.getSerializedName();
                    }

                    if (!"".equals(key) && nbt.contains(key)) {
                        capability.deserializeNBT(nbt.get(key));
                    }
                });
        }
    }
}
