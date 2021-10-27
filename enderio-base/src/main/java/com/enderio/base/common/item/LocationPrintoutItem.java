package com.enderio.base.common.item;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.entity.EntityStorage;
import com.enderio.base.common.capability.location.ICoordinateSelection;
import com.enderio.base.common.menu.CoordinateMenu;
import com.enderio.base.common.capability.location.CoordinateSelection;
import com.enderio.core.common.capability.IMultiCapabilityItem;
import com.enderio.core.common.capability.MultiCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class LocationPrintoutItem extends Item implements IMultiCapabilityItem {

    public LocationPrintoutItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Optional<ICoordinateSelection> optionalSelection = getSelection(pContext.getItemInHand());
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
        Optional<ICoordinateSelection> optionalSelection = getSelection(itemInHand);
        if (optionalSelection.isPresent() && pPlayer.isCrouching()) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                ICoordinateSelection selection = optionalSelection.get();
                handleRightClick(serverPlayer, selection, itemInHand);
            }
            return InteractionResultHolder.sidedSuccess(itemInHand, pLevel.isClientSide);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    private static void handleRightClick(ServerPlayer serverPlayer, ICoordinateSelection selection, ItemStack printout) {
        if (selection.isSet())
            openMenu(serverPlayer, selection, printout.getHoverName().getString());
    }

    private static void openMenu(ServerPlayer player, ICoordinateSelection selection, String name) {

        NetworkHooks.openGui(player,new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TextComponent("");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                return new CoordinateMenu(pContainerId, selection, name);
            }
        }, buf -> CoordinateMenu.writeAdditionalData(buf, selection, name));
    }

    public static Optional<ICoordinateSelection> getSelection(ItemStack stack) {
        return stack.getCapability(EIOCapabilities.COORDINATE_SELECTION).map(selection -> selection);
    }

    public static void setSelection(ItemStack stack, ICoordinateSelection newSelection) {
        stack.getCapability(EIOCapabilities.COORDINATE_SELECTION).ifPresent(selection -> selection.copyInto(newSelection));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> toolTip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, toolTip, pIsAdvanced);
        getSelection(pStack).ifPresent(selection -> {
            if (selection.isSet()) {
                toolTip.add(writeCoordinate('x', selection.getPos().getX())
                    .append(writeCoordinate('y', selection.getPos().getY()))
                    .append(writeCoordinate('z', selection.getPos().getZ())));
                toolTip.add(new TextComponent(selection.getLevelName()));
            }
        });
    }

    private static MutableComponent writeCoordinate(char character, int number) {
        return new TextComponent("" + character).withStyle(ChatFormatting.GRAY).append(new TextComponent("" + number).withStyle(ChatFormatting.GREEN));
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.addSerialized(EIOCapabilities.COORDINATE_SELECTION, LazyOptional.of(CoordinateSelection::new));
        return provider;
    }
}
