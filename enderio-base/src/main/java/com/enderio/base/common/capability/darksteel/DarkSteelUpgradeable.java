package com.enderio.base.common.capability.darksteel;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.item.darksteel.upgrades.DarkSteelUpgrades;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class DarkSteelUpgradeable implements IDarkSteelUpgradable {

    //----------------------- Utils

    public static void addUpgrade(ItemStack is, Supplier<? extends IDarkSteelUpgrade> upgrade) {
        is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).ifPresent(upgradable -> upgradable.applyUpgrade(upgrade.get()));
    }

    public static void addUpgrade(ItemStack is, IDarkSteelUpgrade upgrade) {
        is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).ifPresent(upgradable -> upgradable.applyUpgrade(upgrade));
    }

    public static Collection<IDarkSteelUpgrade> getUpgrades(ItemStack is) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(IDarkSteelUpgradable::getUpgrades).orElse(Collections.emptyList());
    }

    public static boolean hasUpgrade(ItemStack is, String name) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(upgradable -> upgradable.hasUpgrade(name)).orElse(false);
    }

    public static <T extends IDarkSteelUpgrade> Optional<T> getUpgradeAs(ItemStack is, String upgrade) {
        Optional<IDarkSteelUpgradable> cap = is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).resolve();
        if(cap.isEmpty()) {
            return Optional.empty();
        }
        return cap.get().getUpgradeAs(upgrade);
    }

    //----------------------- Class

    private Map<String, IDarkSteelUpgrade> upgrades = new HashMap<>();

    private String upgradeSet;

    public DarkSteelUpgradeable(){
        this("");
    }

    public DarkSteelUpgradeable(String upgradeSet) {
        this.upgradeSet = upgradeSet == null ? "" : upgradeSet;
    }

    @Override
    public void applyUpgrade(IDarkSteelUpgrade upgrade) {
        upgrades.put(upgrade.getSerializedName(), upgrade);
    }

    @Override
    public Optional<IDarkSteelUpgrade> getUpgrade(String upgrade) {
        return Optional.of(upgrades.get(upgrade));
    }

    @Override
    public <T extends IDarkSteelUpgrade> Optional<T> getUpgradeAs(String upgrade) {
        try {
            return Optional.ofNullable((T) upgrades.get(upgrade));
        } catch (Exception e) {
            //TODO: Log
            System.out.println("DarkSteelUpgradeable.getUpgradeAs: upgrade=" + upgrade + " Error=" + e);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Collection<IDarkSteelUpgrade> getUpgrades() {
        return upgrades.values();
    }

    @Override
    public boolean hasUpgrade(String upgrade) {
        return upgrades.containsKey(upgrade);
    }

    @Override
    public Collection<String> getAllPossibleUpgrades() {
        return DarkSteelUpgrades.instance().getUpgradeSet(upgradeSet).map(DarkSteelUpgrades.UpgradeSet::getUpgrades).orElse(Collections.emptySet());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (var entry : upgrades.entrySet()) {
            tag.put(entry.getKey(), entry.getValue().serializeNBT());
        }
        tag.putString("upgradeSet", upgradeSet);
        return tag;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        upgrades.clear();
        if(tag instanceof CompoundTag nbt) {
            for (String key : nbt.getAllKeys()) {
                DarkSteelUpgrades.instance().createUpgrade(key).ifPresent(upgrade -> {
                    upgrade.deserializeNBT(nbt.get(key));
                    applyUpgrade(upgrade);
                });
            }
            upgradeSet = nbt.getString("upgradeSet");
        }
    }

}
