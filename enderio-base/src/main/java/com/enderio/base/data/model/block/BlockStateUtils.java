package com.enderio.base.data.model.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Collections;

public class BlockStateUtils {
    public static void paneBlock(DataGenContext<Block, ? extends IronBarsBlock> ctx, RegistrateBlockstateProvider cons) {
        cons.paneBlock(ctx.get(),
            cons.models().panePost(ctx.getName().concat("_post"), cons.blockTexture(ctx.get()), cons.blockTexture(ctx.get())),
            cons.models().paneSide(ctx.getName().concat("_side"), cons.blockTexture(ctx.get()), cons.blockTexture(ctx.get())),
            cons.models().paneSideAlt(ctx.getName().concat("_side_alt"), cons.blockTexture(ctx.get()), cons.blockTexture(ctx.get())),
            cons.models().paneNoSide(ctx.getName().concat("_no_side"), cons.blockTexture(ctx.get())),
            cons.models().paneNoSideAlt(ctx.getName().concat("_no_side_alt"), cons.blockTexture(ctx.get())));
    }

    /**
     * {@see ModelProvider.MODEL}
     */
    public static void paintedBlock(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider prov, Block toCopy) {
        Block paintedBlock = ctx.get();
        PaintedModelBuilder paintedModel = new PaintedModelBuilder(
            new ResourceLocation(paintedBlock.getRegistryName().getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + paintedBlock.getRegistryName().getPath()),
            prov.models().existingFileHelper, toCopy);
        prov.models().getBuilder(paintedBlock.getRegistryName().getPath());
        prov.models().generatedModels.put(paintedModel.getLocation(), paintedModel);
        prov.simpleBlock(paintedBlock, paintedModel);
    }
}
