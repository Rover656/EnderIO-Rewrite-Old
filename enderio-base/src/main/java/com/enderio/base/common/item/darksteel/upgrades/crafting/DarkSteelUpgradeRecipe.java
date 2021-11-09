package com.enderio.base.common.item.darksteel.upgrades.crafting;

import com.enderio.base.EnderIO;
import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.darksteel.DarkSteelUpgradeable;
import com.enderio.base.common.item.darksteel.upgrades.SpoonUpgrade;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = EnderIO.MODID)
public class DarkSteelUpgradeRecipe extends ShapelessRecipe {

    //---------- Register recipe

    public static final RecipeSerializer<DarkSteelUpgradeRecipe> SERIALIZER = new Serializer();

    @SubscribeEvent
    public static void registerSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        DarkSteelUpgradeRecipe.SERIALIZER.setRegistryName(new ResourceLocation(EnderIO.MODID, "dark_steel_upgrade"));
        event.getRegistry().register(SERIALIZER);
    }

    //--------- Class

    public DarkSteelUpgradeRecipe(ResourceLocation pRecipeId) {
        super(pRecipeId, "", ItemStack.EMPTY, NonNullList.create());
    }

//    @Override
//    public RecipeType<?> getType() {
//        return RecipeType.SMITHING;
//    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        ItemStack item = null;
        ItemStack upgarde = null;
        for (int i = 0; i < pInv.getContainerSize(); i++) {
            if (pInv.getItem(i).getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).isPresent()) {
                item = pInv.getItem(i);
            }
            if (pInv.getItem(i).is(Items.APPLE)) {
                upgarde = item = pInv.getItem(i);
            }
        }
        return item != null && upgarde != null;
    }

    @Override
    public ItemStack assemble(CraftingContainer pInv) {

        ItemStack item = ItemStack.EMPTY;
        ItemStack upgarde = ItemStack.EMPTY;
        for (int i = 0; i < pInv.getContainerSize(); i++) {
            if (pInv.getItem(i).getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).isPresent()) {
                item = pInv.getItem(i);
            }
            if (pInv.getItem(i).is(Items.APPLE)) {
                upgarde = pInv.getItem(i);
            }
        }
        ItemStack result = item.copy();
        DarkSteelUpgradeable.addUpgrade(result, SpoonUpgrade::new);
        return result;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }


    static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<DarkSteelUpgradeRecipe> {

        @Override
        public DarkSteelUpgradeRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return new DarkSteelUpgradeRecipe(pRecipeId);
        }

        @Override
        public DarkSteelUpgradeRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new DarkSteelUpgradeRecipe(pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DarkSteelUpgradeRecipe pRecipe) {
        }

    }
}

