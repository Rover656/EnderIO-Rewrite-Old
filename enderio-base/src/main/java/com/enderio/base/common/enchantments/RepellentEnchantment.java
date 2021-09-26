package com.enderio.base.common.enchantments;

import com.enderio.base.common.util.TeleportUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class RepellentEnchantment extends EIOBaseEnchantment{

	//config rarity?
	public RepellentEnchantment() {
		super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[] { EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET }, () -> true);
	}
	
	//config?
	@Override
	public int getMaxLevel() {
		return 4;
	}
	
	//config?
	@Override
	public int getMaxCost(int pLevel) {
		return 10 + 10*pLevel;
	}
	
	//config
	@Override
	public int getMinCost(int pLevel) {
		return 10 + 5*pLevel;
	}
	
	//config?
	private float getChance(int level) {
		return 0.35F + 0.1F*level;
	}
	
	//config?
	private double getRange(int level) {
		return 8D + 8D*level;
	}
	
	//config non player mobs?
	@Override
	public void doPostHurt(LivingEntity pUser, Entity pAttacker, int pLevel) {
		if (pUser instanceof Player && pAttacker instanceof LivingEntity attacker) {
			if (pUser.getRandom().nextFloat() < getChance(pLevel)) {
				if (pAttacker instanceof Player) {
					TeleportUtils.randomTeleport(attacker, getRange(pLevel));
				}else if (pUser.getRandom().nextFloat() < .75F){ // non player repel config?
					TeleportUtils.randomTeleport(attacker, getRange(pLevel));
				}
			}
		}
	}
}
