package com.enderio.base.common.capability.location;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * This class is in this package, because it's not only used by the item, but also by machines
 */

public class CoordinateSelection implements ICoordinateSelection {

    private BlockPos pos = BlockPos.ZERO;
    private ResourceLocation level = new ResourceLocation("", "");

    private boolean isSet = false;

    public static CoordinateSelection of(BlockPos pos, Level level) {
        CoordinateSelection selection = new CoordinateSelection();
        selection.setLevel(level);
        selection.setPos(pos);
        selection.isSet = true;
        return selection;
    }

    public static CoordinateSelection of(BlockPos pos, ResourceLocation level) {
        CoordinateSelection selection = new CoordinateSelection();
        selection.level = level;
        selection.setPos(pos);
        selection.isSet = true;
        return selection;
    }

    @Override
    public void set(boolean isSet) {
        this.isSet = isSet;
    }

    @Override
    public boolean isSet() {
        return isSet;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public ResourceLocation getLevel() {
        return level;
    }

    @Override
    public void setLevel(ResourceLocation level) {
        this.level = level;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (isSet) {
            nbt.putString("level", level.toString());
            nbt.put("pos", NbtUtils.writeBlockPos(pos));
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        if (tag instanceof CompoundTag nbt && !nbt.isEmpty()) {
            level = new ResourceLocation(nbt.getString("level"));
            pos = NbtUtils.readBlockPos(nbt.getCompound("pos"));
            isSet = true;
        }
    }
}
