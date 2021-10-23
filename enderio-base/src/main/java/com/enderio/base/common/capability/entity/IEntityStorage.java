package com.enderio.base.common.capability.entity;

import com.enderio.core.common.capability.INamedNBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface IEntityStorage extends INamedNBTSerializable<Tag> {
    @Override
    default String getSerializedName() {
        return "EntityStorage";
    }

    @Nonnull
    Optional<ResourceLocation> getEntityType();
    @Nonnull
    Optional<CompoundTag> getEntityNBT();

    void setEntityType(ResourceLocation entityType);
    void setEntityNBT(CompoundTag nbt);
}
