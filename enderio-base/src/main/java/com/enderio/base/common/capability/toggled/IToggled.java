package com.enderio.base.common.capability.toggled;

import com.enderio.core.common.capability.INamedNBTSerializable;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public interface IToggled extends INamedNBTSerializable<Tag> {
    @Override
    default String getSerializedName() {
        return "ToggleState";
    }

    @Nonnull
    boolean isEnabled();
    void toggle();

    @Override
    Tag serializeNBT();

    @Override
    void deserializeNBT(Tag nbt);
}
