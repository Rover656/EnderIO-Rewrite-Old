package com.enderio.base.common.capability.capacitors;

import java.util.Map;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapacitorData extends INBTSerializable<Tag> {
    static final String ALL_ENERGY_CONSUMPSTION = "all_energy_consuption";
    static final String ALL_PRODUCTION_SPEED = "all_production_speed";
    
    static final String ALLOY_ENERGY_CONSUMPSTION = "alloy_energy_consuption";
    static final String ALLOY_PRODUCTION_SPEED = "alloy_production_speed";

    void setBase(float base);

    float getBase();

    void addSpecialization(String type, float modifier);
    
    void addNewSpecialization(String type, float modifier);

    void addAllSpecialization(Map<String, Float> specializations);

    Map<String, Float> getSpecializations();

}
