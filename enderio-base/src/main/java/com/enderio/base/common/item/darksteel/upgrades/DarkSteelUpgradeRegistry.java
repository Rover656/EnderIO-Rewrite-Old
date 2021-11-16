package com.enderio.base.common.item.darksteel.upgrades;

import com.enderio.base.EnderIO;
import com.enderio.base.common.capability.darksteel.IDarkSteelUpgrade;
import com.enderio.base.common.item.darksteel.upgrades.direct.DirectUpgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public final class DarkSteelUpgradeRegistry {

    public static final String UPGRADE_PREFIX = EnderIO.DOMAIN + ".darksteel.upgrade.";

    private static final DarkSteelUpgradeRegistry INST = new DarkSteelUpgradeRegistry();
    private static final String UPGRADE_IN_STACK_KEY = "dark_steel_upgrade";

    static {
        INST.registerUpgrade(EmpoweredUpgrade::new);
        INST.registerUpgrade(SpoonUpgrade::new);
        INST.registerUpgrade(ForkUpgrade::new);
        INST.registerUpgrade(DirectUpgrade::new);
    }

    public static DarkSteelUpgradeRegistry instance() {return INST; }

    private final Map<String, Supplier<IDarkSteelUpgrade>> registeredUpgrades = new HashMap<>();

    private final Map<String, UpgradeSet> possibleUpgradeLists = new HashMap<>();

    private DarkSteelUpgradeRegistry() {}

    //---------- Upgrade register

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

    //---------- Read / Write of Upgrades to ItemStacks

    public ItemStack writeUpgradeToItemStack(ItemStack stack, IDarkSteelUpgrade upgrade) {
        CompoundTag rootTag = new CompoundTag();
        rootTag.putString("name", upgrade.getSerializedName());
        rootTag.put("data", upgrade.serializeNBT());
        stack.getOrCreateTag().put(UPGRADE_IN_STACK_KEY, rootTag);
        return stack;
    }

    public boolean hasUpgrade(ItemStack stack) {
        if(stack.isEmpty() || !stack.hasTag()) {
            return false;
        }
        return stack.getOrCreateTag().contains(UPGRADE_IN_STACK_KEY);
    }

    public Optional<IDarkSteelUpgrade> readUpgradeFromStack(ItemStack stack) {
        if(stack.isEmpty() || !stack.hasTag()) {
            return Optional.empty();
        }
        Tag upTag = stack.getOrCreateTag().get(UPGRADE_IN_STACK_KEY);
        if(upTag instanceof CompoundTag rootTag) {
            String serName = rootTag.getString("name");
            final Optional<IDarkSteelUpgrade> upgrade = createUpgrade(serName);
            return upgrade.map(up -> {
                up.deserializeNBT(rootTag.get("data"));
                return upgrade;
            }).orElse(Optional.empty());
        }
        return Optional.empty();
    }

    //---------- Upgrade Sets (the set of upgrades that can be applied to an upgradable item

    public void addUpgradesToSet(String setName, String... upgrades) {
        addUpgradesToSet(new UpgradeSet(setName).addUpgrades(upgrades));
    }

    public void addUpgradesToSet(UpgradeSet upgrades) {
        UpgradeSet set = possibleUpgradeLists.getOrDefault(upgrades.getName(), new UpgradeSet(upgrades.getName()));
        set.addUpgrades(upgrades);
        possibleUpgradeLists.put(set.getName(), set);
    }

    public Optional<UpgradeSet> getUpgradeSet(String name) {
        return Optional.ofNullable(possibleUpgradeLists.get(name));
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
