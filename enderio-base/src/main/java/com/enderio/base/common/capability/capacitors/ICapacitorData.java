package com.enderio.base.common.capability.capacitors;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapacitorData extends INBTSerializable<Tag>{
    
    MachinePropertie getMachinePropertie();

}
