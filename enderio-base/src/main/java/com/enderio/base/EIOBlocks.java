package com.enderio.base;

import com.enderio.base.common.block.PaintedFenceBlock;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;

public class EIOBlocks {
    private static final Registrate REGISTRATE = EnderIO.registrate();


    public static final BlockEntry<PaintedFenceBlock> PAINTED_FENCE = REGISTRATE.block("painted_fence", PaintedFenceBlock::new).tag(BlockTags.WOODEN_FENCES).simpleItem().lang("Painted Fence").register();

    public static void register() {

    }
}
