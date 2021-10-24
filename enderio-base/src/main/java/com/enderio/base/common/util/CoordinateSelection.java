package com.enderio.base.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

/**
 * This class is in this package, because it's not only used by the item, but also by machines
 */

public class CoordinateSelection implements INBTSerializable<CompoundTag> {

    private BlockPos pos;
    private ResourceLocation level;

    public static CoordinateSelection of(BlockPos pos, Level level) {
        CoordinateSelection selection = new CoordinateSelection();
        selection.setLevel(level);
        selection.setPos(pos);
        return selection;
    }

    public static CoordinateSelection of(BlockPos pos, ResourceLocation level) {
        CoordinateSelection selection = new CoordinateSelection();
        selection.level = level;
        selection.setPos(pos);
        return selection;
    }

    public static CoordinateSelection of(CompoundTag nbt) {
        CoordinateSelection selection = new CoordinateSelection();
        selection.deserializeNBT(nbt);
        return selection;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public static String getLevelName(ResourceLocation level) {
        return level.getNamespace().equals("minecraft") ? level.getPath() : level.toString();
    }
    public String getLevelName() {
        return getLevelName(level);
    }

    public ResourceLocation getLevel() {
        return level;
    }

    /**
     * Only call on Serverside, the only Level the Client knows about is {@link net.minecraft.client.Minecraft#level}
     * @return the level of this Selection
     */
    public Level getLevelInstance() {
        return ServerLifecycleHooks.getCurrentServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, level));
    }

    public void setLevel(Level level) {
        this.level = level.dimension().location();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("level", level.toString());
        nbt.put("pos", NbtUtils.writeBlockPos(pos));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        level = new ResourceLocation(nbt.getString("level"));
        pos = NbtUtils.readBlockPos(nbt.getCompound("pos"));
    }
}
