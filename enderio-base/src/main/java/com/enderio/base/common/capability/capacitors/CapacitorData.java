package com.enderio.base.common.capability.capacitors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class CapacitorData implements ICapacitorData{
    private MachinePropertie machinePropertie;
    
    public CapacitorData() {
        
    }
    
    public CapacitorData(MachinePropertie machinePropertie) {
        this.machinePropertie = machinePropertie;
    }
    
    public void update(MachinePropertie machinePropertie) {
        this.machinePropertie = machinePropertie;
    }

    @Override
    public Tag serializeNBT() {
        // TODO Auto-generated method stub
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public MachinePropertie getMachinePropertie() {
        // TODO Auto-generated method stub
        return null;
    }

}
