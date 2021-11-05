package com.enderio.base.common.capability.darksteel;

import com.enderio.core.common.capability.INamedNBTSerializable;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public interface IDarkSteelUpgrade extends INamedNBTSerializable<Tag> {

    default String getDisplayName() {
        return getSerializedName();
    }

    @Override
    default Tag serializeNBT() {
        return StringTag.valueOf(getSerializedName());
    }

    @Override
    default void deserializeNBT(Tag nbt) {
    }

}
