package com.enderio.base.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class EIOTags {
    public static void init(){
        Items.init();
    }

    public static class Items {

        private static void init() {}

        public static final Tags.IOptionalNamedTag<Item> WRENCH = ItemTags.createOptional(new ResourceLocation("forge", "tools/wrench"));
    }
}
