package com.enderio.base.common.block;

import com.enderio.base.EnderIO;
import com.enderio.base.common.block.glass.GlassBlocks;
import com.enderio.base.common.block.glass.GlassCollisionPredicate;
import com.enderio.base.common.item.EIOCreativeTabs;
import com.enderio.base.data.model.block.BlockStateUtils;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

@SuppressWarnings("unused")
public class EIOBlocks {
    private static final Registrate REGISTRATE = EnderIO.registrate();

    // region Alloy Blocks

    public static final BlockEntry<Block> ELECTRICAL_STEEL_BLOCK = metalBlock("electrical_steel_block").register();
    public static final BlockEntry<Block> ENERGETIC_ALLOY_BLOCK = metalBlock("energetic_alloy_block").register();
    public static final BlockEntry<Block> VIBRANT_ALLOY_BLOCK = metalBlock("vibrant_alloy_block").register();
    public static final BlockEntry<Block> REDSTONE_ALLOY_BLOCK = metalBlock("redstone_alloy_block").register();
    public static final BlockEntry<Block> CONDUCTIVE_IRON_BLOCK = metalBlock("conductive_iron_block").register();
    public static final BlockEntry<Block> PULSATING_IRON_BLOCK = metalBlock("pulsating_iron_block").register();
    public static final BlockEntry<Block> DARK_STEEL_BLOCK = metalBlock("dark_steel_block").register();
    public static final BlockEntry<Block> SOULARIUM_BLOCK = metalBlock("soularium_block").register();
    public static final BlockEntry<Block> END_STEEL_BLOCK = metalBlock("end_steel_block").register();
    public static final BlockEntry<Block> CONSTRUCTION_ALLOY_BLOCK = metalBlock("construction_alloy_block").register();

    // endregion

    // region Chassis

    public static final BlockEntry<Block> SIMPLE_MACHINE_CHASSIS = chassisBlock("simple_machine_chassis").register();

    public static final BlockEntry<Block> INDUSTRIAL_MACHINE_CHASSIS = chassisBlock("industrial_machine_chassis").register();

    public static final BlockEntry<Block> END_STEEL_MACHINE_CHASSIS = chassisBlock("end_steel_machine_chassis")
        .lang("End Steel Chassis")
        .register();

    public static final BlockEntry<Block> SOUL_MACHINE_CHASSIS = chassisBlock("soul_machine_chassis").register();

    public static final BlockEntry<Block> ENHANCED_MACHINE_CHASSIS = chassisBlock("enhanced_machine_chassis").register();

    public static final BlockEntry<Block> SOULLESS_MACHINE_CHASSIS = chassisBlock("soulless_machine_chassis").register();

    // endregion

    // region Dark Steel Building Blocks

    // TODO: FASTER THAN REGULAR LADDERS TOOLTIP
    public static final BlockEntry<DarkSteelLadderBlock> DARK_STEEL_LADDER = REGISTRATE
        .block("dark_steel_ladder", Material.METAL, DarkSteelLadderBlock::new)
        .properties(props -> props
            .strength(0.4f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .noOcclusion())
        .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.get(), prov
            .models()
            .withExistingParent(ctx.getName(), prov.mcLoc("block/ladder"))
            .texture("particle", prov.blockTexture(ctx.get()))
            .texture("texture", prov.blockTexture(ctx.get()))))
        .tag(BlockTags.CLIMBABLE)
        .item()
        .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("block/dark_steel_ladder")))
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
        .build()
        .register();

    public static final BlockEntry<IronBarsBlock> DARK_STEEL_BARS = REGISTRATE
        .block("dark_steel_bars", IronBarsBlock::new)
        .properties(props -> props
            .strength(5.0f, 1000.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .noOcclusion())
        .blockstate(BlockStateUtils::paneBlock)
        .addLayer(() -> RenderType::cutoutMipped)
        .item()
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
        .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("block/dark_steel_bars")))
        .build()
        .register();

    // TODO: Door drops itself in creative????
    public static final BlockEntry<DoorBlock> DARK_STEEL_DOOR = REGISTRATE
        .block("dark_steel_door", Material.METAL, DoorBlock::new)
        .properties(props -> props
            .strength(5.0f, 2000.0f)
            .sound(SoundType.METAL)
            .noOcclusion())
        .blockstate((ctx, prov) -> prov.doorBlock(ctx.get(), prov.modLoc("block/dark_steel_door_bottom"), prov.modLoc("block/dark_steel_door_top")))
        .addLayer(() -> RenderType::cutout)
        .item()
        .model((ctx, prov) -> prov.generated(ctx))
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
        .build()
        .register();

    public static final BlockEntry<TrapDoorBlock> DARK_STEEL_TRAPDOOR = REGISTRATE
        .block("dark_steel_trapdoor", Material.METAL, TrapDoorBlock::new)
        .properties(props -> props
            .strength(5.0f, 2000.0f)
            .sound(SoundType.METAL)
            .noOcclusion())
        .blockstate((ctx, prov) -> prov.trapdoorBlock(ctx.get(), prov.modLoc("block/dark_steel_trapdoor"), true))
        .addLayer(() -> RenderType::cutout)
        .item()
        .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), prov.modLoc("block/dark_steel_trapdoor_bottom")))
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
        .build()
        .register();

    public static final BlockEntry<IronBarsBlock> END_STEEL_BARS = REGISTRATE
        .block("end_steel_bars", IronBarsBlock::new)
        .blockstate(BlockStateUtils::paneBlock)
        .addLayer(() -> RenderType::cutoutMipped)
        .item()
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
        .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("block/end_steel_bars")))
        .build()
        .register();

    // endregion

    // region Fused Quartz/Glass

    public static final GlassBlocks FUSED_QUARTZ = new GlassBlocks(REGISTRATE, "fused_quartz", "Fused Quartz", GlassCollisionPredicate.NONE, false, false,
        true);
    public static final GlassBlocks ENLIGHTENED_FUSED_QUARTZ = new GlassBlocks(REGISTRATE, "fused_quartz_e", "Enlightened Fused Quartz",
        GlassCollisionPredicate.NONE, true, false, true);
    public static final GlassBlocks DARK_FUSED_QUARTZ = new GlassBlocks(REGISTRATE, "fused_quartz_d", "Dark Fused Quartz", GlassCollisionPredicate.NONE, false,
        true, true);

    public static final GlassBlocks FUSED_QUARTZ_PLAYERS_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_p", "Fused Quartz",
        GlassCollisionPredicate.PLAYERS_PASS, false, false, false);
    public static final GlassBlocks ENLIGHTENED_FUSED_QUARTZ_PLAYERS_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_ep", "Enlightened Fused Quartz",
        GlassCollisionPredicate.PLAYERS_PASS, true, false, true);
    public static final GlassBlocks DARK_FUSED_QUARTZ_PLAYERS_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_dp", "Dark Fused Quartz",
        GlassCollisionPredicate.PLAYERS_PASS, false, true, true);

    public static final GlassBlocks FUSED_QUARTZ_MOBS_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_m", "Fused Quartz", GlassCollisionPredicate.MOBS_PASS,
        false, false, false);
    public static final GlassBlocks ENLIGHTENED_FUSED_QUARTZ_MOBS_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_em", "Enlightened Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, true, false, true);
    public static final GlassBlocks DARK_FUSED_QUARTZ_MOBS_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_dm", "Dark Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, false, true, true);

    public static final GlassBlocks FUSED_QUARTZ_ANIMAL_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_a", "Fused Quartz", GlassCollisionPredicate.MOBS_PASS,
        false, false, false);
    public static final GlassBlocks ENLIGHTENED_FUSED_QUARTZ_ANIMAL_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_ea", "Enlightened Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, true, false, true);
    public static final GlassBlocks DARK_FUSED_QUARTZ_ANIMAL_PASS = new GlassBlocks(REGISTRATE, "fused_quartz_da", "Dark Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, false, true, true);

    public static final GlassBlocks FUSED_QUARTZ_PLAYER_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_np", "Fused Quartz",
        GlassCollisionPredicate.PLAYERS_BLOCK, false, false, false);
    public static final GlassBlocks ENLIGHTENED_FUSED_QUARTZ_PLAYER_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_enp", "Enlightened Fused Quartz",
        GlassCollisionPredicate.PLAYERS_BLOCK, true, false, true);
    public static final GlassBlocks DARK_FUSED_QUARTZ_PLAYER_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_dnp", "Dark Fused Quartz",
        GlassCollisionPredicate.PLAYERS_BLOCK, false, true, true);

    public static final GlassBlocks FUSED_QUARTZ_MONSTER_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_nm", "Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, false, false, false);
    public static final GlassBlocks ENLIGHTENED_FUSED_QUARTZ_MONSTER_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_enm", "Enlightened Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, true, false, true);
    public static final GlassBlocks DARK_FUSED_QUARTZ_MONSTER_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_dnm", "Dark Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, false, true, true);

    public static final GlassBlocks FUSED_QUARTZ_ANIMAL_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_na", "Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, false, false, false);
    public static final GlassBlocks ENLIGHTENED_FUSED_QUARTZ_ANIMAL_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_ena", "Enlightened Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, true, false, true);
    public static final GlassBlocks DARK_FUSED_QUARTZ_ANIMAL_BLOCK = new GlassBlocks(REGISTRATE, "fused_quartz_dna", "Dark Fused Quartz",
        GlassCollisionPredicate.MOBS_PASS, false, true, true);

    public static final GlassBlocks QUITE_CLEAR_GLASS = new GlassBlocks(REGISTRATE, "clear_glass", "Quite Clear Glass", GlassCollisionPredicate.NONE, false,
        false, true);
    public static final GlassBlocks ENLIGHTENED_CLEAR_GLASS = new GlassBlocks(REGISTRATE, "enlightened_clear_glass", "Enlightened Clear Glass",
        GlassCollisionPredicate.NONE, true, false, true);
    public static final GlassBlocks DARK_CLEAR_GLASS = new GlassBlocks(REGISTRATE, "dark_clear_glass", "Dark Clear Glass", GlassCollisionPredicate.NONE, true,
        false, true);

    public static final GlassBlocks QUITE_CLEAR_GLASS_PLAYERS_PASS = new GlassBlocks(REGISTRATE, "clear_glass_p", "Quite Clear Glass",
        GlassCollisionPredicate.PLAYERS_PASS, false, false, false);
    public static final GlassBlocks ENLIGHTENED_CLEAR_GLASS_PLAYERS_PASS = new GlassBlocks(REGISTRATE, "clear_glass_ep", "Enlightened Clear Glass",
        GlassCollisionPredicate.PLAYERS_PASS, true, false, true);
    public static final GlassBlocks DARK_CLEAR_GLASS_PLAYERS_PASS = new GlassBlocks(REGISTRATE, "clear_glass_dp", "Dark Clear Glass",
        GlassCollisionPredicate.PLAYERS_PASS, true, false, true);

    public static final GlassBlocks QUITE_CLEAR_GLASS_MOBS_PASS = new GlassBlocks(REGISTRATE, "clear_glass_m", "Quite Clear Glass",
        GlassCollisionPredicate.MOBS_PASS, false, false, false);
    public static final GlassBlocks ENLIGHTENED_CLEAR_GLASS_MOBS_PASS = new GlassBlocks(REGISTRATE, "clear_glass_em", "Enlightened Clear Glass",
        GlassCollisionPredicate.MOBS_PASS, true, false, true);
    public static final GlassBlocks DARK_CLEAR_GLASS_MOBS_PASS = new GlassBlocks(REGISTRATE, "clear_glass_dm", "Dark Clear Glass",
        GlassCollisionPredicate.MOBS_PASS, true, false, true);

    public static final GlassBlocks QUITE_CLEAR_GLASS_ANIMAL_PASS = new GlassBlocks(REGISTRATE, "clear_glass_a", "Quite Clear Glass",
        GlassCollisionPredicate.ANIMALS_PASS, false, false, false);
    public static final GlassBlocks ENLIGHTENED_CLEAR_GLASS_ANIMAL_PASS = new GlassBlocks(REGISTRATE, "clear_glass_ea", "Enlightened Clear Glass",
        GlassCollisionPredicate.ANIMALS_PASS, true, false, true);
    public static final GlassBlocks DARK_CLEAR_GLASS_ANIMAL_PASS = new GlassBlocks(REGISTRATE, "clear_glass_da", "Dark Clear Glass",
        GlassCollisionPredicate.ANIMALS_PASS, true, false, true);

    public static final GlassBlocks QUITE_CLEAR_GLASS_PLAYER_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_np", "Quite Clear Glass",
        GlassCollisionPredicate.PLAYERS_BLOCK, false, false, false);
    public static final GlassBlocks ENLIGHTENED_CLEAR_GLASS_PLAYER_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_enp", "Enlightened Clear Glass",
        GlassCollisionPredicate.PLAYERS_BLOCK, true, false, true);
    public static final GlassBlocks DARK_CLEAR_GLASS_PLAYER_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_dnp", "Dark Clear Glass",
        GlassCollisionPredicate.PLAYERS_BLOCK, true, false, true);

    public static final GlassBlocks QUITE_CLEAR_GLASS_MONSTER_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_nm", "Quite Clear Glass",
        GlassCollisionPredicate.MOBS_PASS, false, false, false);
    public static final GlassBlocks ENLIGHTENED_CLEAR_GLASS_MONSTER_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_enm", "Enlightened Clear Glass",
        GlassCollisionPredicate.MOBS_PASS, true, false, true);
    public static final GlassBlocks DARK_CLEAR_GLASS_MONSTER_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_dnm", "Dark Clear Glass",
        GlassCollisionPredicate.MOBS_PASS, true, false, true);

    public static final GlassBlocks QUITE_CLEAR_GLASS_ANIMAL_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_na", "Quite Clear Glass",
        GlassCollisionPredicate.ANIMALS_BLOCK, false, false, false);
    public static final GlassBlocks ENLIGHTENED_CLEAR_GLASS_ANIMAL_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_ena", "Enlightened Clear Glass",
        GlassCollisionPredicate.ANIMALS_BLOCK, true, false, true);
    public static final GlassBlocks DARK_CLEAR_GLASS_ANIMAL_BLOCK = new GlassBlocks(REGISTRATE, "clear_glass_dna", "Dark Clear Glass",
        GlassCollisionPredicate.ANIMALS_BLOCK, true, false, true);

    // endregion

    //misc Region

    public static final BlockEntry<GraveBlock> GRAVE = REGISTRATE
        .block("grave", Material.STONE, (p) -> new GraveBlock(p))
        .properties(props -> props
            .strength(-1.0F, 3600000.0F)
            .noDrops()
            .noOcclusion())
        .blockstate((con, prov) -> prov.simpleBlock(con.get(), prov
            .models()
            .getExistingFile(new ResourceLocation(EnderIO.DOMAIN, "block/grave"))))
        .addLayer(() -> RenderType::cutout)
        .item()
        .group(() -> EIOCreativeTabs.BLOCKS)
        .build()
        .register();

    public static final BlockEntry<AmethystBlock> INFINITY_CRYSTAL = REGISTRATE
        .block("infinity_crystal_block", Material.AMETHYST, AmethystBlock::new)
        .properties(props -> props
            .strength(1.5F)
            .sound(SoundType.AMETHYST)
            .requiresCorrectToolForDrops())
        .item()
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
        .build()
        .register();

    public static final BlockEntry<BuddingInfinityCrystalBlock> BUDDING_INFINITY_CRYSTAL = REGISTRATE
        .block("budding_infinity_crystal", Material.AMETHYST, BuddingInfinityCrystalBlock::new)
        .properties(props -> props
            .strength(1.5F)
            .sound(SoundType.AMETHYST)
            .requiresCorrectToolForDrops()
            .randomTicks())
        .item()
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
        .build()
        .register();

    public static final BlockEntry<AmethystClusterBlock> INFINITY_CRYSTAL_CLUSTER = REGISTRATE
        .block("infinity_crystal_cluster", Material.AMETHYST, props -> new AmethystClusterBlock(7, 3, props))
        .blockstate((ctx, prov) -> prov.directionalBlock(ctx.get(), prov.models().cross(ctx.getName(), prov.modLoc("block/" + ctx.getName()))))
        .addLayer(() -> RenderType::cutout)
        .properties(props -> props
            .noOcclusion()
            .randomTicks()
            .sound(SoundType.AMETHYST_CLUSTER)
            .lightLevel((state) -> 5))
        .item()
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
        .build()
        .register();

    public static final BlockEntry<AmethystClusterBlock> LARGE_INFINITY_BUD = REGISTRATE
        .block("large_infinity_bud", Material.AMETHYST, props -> new AmethystClusterBlock(5, 3, props))
        .blockstate((ctx, prov) -> prov.directionalBlock(ctx.get(), prov.models().cross(ctx.getName(), prov.modLoc("block/" + ctx.getName()))))
        .addLayer(() -> RenderType::cutout)
        .properties(props -> props
            .noOcclusion()
            .randomTicks()
            .sound(SoundType.AMETHYST_CLUSTER)
            .lightLevel((state) -> 4))
        .item()
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS)) // TODO: Take away...
        .build()
        .register();

    public static final BlockEntry<AmethystClusterBlock> MEDIUM_INFINITY_BUD = REGISTRATE
        .block("medium_infinity_bud", Material.AMETHYST, props -> new AmethystClusterBlock(4, 3, props))
        .blockstate((ctx, prov) -> prov.directionalBlock(ctx.get(), prov.models().cross(ctx.getName(), prov.modLoc("block/" + ctx.getName()))))
        .addLayer(() -> RenderType::cutout)
        .properties(props -> props
            .noOcclusion()
            .randomTicks()
            .sound(SoundType.AMETHYST_CLUSTER)
            .lightLevel((state) -> 2))
        .item()
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS)) // TODO: Take away...
        .build()
        .register();

    public static final BlockEntry<AmethystClusterBlock> SMALL_INFINITY_BUD = REGISTRATE
        .block("small_infinity_bud", Material.AMETHYST, props -> new AmethystClusterBlock(3, 3, props))
        .blockstate((ctx, prov) -> prov.directionalBlock(ctx.get(), prov.models().cross(ctx.getName(), prov.modLoc("block/" + ctx.getName()))))
        .addLayer(() -> RenderType::cutout)
        .properties(props -> props
            .noOcclusion()
            .randomTicks()
            .sound(SoundType.AMETHYST_CLUSTER)
            .lightLevel((state) -> 1))
        .item()
        .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS)) // TODO: Take away...
        .build()
        .register();

    /*
   public static final Block AMETHYST_CLUSTER = register("amethyst_cluster", new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel((p_152651_) -> {
      return 5;
   })));
   public static final Block LARGE_AMETHYST_BUD = register("large_amethyst_bud", new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER).sound(SoundType.MEDIUM_AMETHYST_BUD).lightLevel((p_152639_) -> {
      return 4;
   })));
   public static final Block MEDIUM_AMETHYST_BUD = register("medium_amethyst_bud", new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER).sound(SoundType.LARGE_AMETHYST_BUD).lightLevel((p_152632_) -> {
      return 2;
   })));
   public static final Block SMALL_AMETHYST_BUD = register("small_amethyst_bud", new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER).sound(SoundType.SMALL_AMETHYST_BUD).lightLevel((p_152629_) -> {
      return 1;
     */

    // endregion

    public static <T extends Block> BlockBuilder<T, Registrate> simpleBlockBuilder(String name, T block) {
        return REGISTRATE
            .block(name, (p) -> block)
            .item()
            .group(() -> EIOCreativeTabs.BLOCKS)
            .build();
    }

    private static BlockBuilder<Block, Registrate> metalBlock(String name) {
        return REGISTRATE
            .block(name, Material.METAL, Block::new)
            .properties(props -> props
                .sound(SoundType.METAL)
                .color(MaterialColor.METAL))
            .item()
            .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
            .build();
    }

    private static BlockBuilder<Block, Registrate> chassisBlock(String name) {
        return REGISTRATE
            .block(name, Material.METAL, Block::new)
            .addLayer(() -> RenderType::cutout)
            .properties(props -> props
                .noOcclusion()
                .sound(SoundType.METAL)
                .color(MaterialColor.METAL))
            .item()
            .group(new NonNullLazyValue<>(() -> EIOCreativeTabs.BLOCKS))
            .build();
    }

    public static void register() {}

    public static void clientInit() {

    }
}
