package com.enderio.base.common.capability.darksteel;

import com.enderio.core.common.capability.INamedNBTSerializable;
import net.minecraft.nbt.Tag;

import java.util.Optional;

public interface IDarkSteelUpgradable extends INamedNBTSerializable<Tag> {

    @Override
    default String getSerializedName() {
        return "DarkSteelUpgradable";
    }

    void addUpgrade(IDarkSteelUpgrade upgrade);

    Optional<IDarkSteelUpgrade> getUpgrade(String upgrade);

    boolean hasUpgrade(String upgrade);

}
