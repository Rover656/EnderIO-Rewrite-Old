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

    void applyUpgrade(IDarkSteelUpgrade upgrade);

    Optional<IDarkSteelUpgrade> getUpgrade(String upgradeName);

    <T extends IDarkSteelUpgrade> Optional<T> getUpgradeAs(String upgrade);

    boolean hasUpgrade(String upgradeName);

    Collection<IDarkSteelUpgrade> getUpgrades();

    Collection<String> getAllPossibleUpgrades();
}
