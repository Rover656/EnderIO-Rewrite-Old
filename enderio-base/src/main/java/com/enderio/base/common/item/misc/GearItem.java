package com.enderio.base.common.item.misc;

import java.util.function.Consumer;

import com.enderio.base.client.renderer.GearBEWLR;

import com.enderio.base.common.item.misc.MaterialItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.util.NonNullLazy;

public class GearItem extends MaterialItem {

    public GearItem(Properties props, boolean hasGlint) {
        super(props, hasGlint);
    }

    // enables the use of a BEWLR
    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            // Minecraft can be null during datagen
            final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> GearBEWLR.INSTANCE);

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer.get();
            }
        });
    }
}
