package com.enderio.base.common.item.food;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;

public class EnderiosItem extends BowlFoodItem {
    private static final FoodProperties properties = ((new FoodProperties.Builder()).nutrition(10).saturationMod(0.8f).build());

    public EnderiosItem(Properties pProperties) {
        super(pProperties.food(properties));
    }

    // TODO: Teleportation behaviours.
}
