package com.enderio.base.common.item;

import java.util.function.Consumer;

import com.enderio.base.client.renderers.GearBEWLR;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;

public class GearItem extends MaterialItem{

	public GearItem(Properties props, boolean hasGlint) {
		super(props, hasGlint);
	}
	
	
	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			
			final GearBEWLR g = GearBEWLR.INSTANCE;
			
			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return g;
			}
			
		});
	}
	
}
