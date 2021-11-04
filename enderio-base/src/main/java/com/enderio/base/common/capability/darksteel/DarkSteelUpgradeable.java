package com.enderio.base.common.capability.darksteel;

import com.enderio.base.common.item.darksteel.upgrades.SpoonUpgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class DarkSteelUpgradeable implements IDarkSteelUpgradable {

    final static Map<String, Supplier<IDarkSteelUpgrade>> REGISTERED_UPGRADES = new HashMap<>();

    public static void registerUpgrade(Supplier<IDarkSteelUpgrade> upgrade) {
        REGISTERED_UPGRADES.put(upgrade.get().getSerializedName(), upgrade);
    }

    private static Optional<IDarkSteelUpgrade> createUpgrade(String name) {
        Supplier<IDarkSteelUpgrade> val = REGISTERED_UPGRADES.get(name);
        if(val == null) {
            return Optional.empty();
        }
        return Optional.of(val.get());
    }

    static {
        registerUpgrade(SpoonUpgrade::new);
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
                createUpgrade(key).ifPresent(upgrade -> {
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
    public boolean hasUpgrade(String upgrade) {
        return upgrades.containsKey(upgrade);
    }
}
