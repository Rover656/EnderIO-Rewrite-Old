package com.enderio.base.common.capability.darksteel;

import com.enderio.base.common.item.darksteel.upgrades.DarkSteelUpgrades;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.*;

public class DarkSteelUpgradeable implements IDarkSteelUpgradable {

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
