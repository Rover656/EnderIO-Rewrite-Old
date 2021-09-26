package com.enderio.base.common.enchantments;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class AutoSmeltEnchantment extends EIOBaseEnchantment{

	//config rarity
	public AutoSmeltEnchantment() {
		super(Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[] { EquipmentSlot.MAINHAND}, () -> true);
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	//config
	@Override
	public int getMaxCost(int pLevel) {
		return 60;
	}
	
	//config
	@Override
	public int getMinCost(int pLevel) {
		return 15;
	}
	
	@Override
	protected boolean checkCompatibility(@Nonnull Enchantment pOther) {
		return super.checkCompatibility(pOther) && pOther != Enchantments.SILK_TOUCH;
	}
	
}
