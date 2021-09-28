package com.enderio.base;

import com.enderio.base.common.block.GraveBlock;
import com.enderio.base.common.util.BlockStateUtils;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.Block;

public class EIOBlocks {
    private static final Registrate REGISTRATE = EnderIO.registrate();
    
    public static final BlockEntry<GraveBlock> GRAVE = blockBuilder("grave", new GraveBlock()).blockstate((c,p) -> BlockStateUtils.graveState(c, p)).register();
            
    public static <T extends Block> BlockBuilder<T, Registrate> blockBuilder(String name, T block) {
        return REGISTRATE.block(name,(p) -> block);
    }
    
    public static void register() {}
}
