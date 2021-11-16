package com.enderio.base.common.enchantment;

import com.enderio.base.EIOConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ShimmerEnchantment extends EIOBaseEnchantment {

    public ShimmerEnchantment() {
        super(EIOConfig.COMMON.ENCHANTMENTS.SHIMMER_RARITY.get(), EnchantmentCategory.VANISHABLE, EquipmentSlot.values(), () -> true);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return EIOConfig.COMMON.ENCHANTMENTS.SHIMMER_MAX_COST.get();
    }

    @Override
    public int getMinCost(int pLevel) {
        return EIOConfig.COMMON.ENCHANTMENTS.SHIMMER_MIN_COST.get();
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }
}
