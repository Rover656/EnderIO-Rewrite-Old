package com.enderio.base;

import com.enderio.base.common.block.painted.PaintedFenceBlock;
import com.enderio.base.common.block.painted.PaintedFenceGateBlock;
import com.enderio.base.common.block.painted.PaintedSandBlock;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class EIOBlocks {
    private static final Registrate REGISTRATE = EnderIO.registrate();


    public static final BlockEntry<PaintedFenceBlock> PAINTED_FENCE = REGISTRATE.block("painted_fence", PaintedFenceBlock::new).blockstate((ctx, prov) -> {}).tag(BlockTags.WOODEN_FENCES).simpleItem().lang("Painted Fence").register();
    public static final BlockEntry<PaintedFenceGateBlock> PAINTED_FENCE_GATE = REGISTRATE.block("painted_fence_gate", PaintedFenceGateBlock::new).blockstate((ctx, prov) -> {}).tag(BlockTags.FENCE_GATES).simpleItem().lang("Painted Fence Gate").register();
    public static final BlockEntry<PaintedSandBlock> PAINTED_SAND = REGISTRATE.block("painted_sand", PaintedSandBlock::new).blockstate((ctx, prov) -> {}).properties(BlockBehaviour.Properties::noOcclusion).tag(BlockTags.SAND).simpleItem().lang("Painted Sand").register();

    public static void register() {

    }
}
