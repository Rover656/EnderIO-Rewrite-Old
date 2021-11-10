package com.enderio.base.common.item.darksteel.upgrades;

import com.enderio.base.common.capability.darksteel.IDarkSteelUpgrade;

public class SpoonUpgrade implements IDarkSteelUpgrade {

    public static final String NAME = DarkSteelUpgradeRegistry.UPGADE_PREFIX + "spoon";

    public static SpoonUpgrade create() {
        return new SpoonUpgrade();
    }

    @Override
    public String getSerializedName() {
        return NAME;
    }

}
