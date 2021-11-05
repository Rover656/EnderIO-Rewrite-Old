package com.enderio.base.common.item.darksteel.upgrades;

import com.enderio.base.common.capability.darksteel.IDarkSteelUpgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;

import java.util.function.Supplier;

public class EmpoweredUpgrade implements IDarkSteelUpgrade {

    public static final Supplier<EmpoweredUpgrade> EMPOWERED_1 = () -> new EmpoweredUpgrade(1,100000);
    public static final Supplier<EmpoweredUpgrade> EMPOWERED_2 = () -> new EmpoweredUpgrade(2,150000);
    public static final Supplier<EmpoweredUpgrade> EMPOWERED_3 = () -> new EmpoweredUpgrade(3,250000);
    public static final Supplier<EmpoweredUpgrade> EMPOWERED_4 = () -> new EmpoweredUpgrade(4,1000000);
//
//    private static int[] LEVELS = new int[] {
//       100000,150000,250000,1000000
//    };
//
//    public static EmpoweredUpgrade create(int level) {
//        return new EmpoweredUpgrade(level,LEVELS[level]);
//    }


    public static final String NAME = "Empowered";

    private int level;

    private int maxStorage;

    private EnergyStorage storage;


    public EmpoweredUpgrade() {
        this(0,0);
    }

    public EmpoweredUpgrade(int level, int  maxStorage) {
        this.level = level;
        this.maxStorage = maxStorage;
        storage = new EnergyStorage(maxStorage);
    }

    public int getLevel() {
        return level;
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
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        if(tag instanceof CompoundTag nbt) {
            level = nbt.getInt("level");
            maxStorage = nbt.getInt("maxStorage");
            storage = new EnergyStorage(maxStorage);
        }
    }
}
