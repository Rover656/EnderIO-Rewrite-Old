package com.enderio.base.common.item.tool;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.List;

public class ElectromagnetItem extends PoweredToggledItem {


    private static final double collisionDistanceSq = 1.25 * 1.25;
    private static final double speed = 0.035;
    private static final double speed4 = speed * 4;

    public ElectromagnetItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected int getEnergyUse() {
        //TODO: Config
        return 1;
    }

    @Override
    protected int getMaxEnergy() {
        //TODO: Config
        return 10000;
    }

    private int getRange() {
        //TODO: Config
        return 5;
    }

    private int getMaxItems() {
        //TODO: Config
        return 20;
    }

    private boolean isBlackListed(ItemEntity entity) {
        //TODO: Config
        return false;
    }

    private boolean isMagnetable(Entity entity) {
        boolean isValidTarget = false;
        if (entity instanceof ItemEntity itemEntity) {
            isValidTarget = !isBlackListed(itemEntity);
        } else if (entity instanceof ExperienceOrb) {
            isValidTarget = true;
        }
        return isValidTarget;
    }

    @Override
    protected void onTickWhenActive(Player player, @Nonnull ItemStack pStack, @Nonnull Level pLevel, @Nonnull Entity pEntity, int pSlotId,
        boolean pIsSelected) {

        int range = getRange();
        AABB bnds = new AABB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range,
            player.getZ() + range);

        List<Entity> toMove = pLevel.getEntities(player, bnds, this::isMagnetable);

        int itemsRemaining = getMaxItems();
        if (itemsRemaining <= 0) {
            itemsRemaining = Integer.MAX_VALUE;
        }

        for (Entity entity : toMove) {

            double x = player.getX() - entity.getX();
            double y = player.getY() + player.getEyeHeight() * .75f - entity.getY();
            double z = player.getZ() - entity.getZ();

            double distance = x * x + y * y + z * z;

            if (distance < collisionDistanceSq) {
                //TODO: Not sure if this is required, works without it
                entity.playerTouch(player);
            } else {
                double distancespeed = speed4 / distance;
                Vec3 mov = entity.getDeltaMovement();
                double deltaX = mov.x + x * distancespeed;
                double deltaZ = mov.z + z * distancespeed;
                double deltaY;
                if (y > 0) {
                    deltaY = 0.12;
                } else {
                    deltaY = mov.y + y * speed;
                }
                entity.setDeltaMovement(deltaX, deltaY, deltaZ);
            }

            if (itemsRemaining-- <= 0) {
                return;
            }
        }

    }



}
