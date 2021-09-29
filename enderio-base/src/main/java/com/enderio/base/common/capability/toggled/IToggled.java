package com.enderio.base.common.capability.toggled;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public interface IToggled extends INBTSerializable<Tag> {
    @Nonnull
    boolean isEnabled();
    void toggle();

    @Override
    Tag serializeNBT();

    @Override
    void deserializeNBT(Tag nbt);
}
