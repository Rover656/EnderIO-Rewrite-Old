package com.enderio.base.common.capability.darksteel;

import com.enderio.core.common.capability.INamedNBTSerializable;
import net.minecraft.nbt.Tag;

import java.util.Collection;
import java.util.Optional;

public interface IDarkSteelUpgradable extends INamedNBTSerializable<Tag> {

    @Override
    default String getSerializedName() {
        return "DarkSteelUpgradable";
    }

    void addUpgrade(IDarkSteelUpgrade upgrade);

    Optional<IDarkSteelUpgrade> getUpgrade(String upgradeName);

    <T extends IDarkSteelUpgrade> Optional<T> getUpgrade(Class<T> upgrade);

    boolean hasUpgrade(String upgradeName);

    Collection<IDarkSteelUpgrade> getUpgrades();
}
