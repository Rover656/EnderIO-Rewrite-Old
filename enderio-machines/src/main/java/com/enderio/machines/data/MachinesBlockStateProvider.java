package com.enderio.machines.data;

import com.enderio.machines.common.block.PaintedFenceBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MachinesBlockStateProvider extends BlockStateProvider {

    MachinesBlockStateProvider instance;

    private MachinesBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper){
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
