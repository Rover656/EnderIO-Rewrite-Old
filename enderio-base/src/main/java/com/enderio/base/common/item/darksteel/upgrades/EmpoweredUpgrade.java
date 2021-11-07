package com.enderio.base.common.item.darksteel.upgrades;

import com.enderio.base.common.capability.darksteel.IDarkSteelUpgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.EnergyStorage;

import java.util.Random;
import java.util.function.Supplier;

public class EmpoweredUpgrade implements IDarkSteelUpgrade {

    //TODO: Config All the things
    public static final Supplier<EmpoweredUpgrade> EMPOWERED_1 = () -> new EmpoweredUpgrade(1,100000, 0.5f);
    public static final Supplier<EmpoweredUpgrade> EMPOWERED_2 = () -> new EmpoweredUpgrade(2,150000, 0.6f);
    public static final Supplier<EmpoweredUpgrade> EMPOWERED_3 = () -> new EmpoweredUpgrade(3,250000, 0.7f);
    public static final Supplier<EmpoweredUpgrade> EMPOWERED_4 = () -> new EmpoweredUpgrade(4,1000000,0.85f);

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
        this(0,0,0);
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

    public EnergyStorage getStorage() {
        return storage;
    }

    @Override
    public String getSerializedName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
        return getSerializedName() + " " + level;
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("level", level);
        nbt.putInt("maxStorage", maxStorage);
        nbt.putFloat("damageAbsorptionChance", damageAbsorptionChance);
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        if(tag instanceof CompoundTag nbt) {
            level = nbt.getInt("level");
            maxStorage = nbt.getInt("maxStorage");
            damageAbsorptionChance = nbt.getFloat("damageAbsorptionChance");
            storage = new EnergyStorage(maxStorage);
        }
    }


}
