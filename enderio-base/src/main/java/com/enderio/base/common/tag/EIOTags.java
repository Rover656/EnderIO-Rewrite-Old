package com.enderio.base.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class EIOTags {
    
    public static void init(){
        Items.init();
        Blocks.init();
    }
    
    public static class Items{
        
        private static void init(){}
        
        public static final IOptionalNamedTag<Item> DUSTS_LAPIS = ItemTags.createOptional(new ResourceLocation("forge", "dusts/lapis"));
        public static final IOptionalNamedTag<Item> DUSTS_COAL = ItemTags.createOptional(new ResourceLocation("forge", "dusts/coal"));
        public static final IOptionalNamedTag<Item> DUSTS_QUARTZ = ItemTags.createOptional(new ResourceLocation("forge", "dusts/quartz"));
        public static final IOptionalNamedTag<Item> SILICON = ItemTags.createOptional(new ResourceLocation("forge", "silicon"));
        public static final IOptionalNamedTag<Item> FUSED_QUARTZ = ItemTags.createOptional(new ResourceLocation("forge", "fused_quartz"));
        public static final IOptionalNamedTag<Item> CLEAR_GLASS = ItemTags.createOptional(new ResourceLocation("forge", "clear_glass"));
    }
    
    public static class Blocks{
        
        private static void init(){}
        
        //public static final IOptionalNamedTag<Block> FUSED_QUARTZ = BlockTags.createOptional(new ResourceLocation("forge", "fused_quartz"));
    }
}
