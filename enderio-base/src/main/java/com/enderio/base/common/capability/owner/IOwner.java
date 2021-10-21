package com.enderio.base.common.capability.owner;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

// A capability for the Grave BE
public interface IOwner extends INBTSerializable<CompoundTag> {

    GameProfile getProfile();

    void setProfile(GameProfile profile);

}
