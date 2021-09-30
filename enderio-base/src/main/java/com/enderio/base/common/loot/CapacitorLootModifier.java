package com.enderio.base.common.loot;

import java.util.List;

import com.enderio.base.EnderIO;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnderIO.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapacitorLootModifier extends LootModifier{

    protected CapacitorLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        context.getParam(null);
        return null;
    }

}
