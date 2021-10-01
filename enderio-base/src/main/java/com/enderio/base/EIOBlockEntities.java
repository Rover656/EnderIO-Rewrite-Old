package com.enderio.base;

import com.enderio.base.common.block.painted.DoublePaintedBlockEntity;
import com.enderio.base.common.block.painted.SinglePaintedBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.TileEntityBuilder;
import com.tterrag.registrate.util.entry.TileEntityEntry;

public class EIOBlockEntities {

    private static final Registrate REGISTRATE = EnderIO.registrate();

    public static final TileEntityEntry<SinglePaintedBlockEntity> SINGLE_PAINTED = REGISTRATE.tileEntity("single_painted", (TileEntityBuilder.BlockEntityFactory<SinglePaintedBlockEntity>) (pos, state, type) -> new SinglePaintedBlockEntity(type, pos, state))
        .validBlocks(EIOBlocks.getPaintedSupplier()).register();
    public static final TileEntityEntry<DoublePaintedBlockEntity> DOUBLE_PAINTED = REGISTRATE.tileEntity("double_painted", (TileEntityBuilder.BlockEntityFactory<DoublePaintedBlockEntity>) (pos, state, type) -> new DoublePaintedBlockEntity(type, pos, state))
        .validBlocks(EIOBlocks.getDoublePaintedSupplier()).register();


    public static void register() {
    }
}
