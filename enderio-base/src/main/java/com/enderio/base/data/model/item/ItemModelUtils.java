package com.enderio.base.data.model.item;

import com.enderio.base.EnderIO;
import com.enderio.base.common.item.misc.GearItem;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class ItemModelUtils {
    public static void fakeBlockModel(DataGenContext<Item, ? extends Item> ctx, RegistrateItemModelProvider prov) {
        prov
            .withExistingParent(prov.name(ctx), prov.mcLoc("block/cube_all"))
            .texture("all", prov.itemTexture(ctx));
    }

    public static void mimicItem(DataGenContext<Item, ? extends Item> ctx, ItemEntry<? extends Item> item, RegistrateItemModelProvider prov) {
        prov.generated(ctx, prov.itemTexture(item));
    }

    public static void gearItem(DataGenContext<Item, ? extends Item> ctx, RegistrateItemModelProvider prov) {
        // json so the BEWLR is used + perspectives
        prov
            .getBuilder(ctx
                .get()
                .getRegistryName()
                .getPath()
                .toString())
            .parent((new ModelFile.UncheckedModelFile("builtin/entity")))
            .transforms()
            .transform(ModelBuilder.Perspective.GROUND)
            .rotation(0, 0, 0)
            .translation(0, 2, 0)
            .scale(0.5F, 0.5F, 0.5F)
            .end()
            .transform(ModelBuilder.Perspective.HEAD)
            .rotation(0, 180, 0)
            .translation(0, 13, 7)
            .scale(1F, 1F, 1F)
            .end()
            .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
            .rotation(0, 0, 0)
            .translation(0, 3, 1)
            .scale(0.55F, 0.55F, 0.55F)
            .end()
            .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
            .rotation(0, -90F, 25F)
            .translation(1.13F, 3.2F, 1.13F)
            .scale(0.68F, 0.68F, 0.68F)
            .end()
            .transform(ModelBuilder.Perspective.FIXED)
            .rotation(0, 180, 0)
            .translation(0, 0, 0)
            .scale(1F, 1F, 1F)
            .end()
            .end();

        // json with the actual model
        prov
            .getBuilder(ctx
                .get()
                .getRegistryName()
                .getPath()
                .toString() + "_helper")
            .parent(new ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", new ResourceLocation(EnderIO.MODID, "item/" + ctx
                .get()
                .getRegistryName()
                .getPath()
                .toString()));
    }
}
