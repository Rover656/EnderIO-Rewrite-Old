package com.enderio.machines;

import com.enderio.machines.common.block.PaintedFenceBlock;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;

@SuppressWarnings("unused")
public class EIOMachinesBlocks {
    private static final Registrate REGISTRATE = EnderIOMachines.registrate();


    // region Gears

    public static final BlockEntry<PaintedFenceBlock> PAINTED_FENCE = REGISTRATE.block("painted_fence", PaintedFenceBlock::new).tag(BlockTags.WOODEN_FENCES).simpleItem().lang("Painted Fence").register();



    public static void register() {}
}
