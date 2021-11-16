package com.enderio.base.common.item.darksteel.upgrades.direct;

import com.enderio.base.EnderIO;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = EnderIO.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DirectUpgradeLootModifier extends LootModifier {

    protected DirectUpgradeLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {

        if (context.getParam(LootContextParams.THIS_ENTITY) instanceof Player player) {
            ServerLevel level = context.getLevel();
            for (ItemStack is : generatedLoot) {
                ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), is.copy());
                //drop the item at the players feet
                level.addFreshEntity(itemEntity);
                //get the player to pick it up
                itemEntity.playerTouch(player);
            }
            generatedLoot.clear(); //remove default drop behaviour
        }
        return generatedLoot;
    }

    @SubscribeEvent
    public static void register(@Nonnull RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new Serializer().setRegistryName(EnderIO.loc("direct_upgrade")));
    }

    public static class Serializer extends GlobalLootModifierSerializer<DirectUpgradeLootModifier> {

        @Override
        public DirectUpgradeLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            return new DirectUpgradeLootModifier(conditions);
        }

        @Override
        public JsonObject write(DirectUpgradeLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }

}
