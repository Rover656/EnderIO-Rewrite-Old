package com.enderio.base.common.capability.darksteel;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.item.darksteel.upgrades.DarkSteelUpgrades;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class DarkSteelUpgradeable implements IDarkSteelUpgradable {

    public static void addUpgrade(ItemStack is, Supplier<? extends IDarkSteelUpgrade> upgrade) {
        is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).ifPresent(upgradable -> upgradable.addUpgrade(upgrade.get()));
    }

    public static Collection<IDarkSteelUpgrade> getUpgrades(ItemStack is) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(IDarkSteelUpgradable::getUpgrades).orElse(Collections.emptyList());
    }

    public static boolean hasUpgrade(ItemStack is, String name) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(upgradable -> upgradable.hasUpgrade(name)).orElse(false);
    }

    public static <T extends IDarkSteelUpgrade> Optional<T> getUpgrade(ItemStack is, Class<T> upgrade) {
        return is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(upgradable -> upgradable.getUpgrade(upgrade)).orElse(Optional.empty());
    }

    private Map<String, IDarkSteelUpgrade> upgrades = new HashMap<>();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (var entry : upgrades.entrySet()) {
            tag.put(entry.getKey(), entry.getValue().serializeNBT());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        upgrades.clear();
        if(tag instanceof CompoundTag nbt) {
            for (String key : nbt.getAllKeys()) {
                DarkSteelUpgrades.INST.createUpgrade(key).ifPresent(upgrade -> {
                    upgrade.deserializeNBT(nbt.get(key));
                    addUpgrade(upgrade);
                });
            }
        }
    }

    @Override
    public void addUpgrade(IDarkSteelUpgrade upgrade) {
        upgrades.put(upgrade.getSerializedName(), upgrade);
    }

    @Override
    public Optional<IDarkSteelUpgrade> getUpgrade(String upgrade) {
        return Optional.of(upgrades.get(upgrade));
    }

    @Override
    public <T extends IDarkSteelUpgrade> Optional<T> getUpgrade(Class<T> upgrade) {
        for (var entry : upgrades.values()) {
            if(upgrade.isAssignableFrom(entry.getClass())) {
                return Optional.of(upgrade.cast(entry));
            }
        }
        return Optional.empty();
    }

    @Override
    public Collection<IDarkSteelUpgrade> getUpgrades() {
        return upgrades.values();
    }

    @Override
    public boolean hasUpgrade(String upgrade) {
        return upgrades.containsKey(upgrade);
    }


}
