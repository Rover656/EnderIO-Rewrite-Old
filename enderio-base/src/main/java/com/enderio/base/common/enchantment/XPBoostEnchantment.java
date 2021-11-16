package com.enderio.base.common.enchantment;

import com.enderio.base.EIOConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class XPBoostEnchantment extends EIOBaseEnchantment {

    public XPBoostEnchantment() {
        super(EIOConfig.COMMON.ENCHANTMENTS.XP_BOOST_RARITY.get(), EIOEnchantmentCategories.XPBOOST, new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND }, () -> true);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMaxCost(int pLevel) {
        return EIOConfig.COMMON.ENCHANTMENTS.XP_BOOST_MAX_COST_BASE.get() + EIOConfig.COMMON.ENCHANTMENTS.XP_BOOST_MAX_COST_MULT.get() * pLevel;
    }

    @Override
    public int getMinCost(int pLevel) {
        return EIOConfig.COMMON.ENCHANTMENTS.XP_BOOST_MIN_COST_BASE.get() + EIOConfig.COMMON.ENCHANTMENTS.XP_BOOST_MIN_COST_MULT.get() * pLevel;
    }

    @Override
    protected boolean checkCompatibility(Enchantment pOther) {
        return super.checkCompatibility(pOther) && pOther != Enchantments.SILK_TOUCH;
    }
}
