package com.enderio.base.common.capability.darksteel;

import com.enderio.core.common.capability.INamedNBTSerializable;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.Optional;

public interface IDarkSteelUpgrade extends INamedNBTSerializable<Tag> {

    default String getDisplayName() {
        return getSerializedName();
    }

    /**
     * Only one upgrade can be added for a slot. For example, if you had a jetpack and glider upgrade, both upgrades returning the same
     * slot would prevent them from both being applied to the same item.
     * @return the slot
     */
    default String getSlot() {
        return getSerializedName();
    }

    /**
     * If this upgrade has multiple tiers (eg, level 1, 2, 3 etc) return the next tier.
     * @return the next tier
     */
    default Optional<? extends IDarkSteelUpgrade> getNextTier() {
        return Optional.empty();
    }

    @Override
    default Tag serializeNBT() {
        return StringTag.valueOf(getSerializedName());
    }

    @Override
    default void deserializeNBT(Tag nbt) {
    }

}
