package com.enderio.base.common.capability.darksteel;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.item.darksteel.upgrades.DarkSteelUpgrades;
import com.enderio.base.common.item.darksteel.upgrades.EmpoweredUpgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class DarkSteelUpgradeable implements IDarkSteelUpgradable {

    //----------------------- Utils

    public static void addUpgrade(ItemStack is, Supplier<? extends IDarkSteelUpgrade> upgrade) {
        is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).ifPresent(upgradable -> upgradable.addUpgrade(upgrade.get()));
    }

    public static ItemStack addUpgrade(ItemStack is, IDarkSteelUpgrade upgrade) {
        is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).ifPresent(upgradable -> upgradable.addUpgrade(upgrade));
        return is;
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

    public static Collection<IDarkSteelUpgrade> getUpgradesThatCanBeAppliedAtTheMoment(ItemStack is) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(IDarkSteelUpgradable::getUpgradesThatCanBeAppliedAtTheMoment).orElse(Collections.emptyList());
    }

    public static Collection<IDarkSteelUpgrade> getAllPossibleUpgrades(ItemStack is) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(IDarkSteelUpgradable::getAllPossibleUpgrades).orElse(Collections.emptyList());
    }

    //----------------------- Class

    private final Map<String, IDarkSteelUpgrade> upgrades = new HashMap<>();

    private String upgradeSet;

    public DarkSteelUpgradeable(){
        this("");
    }

    public DarkSteelUpgradeable(String upgradeSet) {
        this.upgradeSet = upgradeSet == null ? "" : upgradeSet;
    }

    @Override
    public void addUpgrade(IDarkSteelUpgrade upgrade) {
        removeUpgradeInSlot(upgrade.getSlot());
        upgrades.put(upgrade.getSerializedName(), upgrade);
    }

    @Override
    public void removeUpgrade(String name) {
        upgrades.remove(name);
    }

    private void removeUpgradeInSlot(String slot) {
        Optional<String> toRemove = Optional.empty();
        for(var entry : upgrades.entrySet()) {
            if(entry.getValue().getSlot().equals(slot)) {
                toRemove = Optional.of(entry.getKey());
                break;
            }
        }
        toRemove.ifPresent(upgrades::remove);
    }

    @Override
    public boolean canApplyUpgrade(IDarkSteelUpgrade upgrade) {
        if(upgrades.isEmpty()) {
            return EmpoweredUpgrade.NAME.equals(upgrade.getSerializedName()) && upgrade.isBaseTier();
        }

        Optional<IDarkSteelUpgrade> existing = getUpgrade(upgrade.getSerializedName());
        if(existing.isPresent()) {
            return existing.get().isValidUpgrade(upgrade);
        }
        if(!upgrade.isBaseTier()) {
            return false;
        }
        return DarkSteelUpgrades.instance().getUpgradeSet(upgradeSet).map(set -> set.getUpgrades().contains(upgrade.getSerializedName())).orElse(false);
    }

    @Override
    public Optional<IDarkSteelUpgrade> getUpgrade(String upgrade) {
        return Optional.ofNullable(upgrades.get(upgrade));
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
    public Collection<IDarkSteelUpgrade> getUpgradesThatCanBeAppliedAtTheMoment() {
        if(upgrades.isEmpty()) {
            return List.of(EmpoweredUpgrade.createBaseUpgrade());
        }
        final List<IDarkSteelUpgrade> result = new ArrayList<>();
        upgrades.values().forEach(upgrade -> upgrade.getNextTier().ifPresent(result::add));

        getAllPossibleUpgrades().forEach(upgrade -> {
            if(!hasUpgrade(upgrade.getSerializedName())) {
                result.add(upgrade);
            }
        });
        return result;
    }

    @Override
    public Collection<IDarkSteelUpgrade> getAllPossibleUpgrades() {
        Set<String> upgradeNames = DarkSteelUpgrades.instance().getUpgradeSet(upgradeSet).map(DarkSteelUpgrades.UpgradeSet::getUpgrades).orElse(Collections.emptySet());
        final List<IDarkSteelUpgrade> result = new ArrayList<>();
        upgradeNames.forEach(s -> DarkSteelUpgrades.instance().createUpgrade(s).ifPresent(result::add));
        return result;
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
                    addUpgrade(upgrade);
                });
            }
            upgradeSet = nbt.getString("upgradeSet");
        }
    }

}
