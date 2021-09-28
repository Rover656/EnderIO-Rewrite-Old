package com.enderio.base;

import com.enderio.base.common.block.GraveBlock;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class EIOBlocks {
    private static final Registrate REGISTRATE = EnderIO.registrate();
    
    public static final BlockEntry<GraveBlock> GRAVE = blockBuilder("grave", new GraveBlock()).blockstate((con, prov) -> prov.simpleBlock(con.get(), prov.models().getExistingFile(new ResourceLocation(EnderIO.DOMAIN, "block/grave")))).register();
            
    public static <T extends Block> BlockBuilder<T, Registrate> blockBuilder(String name, T block) {
        return REGISTRATE.block(name,(p) -> block).item().group(new NonNullLazyValue<>(() -> EIOCreativeTabs.MATERIALS)).build();
    }
    
    public static void register() {}
}
