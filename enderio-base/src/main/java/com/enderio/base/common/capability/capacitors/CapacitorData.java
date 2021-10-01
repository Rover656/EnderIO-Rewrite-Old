package com.enderio.base.common.capability.capacitors;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class CapacitorData implements ICapacitorData{
    private float base = 0;
    private Map<String, Float> specializations = new HashMap<>();
    

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
    public void setBase(float base) {
        this.base = base;
    }

    @Override
    public float getBase() {
        return this.base;
    }

    @Override
    public void addSpecialization(String type, float modifier) {
        this.specializations.put(type, modifier);
    }

    @Override
    public void addAllSpecialization(Map<String, Float> specializations) {
        this.specializations.putAll(specializations);
    }

    @Override
    public Map<String, Float> getSpecializations() {
        return this.specializations;
    }
}
