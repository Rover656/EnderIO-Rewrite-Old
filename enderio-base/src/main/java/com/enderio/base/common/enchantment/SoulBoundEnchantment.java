package com.enderio.base.common.enchantment;

import com.enderio.base.EIOConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SoulBoundEnchantment extends EIOBaseEnchantment {

    public SoulBoundEnchantment() {
        super(EIOConfig.COMMON.ENCHANTMENTS.SOUL_BOUND_RARITY.get(), EnchantmentCategory.VANISHABLE, EquipmentSlot.values(), () -> true);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return EIOConfig.COMMON.ENCHANTMENTS.SOUL_BOUND_MAX_COST.get();
    }

    @Override
    public int getMinCost(int pLevel) {
        return EIOConfig.COMMON.ENCHANTMENTS.SOUL_BOUND_MIN_COST.get();
    }
}
