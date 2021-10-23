package com.enderio.base.common.capability.owner;

import com.enderio.core.common.capability.INamedNBTSerializable;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

// A capability for the Grave BE
public interface IOwner extends INamedNBTSerializable<CompoundTag> {

    @Override
    default String getSerializedName() {
        return "Owner";
    }

    GameProfile getProfile();

    default void setProfile(GameProfile profile) {
        setProfile(profile, prof -> {});
    }

    void setProfile(GameProfile profile, ProfileSetCallback callback);

    interface ProfileSetCallback {
        void profileSet(GameProfile profile);
    }

}
