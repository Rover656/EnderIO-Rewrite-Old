package com.enderio.base.common.capability.capacitors;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.Constants;

public class CapacitorData implements ICapacitorData{
    private float base = 0;
    private Map<String, Float> specializations = new HashMap<>();
    
    public CapacitorData() {
        
    }

    public CapacitorData(float base, Map<String, Float> specializations) {
        this.base = base;
        this.specializations = specializations;
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("base", this.base);
        ListTag list = new ListTag();
        this.specializations.forEach((s,f) -> {
            CompoundTag entry = new CompoundTag();
            entry.putString("type", s);
            entry.putFloat("value", f);
            list.add(entry);
        });
        nbt.put("Specializations", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (nbt instanceof CompoundTag tag) {
            this.specializations.clear();
            this.base = tag.getFloat("base");
            ListTag List = tag.getList("Specializations", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < List.size(); i++) {
                addSpecialization(List.getCompound(i).getString("type"), List.getCompound(i).getFloat("value"));
            }
        }
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
    public void addNewSpecialization(String type, float modifier) {
        this.specializations.clear();
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
