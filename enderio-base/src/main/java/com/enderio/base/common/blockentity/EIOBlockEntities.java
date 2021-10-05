package com.enderio.base.common.blockentity;

import com.enderio.base.EnderIO;
import com.enderio.base.common.block.EIOBlocks;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.TileEntityBuilder;
import com.tterrag.registrate.util.entry.TileEntityEntry;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EIOBlockEntities {
    private static final Registrate REGISTRATE = EnderIO.registrate();
    
    public static final TileEntityEntry<BlockEntity> GRAVE = REGISTRATE.tileEntity("grave", (s,p,t) -> new GraveBlockEntity(t, s, p)).validBlock(EIOBlocks.GRAVE).register();

    public static final TileEntityEntry<SinglePaintedBlockEntity> SINGLE_PAINTED = REGISTRATE.tileEntity("single_painted", (TileEntityBuilder.BlockEntityFactory<SinglePaintedBlockEntity>) (pos, state, type) -> new SinglePaintedBlockEntity(type, pos, state))
        .validBlocks(EIOBlocks.getPaintedSupplier().toArray(new NonNullSupplier[0])).register();
    public static final TileEntityEntry<DoublePaintedBlockEntity> DOUBLE_PAINTED = REGISTRATE.tileEntity("double_painted", (TileEntityBuilder.BlockEntityFactory<DoublePaintedBlockEntity>) (pos, state, type) -> new DoublePaintedBlockEntity(type, pos, state))
        .validBlocks(EIOBlocks.PAINTED_SLAB).register();

    public static void register() {}
}
