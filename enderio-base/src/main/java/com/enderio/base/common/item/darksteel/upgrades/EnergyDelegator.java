package com.enderio.base.common.item.darksteel.upgrades;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.darksteel.IDarkSteelUpgradable;
import com.enderio.core.common.capability.MultiCapabilityProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Optional;

public class EnergyDelegator implements IEnergyStorage , INBTSerializable<Tag>  {

    private final MultiCapabilityProvider prov;

    private static final EnergyStorage NULL_DELEGATE = new EnergyStorage(0);

    public EnergyDelegator(MultiCapabilityProvider prov) {
       this.prov = prov;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return getDelegate().receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return getDelegate().extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return getDelegate().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return getDelegate().getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return getDelegate().canExtract();
    }

    @Override
    public boolean canReceive() {
        return getDelegate().canReceive();
    }

    @Override
    public Tag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(Tag nbt) {

    }

    private IEnergyStorage getDelegate() {
//        return prov
//            .getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE)
//            .resolve()
//            .flatMap(upgradable -> upgradable.getUpgrade(EmpoweredUpgrade.class))
//            .map(energyUpgrade -> energyUpgrade.getStorage())
//            .orElse(NULL_DELEGATE);

        //        Optional<IDarkSteelUpgradable> a = prov.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).map(upgradable -> upgradable);
        //        Optional<EnergyUpgrade> b = a.flatMap(upgradable -> upgradable.getUpgrade(EnergyUpgrade.class));
        //        Optional<EnergyStorage> c = b.map(energyUpgrade -> energyUpgrade.getStorage());
        //        c.orElse(NULL_DELEGATE);

                Optional<IDarkSteelUpgradable> cap = prov.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).resolve();
                if(cap.isPresent()) {
                    Optional<EmpoweredUpgrade> energyUp = cap.get().getUpgrade(EmpoweredUpgrade.class);
                    if(energyUp.isPresent()) {
                        return energyUp.get().getStorage();
                    }
                }
                return NULL_DELEGATE;

    }




}
