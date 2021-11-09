package com.enderio.base.common.item.darksteel.upgrades;

import com.enderio.base.common.capability.darksteel.IDarkSteelUpgrade;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class DarkSteelUpgrades {


    private static final DarkSteelUpgrades INST = new DarkSteelUpgrades();

    static {
        INST.registerUpgrade(SpoonUpgrade::new);
        INST.registerUpgrade(EmpoweredUpgrade::new);
    }

    public static DarkSteelUpgrades instance() {return INST; }

    private final Map<String, Supplier<IDarkSteelUpgrade>> registeredUpgrades = new HashMap<>();

    private final Map<String, UpgradeSet> possibleUpgradeLists = new HashMap<>();

    private DarkSteelUpgrades() {}

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

    public void addUpgradesToSet(UpgradeSet upgrades) {
        UpgradeSet set = possibleUpgradeLists.getOrDefault(upgrades.getName(), new UpgradeSet(upgrades.getName()));
        set.addUpgrades(upgrades);
        possibleUpgradeLists.put(set.getName(), set);
    }

    public Optional<UpgradeSet> getUpgradeSet(String name) {
        return Optional.of(possibleUpgradeLists.get(name));
    }

    public Collection<IDarkSteelUpgrade> getUpgradesForSet(String name) {
        Set<String> upNames = possibleUpgradeLists.get(name).getUpgrades();
        List<IDarkSteelUpgrade> res = new ArrayList<>(upNames.size());

        for(String up : upNames) {
            createUpgrade(up).ifPresent(res::add);
        }
        return res;
    }

    public static class UpgradeSet {

        private final String name;
        private final Set<String> upgrades;

        public UpgradeSet(String name) {
            this.name = name;
            upgrades = new HashSet<>();
        }

        public UpgradeSet addUpgrades(UpgradeSet set) {
            upgrades.addAll(set.getUpgrades());
            return this;
        }

        public UpgradeSet addUpgrades(String... upgrade) {
            upgrades.addAll(Arrays.stream(upgrade).toList());
            return this;
        }

        public Set<String> getUpgrades() {
            return Collections.unmodifiableSet(upgrades);
        }

        public String getName() {
            return name;
        }

    }

}
