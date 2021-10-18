package com.enderio.base.common.item.capacitors;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.capacitors.CapacitorData;
import com.enderio.base.common.item.EIOItems;
import com.enderio.base.common.util.CapacitorUtil;
import com.enderio.core.common.capability.IMultiCapability;
import com.enderio.core.common.capability.MultiCapabilityProvider;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public class LootCapacitorItem extends Item implements IMultiCapability{
    private CapacitorData data = new CapacitorData();
    
    public LootCapacitorItem(Properties pProperties) {
        super(pProperties);
    }
    
    public LootCapacitorItem(Properties pProperties, float base, Map<String, Float> specialization) {
        super(pProperties);
        this.data.setBase(base);
        this.data.addAllSpecialization(specialization);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
            TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pStack.is(EIOItems.LOOT_CAPACITOR.get())) { // only apply tooltip to loot capacitors
            CapacitorUtil.getTooltip(pStack, pTooltipComponents);
        }
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt, MultiCapabilityProvider provider) {
        if (stack.is(EIOItems.LOOT_CAPACITOR.get())) { // each stack loot capacitors needs it own capability
            provider.add(EIOCapabilities.CAPACITOR, LazyOptional.of(CapacitorData::new));
        }else {// other capacitors share (each will always be the same as another stack).
            provider.add(EIOCapabilities.CAPACITOR, LazyOptional.of(() -> data));
        }
        return provider;
    }

}
