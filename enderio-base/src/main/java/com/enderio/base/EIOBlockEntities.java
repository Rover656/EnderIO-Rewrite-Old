package com.enderio.base;

import com.enderio.base.common.blockentity.GraveBE;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.TileEntityEntry;

import net.minecraft.world.level.block.entity.BlockEntity;

public class EIOBlockEntities {
    private static final Registrate REGISTRATE = EnderIO.registrate();
    
    public static final TileEntityEntry<BlockEntity> GRAVEBE = REGISTRATE.tileEntity("gravebe", (s,p,t) -> new GraveBE(t, s, p)).validBlock(EIOBlocks.GRAVE).register();
    
    public static void register() {}
}
