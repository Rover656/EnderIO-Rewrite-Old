package com.enderio.base.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SoulBoundEnchantment extends EIOBaseEnchantment{

	//config rarity?
	public SoulBoundEnchantment() {
		super(Rarity.VERY_RARE, EnchantmentCategory.VANISHABLE, EquipmentSlot.values(), () -> true);
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	//config?
	@Override
	public int getMaxCost(int pLevel) {
		return 60;
	}
	
	//config
	@Override
	public int getMinCost(int pLevel) {
		return 16;
	}

}
