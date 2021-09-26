package com.enderio.machines.common;

import com.enderio.machines.EIOMachinesBlocks;
import com.enderio.machines.EnderIOMachines;
import com.enderio.machines.common.block.PaintedFenceBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.TileEntityBuilder;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EIOMachinesBlockEntities {

    private static final Registrate REGISTRATE = EnderIOMachines.registrate();

    public static final TileEntityEntry<PaintedFenceBlockEntity> PAINTED_FENCE = REGISTRATE.tileEntity("painted_fence", (TileEntityBuilder.BlockEntityFactory<PaintedFenceBlockEntity>) (pos, state, type) -> new PaintedFenceBlockEntity(type, pos, state)).validBlock(
        EIOMachinesBlocks.PAINTED_FENCE).register();

    public static void register() {

    }
}
