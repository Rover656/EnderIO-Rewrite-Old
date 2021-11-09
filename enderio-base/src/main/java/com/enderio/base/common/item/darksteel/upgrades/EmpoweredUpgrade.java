package com.enderio.base.common.item.darksteel.upgrades;

import com.enderio.base.common.capability.darksteel.IDarkSteelUpgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.EnergyStorage;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class EmpoweredUpgrade implements IDarkSteelUpgrade {

    //TODO: Config All the things
    private static final Supplier<EmpoweredUpgrade>[] UPGRADES = new Supplier[] {
        () -> new EmpoweredUpgrade(0,100000, 0.5f),
        () -> new EmpoweredUpgrade(1,150000, 0.6f),
        () -> new EmpoweredUpgrade(2,250000, 0.7f),
        () -> new EmpoweredUpgrade(3,1000000,0.85f)
    };

    public static Optional<EmpoweredUpgrade> getUpgradeForTier(int tier) {
        if(tier < 0 || tier >= UPGRADES.length) {
            return Optional.empty();
        }
        return Optional.of(UPGRADES[tier].get());
    }

    public static final String NAME = "Empowered";

    private static final Random RANDOM = new Random();

    //TODO: Config
    private int obsianBreakPowerUse = 50;

    //TODO: Config
    private int speedBoostWhenPowered = 2;

    //TODO: Config
    private int speedBoostWhenObsidian = 50;

    //TODO: Config
    private int powerUsePerDamagePoint = 750;

    private int level;

    private int maxStorage;

    private float damageAbsorptionChance;

    private EnergyStorage storage;


    public EmpoweredUpgrade() {
        this(1,100000,0.5f);
    }

    public EmpoweredUpgrade(int level, int  maxStorage, float damageAbsorptionChance) {
        this.level = level;
        this.maxStorage = maxStorage;
        this.damageAbsorptionChance = damageAbsorptionChance;
        storage = new EnergyStorage(maxStorage);
    }

    public float adjustDestroySpeed(float speed, BlockState pState) {
        if (storage.getEnergyStored() > 0) {
            speed += speedBoostWhenPowered;
        }
        if (useObsidianMining(pState)) {
            speed += speedBoostWhenObsidian;
        }
        return speed;
    }

    public int adjustDamage(int oldDamage, int newDamage) {
        int damageTaken = newDamage - oldDamage;
        if (damageTaken > 0 && storage.getEnergyStored() > 0 && RANDOM.nextDouble() < damageAbsorptionChance) {
            storage.extractEnergy(damageTaken * powerUsePerDamagePoint, false);
            return oldDamage;
        }
        return newDamage;
    }

    public void onMineBlock(BlockState pState) {
        if(useObsidianMining(pState)) {
           storage.extractEnergy(obsianBreakPowerUse, false);
        }
    }

    private boolean useObsidianMining(BlockState pState) {
        //TODO: Check for blocks with hardness > 50 as well as obsidian
        return storage.getEnergyStored() >= obsianBreakPowerUse && pState.getBlock() == Blocks.OBSIDIAN;
    }

    @Override
    public Optional<? extends IDarkSteelUpgrade> getNextTier() {
        return getUpgradeForTier(level + 1);
    }

    public EnergyStorage getStorage() {
        return storage;
    }

    @Override
    public String getSerializedName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return getSerializedName() + " " + (level + 1);
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("level", level);
        nbt.put("storage", storage.serializeNBT());
        nbt.putInt("maxStorage", maxStorage);
        nbt.putFloat("damageAbsorptionChance", damageAbsorptionChance);
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        if(tag instanceof CompoundTag nbt) {
            level = nbt.getInt("level");
            maxStorage = nbt.getInt("maxStorage");
            storage = new EnergyStorage(maxStorage);
            storage.deserializeNBT(nbt.get("storage"));
            damageAbsorptionChance = nbt.getFloat("damageAbsorptionChance");
        }
    }


}
