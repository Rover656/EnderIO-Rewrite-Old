package com.enderio.base.common.util;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class BlockStateUtils {

    public static void graveState(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider prov) {
        prov.getVariantBuilder(ctx.get()).partialState().addModels(new ConfiguredModel(new ModelFile.UncheckedModelFile("blocks/skull")));
    }
}
