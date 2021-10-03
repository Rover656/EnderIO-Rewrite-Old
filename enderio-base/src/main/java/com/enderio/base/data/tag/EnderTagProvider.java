package com.enderio.base.data.tag;

import com.enderio.base.EIOBlocks;
import com.enderio.base.EnderIO;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class EnderTagProvider extends BlockTagsProvider {
    public EnderTagProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, EnderIO.DOMAIN, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(EIOBlocks.PAINTED_SAND.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(EIOBlocks.PAINTED_GLOWSTONE.get(), EIOBlocks.PAINTED_REDSTONE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(EIOBlocks.PAINTED_FENCE.get(), EIOBlocks.PAINTED_FENCE_GATE.get(), EIOBlocks.PAINTED_STAIRS.get(), EIOBlocks.PAINTED_CRAFTING_TABLE.get(), EIOBlocks.PAINTED_TRAPDOOR.get(), EIOBlocks.PAINTED_WOODEN_PRESSURE_PLATE.get(), EIOBlocks.PAINTED_SLAB.get());
    }
}
