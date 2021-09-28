package com.enderio.base;

import com.enderio.base.common.block.painted.SinglePaintedBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.TileEntityBuilder;
import com.tterrag.registrate.util.entry.TileEntityEntry;

public class EIOBlockEntities {

    private static final Registrate REGISTRATE = EnderIO.registrate();

    public static final TileEntityEntry<SinglePaintedBlockEntity> SINGLE_PAINTED = REGISTRATE.tileEntity("single_painted", (TileEntityBuilder.BlockEntityFactory<SinglePaintedBlockEntity>) (pos, state, type) -> new SinglePaintedBlockEntity(type, pos, state)).validBlocks(
        EIOBlocks.PAINTED_FENCE, EIOBlocks.PAINTED_FENCE_GATE, EIOBlocks.PAINTED_STAIRS, EIOBlocks.PAINTED_CRAFTING_TABLE, EIOBlocks.PAINTED_SAND).register();


    public static void register() {
    }
}
