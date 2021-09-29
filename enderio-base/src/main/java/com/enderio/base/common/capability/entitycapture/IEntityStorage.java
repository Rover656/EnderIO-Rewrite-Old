package com.enderio.base.common.capability.entitycapture;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface IEntityStorage extends INBTSerializable<Tag> {
    @Nonnull
    Optional<ResourceLocation> getEntityType();
    @Nonnull
    Optional<CompoundTag> getEntityNBT();

    void setEntityType(ResourceLocation entityType);
    void setEntityNBT(CompoundTag nbt);
}
