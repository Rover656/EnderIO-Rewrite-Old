package com.enderio.base.common.blockentity;

import com.enderio.base.EIOBlocks;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GraveHandler {
    
    //TODO implementation with other mods.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void GraveDeath(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (player.isCreative()) {return;}
            MutableBlockPos pos = new MutableBlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());
            BlockPlaceContext pUseContext = new BlockPlaceContext(player, InteractionHand.MAIN_HAND, new ItemStack(EIOBlocks.GRAVE.get()), new BlockHitResult(Vec3.atCenterOf(pos), Direction.NORTH, pos, true));
            while (!player.level.getBlockState(pos).canBeReplaced(pUseContext)) {//check if a grave can be made
                pos.move(0, 1, 0);
            } 
            player.level.setBlockAndUpdate(pos, EIOBlocks.GRAVE.getDefaultState());
            BlockEntity be =  player.level.getBlockEntity(pos);
            if (be instanceof GraveBE grave) {
                grave.addInventory(player);//set inventory, uuid and dirction for grave.
                event.getDrops().clear();//clears items that would have been dropped.
            }
        }
    }

}