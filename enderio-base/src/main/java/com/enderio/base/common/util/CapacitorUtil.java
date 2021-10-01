package com.enderio.base.common.util;

import java.util.ArrayList;
import java.util.Random;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.capacitors.ICapacitorData;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public class CapacitorUtil {
    private static ArrayList<String> types = new ArrayList<>();
    static {
        types.add(ICapacitorData.ALL_ENERGY_CONSUMPSTION);
        types.add(ICapacitorData.ALL_PRODUCTION_SPEED);
        types.add(ICapacitorData.ALLOY_ENERGY_CONSUMPSTION);
        types.add(ICapacitorData.ALLOY_ENERGY_CONSUMPSTION);
    }

    public static String getRandomType() {
        return types.get(new Random().nextInt(types.size()));
    }
    
    public static Component getTooltip(ItemStack stack) {
        TranslatableComponent[] t = new TranslatableComponent[] { new TranslatableComponent("")};// any better way?
        stack.getCapability(EIOCapabilities.CAPACITOR).ifPresent(cap -> {
            t[0] = new TranslatableComponent("Test Flavor text : %1$s %2$s %3$s", 
                    getBaseText(cap.getBase()), 
                    getTypeText(cap.getSpecializations().keySet().stream().findFirst().get()),
                    getGradeText(cap.getSpecializations().values().stream().findFirst().get()));
        });
        return t[0];
    }
    
    private static TranslatableComponent getBaseText(float base) {
        return new TranslatableComponent("loot.enderio.capacitor.base." + (int)Math.ceil(base));
    }
    
    private static TranslatableComponent getTypeText(String type) {
        return new TranslatableComponent("loot.enderio.capacitor.type." + type);
    }
    
    private static TranslatableComponent getGradeText(float grade) {
        return new TranslatableComponent("loot.enderio.capacitor.grade." + (int)Math.ceil(grade));
    }
}
