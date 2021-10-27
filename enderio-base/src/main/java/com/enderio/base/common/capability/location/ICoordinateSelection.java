package com.enderio.base.common.capability.location;

import com.enderio.core.common.capability.INamedNBTSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

/**
 * You can access any variable as nonNull, under the condition, that isSet returns true. Otherwise it's an empty capability
 */
public interface ICoordinateSelection extends INamedNBTSerializable<Tag> {

    @Override
    default String getSerializedName() {
        return "CoordinateSelection";
    }

    BlockPos getPos();

    void setPos(BlockPos pos);

    static String getLevelName(ResourceLocation level) {
        return level.getNamespace().equals("minecraft") ? level.getPath() : level.toString();
    }

    default String getLevelName() {
        return getLevelName(getLevel());
    }

    ResourceLocation getLevel();

    /**
     * Only call on Serverside, the only Level the Client knows about is {@link net.minecraft.client.Minecraft#level}
     * @return the level of this Selection or null if no level is found
     */
    @Nullable
    default Level getLevelInstance() {
        return ServerLifecycleHooks.getCurrentServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, getLevel()));
    }

    default void setLevel(Level level) {
        setLevel(level.dimension().location());
    }

    void setLevel(ResourceLocation level);

    default void copyInto(ICoordinateSelection other) {
        setLevel(other.getLevel());
        setPos(other.getPos());
        set(other.isSet());
    }

    boolean isSet();

    void set(boolean isSet);
}
