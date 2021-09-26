package com.enderio.base.common.enchantments;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SoulBoundHandler {
	
//	@SubscribeEvent
//	public static void handler(LivingDropsEvent event) {
//		ArrayList<ItemEntity> drops = new ArrayList<ItemEntity>();
//		if (event.getEntityLiving() instanceof Player player) {
//			Iterator<ItemEntity> iter = event.getDrops().iterator();
//			while (iter.hasNext()) {
//		    	ItemEntity ei = iter.next();
//		    	ItemStack item = ei.getItem();
//		    	if (isSoulBound(item)) {
//		    		if (addToPlayerInventory(player, item)) {
//		    			iter.remove();
//		    		}
//		    	}
//		    }
//			if (!drops.retainAll(event.getDrops())) { return; }
//			event.setCanceled(true);
//			drops.forEach((item) -> {
//				event.getEntityLiving().level.addFreshEntity(item);
//				item.setPos(event.getEntityLiving().position());
//			});
//		}
//	}

}
