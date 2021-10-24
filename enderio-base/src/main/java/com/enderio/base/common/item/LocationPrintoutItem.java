package com.enderio.base.common.item;

import com.enderio.base.common.menu.CoordinateMenu;
import com.enderio.base.common.util.CoordinateSelection;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class LocationPrintoutItem extends Item {

    //TODO: move to capability once rovers MultiCapabilityHandler is done

    private static final String LOCATION_NBT_KEY = "enderio_selection";

    public LocationPrintoutItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Optional<CoordinateSelection> optionalSelection = getSelection(pContext.getItemInHand());
        if (optionalSelection.isPresent() && pContext.getPlayer() != null && pContext.getPlayer().isCrouching()) {
            if (pContext.getPlayer() instanceof ServerPlayer serverPlayer) {
                handleRightClick(serverPlayer, optionalSelection.get(), pContext.getItemInHand());
            }
            return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide);
        }
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
        Optional<CoordinateSelection> optionalSelection = getSelection(itemInHand);
        if (optionalSelection.isPresent() && pPlayer.isCrouching()) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                CoordinateSelection selection = optionalSelection.get();
                handleRightClick(serverPlayer, selection, itemInHand);
            }
            return InteractionResultHolder.sidedSuccess(itemInHand, pLevel.isClientSide);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    private static void handleRightClick(ServerPlayer serverPlayer, CoordinateSelection selection, ItemStack printout) {
        openMenu(serverPlayer, selection.getPos(), selection.getLevel(), printout.getHoverName().getString());
    }

    private static void openMenu(ServerPlayer player, BlockPos pos, ResourceLocation dimension, String name) {
        CoordinateMenu copyFrom = new CoordinateMenu(0, pos, dimension, name);

        NetworkHooks.openGui(player,new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TextComponent("");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                return new CoordinateMenu(pContainerId, copyFrom.getPos(), copyFrom.getDimension(), name);
            }
        }, copyFrom::writeAdditionalData);
    }

    public static Optional<CoordinateSelection> getSelection(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(LOCATION_NBT_KEY, Tag.TAG_COMPOUND)) {
            return Optional.of(CoordinateSelection.of(tag.getCompound(LOCATION_NBT_KEY)));
        }
        return Optional.empty();
    }

    public static void setSelection(ItemStack stack, CoordinateSelection selection) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put(LOCATION_NBT_KEY, selection.serializeNBT());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> toolTip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, toolTip, pIsAdvanced);
        getSelection(pStack).ifPresent(selection -> {
            toolTip.add(
                writeCoordinate('x', selection.getPos().getX())
                    .append(writeCoordinate('y', selection.getPos().getY()))
                    .append(writeCoordinate('z', selection.getPos().getZ()))
            );
            toolTip.add(new TextComponent(selection.getLevelName()));
        });
    }

    private static MutableComponent writeCoordinate(char character, int number) {
        return new TextComponent("" + character).withStyle(ChatFormatting.GRAY).append(new TextComponent("" + number).withStyle(ChatFormatting.GREEN));
    }
}
