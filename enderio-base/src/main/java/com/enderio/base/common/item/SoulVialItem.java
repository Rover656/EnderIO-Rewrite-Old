package com.enderio.base.common.item;

import com.enderio.base.EIOCreativeTabs;
import com.enderio.base.EIOItems;
import com.enderio.base.common.util.EntityCaptureUtils;
import com.enderio.core.common.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SoulVialItem extends Item {
    public SoulVialItem(Properties pProperties) {
        super(pProperties);
    }

    // Item appearance and description

    @Override
    public boolean isFoil(@Nonnull ItemStack pStack) {
        return getEntityType(pStack).isPresent();
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack pStack, @Nullable Level pLevel, @Nonnull List<Component> pTooltipComponents,
        @Nonnull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        // Add entity information
        getEntityType(pStack).ifPresent(entityType -> {
            pTooltipComponents.add(new TranslatableComponent(EntityUtil.getEntityDescriptionId(entityType)));
            // TODO: Also add health data
        });
    }

    // endregion

    // region Interactions

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack pStack, @Nonnull Player pPlayer, @Nonnull LivingEntity pInteractionTarget,
        @Nonnull InteractionHand pUsedHand) {
        if (pPlayer.level.isClientSide) {
            return InteractionResult.FAIL;
        }

        if (getEntityType(pStack).isEmpty()) {
            // Don't allow bottled player.
            if (pInteractionTarget instanceof Player) {
                pPlayer.displayClientMessage(new TextComponent("You cannot put player in a bottle"), true);
                return InteractionResult.FAIL;
            }

            // Get the entity type and verify it isn't blacklisted
            // TODO: maybe make the method give a rejection reason so we can give accurate status messages?
            if (!EntityCaptureUtils.canCapture(pInteractionTarget)) {
                pPlayer.displayClientMessage(new TextComponent("This entity is blacklisted"), true);
                return InteractionResult.FAIL;
            }

            // No dead mobs.
            if (!pInteractionTarget.isAlive()) {
                pPlayer.displayClientMessage(new TextComponent("Cannot capture dead mob"), true);
                return InteractionResult.FAIL;
            }

            // TODO: If there's more than one vial in the player's hand, keep the others in tact by putting the filled one elsewhere.

            // Create a filled vial and put the entity's NBT inside.
            ItemStack filledVial = new ItemStack(EIOItems.FILLED_SOUL_VIAL.get());
            filledVial.setTag(pInteractionTarget.serializeNBT());

            // Replace the empty vial with the full one
            pPlayer.setItemInHand(pUsedHand, filledVial);

            // Remove the entity from the world
            pInteractionTarget.discard();
        }

        return InteractionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack itemStack = pContext.getItemInHand();
        Player player = pContext.getPlayer();

        // Only players may use the soul vial
        if (player == null) {
            return InteractionResult.FAIL;
        }

        // Try to get the entity type from the item stack.
        getEntityType(itemStack).ifPresent(entityType -> {
            // Get the face of the block we clicked and its position.
            Direction face = pContext.getClickedFace();
            BlockPos spawnPos = pContext.getClickedPos();

            // Get the spawn location for the mob.
            double spawnX = spawnPos.getX() + face.getStepX() + 0.5;
            double spawnY = spawnPos.getY() + face.getStepY();
            double spawnZ = spawnPos.getZ() + face.getStepZ() + 0.5;

            float rotation = Mth.wrapDegrees(pContext
                .getLevel()
                .getRandom()
                .nextFloat() * 360.0f);

            // noinspection ConstantConditions - the itemstack tag will not be null, its deal with in getEntityType.
            Optional<Entity> entity = EntityType.create(itemStack.getTag(), pContext.getLevel());
            entity.ifPresent(ent -> {
                ent.setPos(spawnX, spawnY, spawnZ);
                ent.setYRot(rotation);
                pContext
                    .getLevel()
                    .addFreshEntity(ent);
            });

            player.setItemInHand(pContext.getHand(), new ItemStack(EIOItems.EMPTY_SOUL_VIAL.get()));

        });

        return super.useOn(pContext);
    }

    // endregion

    // region Creative tabs

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab pCategory, @Nonnull NonNullList<ItemStack> pItems) {
        if (pCategory == getItemCategory()) {
            pItems.add(new ItemStack(this));
        } else if (pCategory == EIOCreativeTabs.SOULS) {
            // Register for every mob that can be captured.
            for (ResourceLocation entity : EntityCaptureUtils.getCapturableEntities()) {
                ItemStack is = new ItemStack(EIOItems.FILLED_SOUL_VIAL.get());
                setEntityType(is, entity);
                pItems.add(is);
            }
        }
    }

    @Override
    public Collection<CreativeModeTab> getCreativeTabs() {
        return Arrays.asList(getItemCategory(), EIOCreativeTabs.SOULS);
    }

    // endregion

    // region Entity Storage

    public static Optional<ResourceLocation> getEntityType(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("id")) {
            return Optional.of(new ResourceLocation(tag.getString("id")));
        }
        return Optional.empty();
    }

    private static void setEntityType(ItemStack stack, ResourceLocation entityType) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("id", entityType.toString());
        stack.setTag(tag);
    }

    // endregion
}
