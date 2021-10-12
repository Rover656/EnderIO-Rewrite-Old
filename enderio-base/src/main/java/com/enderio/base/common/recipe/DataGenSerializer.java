package com.enderio.base.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public abstract class DataGenSerializer<T extends Recipe<C>, C extends Container> extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
        public abstract void toJson(CapacitorDataRecipe recipe, JsonObject json);
}
