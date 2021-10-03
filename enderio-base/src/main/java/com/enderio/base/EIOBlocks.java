package com.enderio.base;

import com.enderio.base.common.block.painted.*;
import com.enderio.base.common.item.PaintedSlabBlockItem;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Arrays;

public class EIOBlocks {
    private static final Registrate REGISTRATE = EnderIO.registrate();

    private static NonNullSupplier<Block>[] painted;
    private static NonNullSupplier<Block>[] doublePainted;

    public static final BlockEntry<PaintedFenceBlock> PAINTED_FENCE = REGISTRATE.block("painted_fence", PaintedFenceBlock::new).initialProperties(() -> Blocks.OAK_FENCE).loot((loot, block) -> {}).blockstate((ctx, prov) -> {}).tag(BlockTags.WOODEN_FENCES).simpleItem().lang("Painted Fence").register();
    public static final BlockEntry<PaintedFenceGateBlock> PAINTED_FENCE_GATE = REGISTRATE.block("painted_fence_gate", PaintedFenceGateBlock::new).initialProperties(() -> Blocks.OAK_FENCE_GATE).loot((loot, block) -> {}).blockstate((ctx, prov) -> {}).tag(BlockTags.FENCE_GATES).simpleItem().lang("Painted Fence Gate").register();
    public static final BlockEntry<PaintedSandBlock> PAINTED_SAND = REGISTRATE.block("painted_sand", PaintedSandBlock::new).blockstate((ctx, prov) -> {}).initialProperties(() -> Blocks.SAND).loot((loot, block) -> {}).properties(BlockBehaviour.Properties::noOcclusion).tag(BlockTags.SAND).simpleItem().lang("Painted Sand").register();
    public static final BlockEntry<PaintedStairBlock> PAINTED_STAIRS = REGISTRATE.block("painted_stairs", PaintedStairBlock::new).blockstate((ctx, prov) -> {}).initialProperties(() -> Blocks.OAK_STAIRS).loot((loot, block) -> {}).properties(BlockBehaviour.Properties::noOcclusion).tag(BlockTags.WOODEN_STAIRS).simpleItem().lang("Painted Stair").register();
    public static final BlockEntry<PaintedCraftingTableBlock> PAINTED_CRAFTING_TABLE = REGISTRATE.block("painted_crafting_table", PaintedCraftingTableBlock::new).blockstate((ctx, prov) -> {}).initialProperties(() -> Blocks.CRAFTING_TABLE).loot((loot, block) -> {}).properties(BlockBehaviour.Properties::noOcclusion).simpleItem().lang("Painted Crafting Table").register();
    public static final BlockEntry<PaintedRedstoneBlock> PAINTED_REDSTONE_BLOCK = REGISTRATE.block("painted_redstone_block", PaintedRedstoneBlock::new).blockstate((ctx, prov) -> {}).initialProperties(() -> Blocks.REDSTONE_BLOCK).loot((loot, block) -> {}).properties(BlockBehaviour.Properties::noOcclusion).simpleItem().lang("Painted Redstone Block").register();
    public static final BlockEntry<PaintedTrapDoorBlock> PAINTED_TRAPDOOR = REGISTRATE.block("painted_trapdoor", PaintedTrapDoorBlock::new).blockstate((ctx, prov) -> {}).initialProperties(() -> Blocks.OAK_TRAPDOOR).loot((loot, block) -> {}).loot((loot, block) -> {}).properties(BlockBehaviour.Properties::noOcclusion).simpleItem().lang("Painted Trapdoor").register();
    public static final BlockEntry<PaintedWoodenPressurePlateBlock> PAINTED_WOODEN_PRESSURE_PLATE = REGISTRATE.block("painted_wooden_pressure_plate", PaintedWoodenPressurePlateBlock::new).blockstate((ctx, prov) -> {}).initialProperties(() -> Blocks.OAK_PRESSURE_PLATE).loot((loot, block) -> {}).properties(BlockBehaviour.Properties::noOcclusion).simpleItem().lang("Painted Wooden Pressure Plate").register();
    public static final BlockEntry<PaintedSlabBlock> PAINTED_SLAB = REGISTRATE.block("painted_slab", PaintedSlabBlock::new).blockstate((ctx, prov) -> {}).initialProperties(() -> Blocks.OAK_SLAB).loot((loot, block) -> {}).properties(BlockBehaviour.Properties::noOcclusion).lang("Painted Slab").item(PaintedSlabBlockItem::new).build().register();
    public static final BlockEntry<SinglePaintedBlock> PAINTED_GLOWSTONE = REGISTRATE.block("painted_glowstone", SinglePaintedBlock::new).blockstate((ctx, prov) -> {}).initialProperties(() -> Blocks.GLOWSTONE).loot((loot, block) -> {}).properties(BlockBehaviour.Properties::noOcclusion).lang("Painted Glowstone").simpleItem().register();


    public static Block[] getPainted() {
        return Arrays.stream(painted).map(NonNullSupplier::get).toArray(Block[]::new);
    }
    public static Block[] getDoublePainted() {
        return Arrays.stream(doublePainted).map(NonNullSupplier::get).toArray(Block[]::new);
    }
    public static NonNullSupplier<Block>[] getPaintedSupplier() {
        if (painted == null)
            painted = new NonNullSupplier[]{EIOBlocks.PAINTED_FENCE, EIOBlocks.PAINTED_FENCE_GATE, EIOBlocks.PAINTED_SAND, EIOBlocks.PAINTED_STAIRS, EIOBlocks.PAINTED_CRAFTING_TABLE, EIOBlocks.PAINTED_REDSTONE_BLOCK, EIOBlocks.PAINTED_TRAPDOOR, EIOBlocks.PAINTED_WOODEN_PRESSURE_PLATE, EIOBlocks.PAINTED_GLOWSTONE};
        return painted;
    }
    public static NonNullSupplier<Block>[] getDoublePaintedSupplier() {
        if (doublePainted == null)
            doublePainted = new NonNullSupplier[]{EIOBlocks.PAINTED_SLAB};
        return doublePainted;
    }

    public static void register() {
    }
}
