package com.enderio.base;

import com.enderio.base.common.block.PaintedFenceBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.TileEntityBuilder;
import com.tterrag.registrate.util.entry.TileEntityEntry;

public class EIOMachinesBlockEntities {

    private static final Registrate REGISTRATE = EnderIO.registrate();

    public static final TileEntityEntry<PaintedFenceBlockEntity> PAINTED_FENCE = REGISTRATE.tileEntity("painted_fence", (TileEntityBuilder.BlockEntityFactory<PaintedFenceBlockEntity>) (pos, state, type) -> new PaintedFenceBlockEntity(type, pos, state)).validBlock(
        EIOBlocks.PAINTED_FENCE).register();

    public static void register() {
    }
}
