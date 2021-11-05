package com.enderio.base.common.item.darksteel.upgrades;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.darksteel.IDarkSteelUpgradable;
import com.enderio.base.common.capability.darksteel.IDarkSteelUpgrade;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public final class DarkSteelUpgrades {


    public static final DarkSteelUpgrades INST = new DarkSteelUpgrades();

    static {
        INST.registerUpgrade(SpoonUpgrade::new);
        INST.registerUpgrade(EmpoweredUpgrade::new);
    }

    private final Map<String, Supplier<IDarkSteelUpgrade>> registeredUpgrades = new HashMap<>();



    public void registerUpgrade(Supplier<IDarkSteelUpgrade> upgrade) {
        registeredUpgrades.put(upgrade.get().getSerializedName(), upgrade);
    }

    public Optional<IDarkSteelUpgrade> createUpgrade(String name) {
        Supplier<IDarkSteelUpgrade> val = registeredUpgrades.get(name);
        if (val == null) {
            return Optional.empty();
        }
        return Optional.of(val.get());
    }

    public static void addUpgrade(ItemStack is, Supplier<? extends IDarkSteelUpgrade> upgrade) {
        is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).ifPresent(upgradable -> upgradable.addUpgrade(upgrade.get()));
    }

    public static Collection<IDarkSteelUpgrade> getUpgrades(ItemStack is) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(IDarkSteelUpgradable::getUpgrades).orElse(Collections.emptyList());
    }

    public static boolean hasUpgrade(ItemStack is, String name) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(upgradable -> upgradable.hasUpgrade(name)).orElse(false);
    }

}
