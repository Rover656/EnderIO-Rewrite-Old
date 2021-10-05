package com.enderio.base.common.block.painted;

import com.enderio.base.common.block.EIOBlocks;
import com.enderio.base.common.blockentity.EIOBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class PaintedCraftingTableBlock extends CraftingTableBlock implements EntityBlock {

    //TODO: Switch Reflection to AT
    Field access = ObfuscationReflectionHelper.findField(CraftingMenu.class, "f_39350_");
    private static final Component CONTAINER_TITLE = new TranslatableComponent("container.crafting");

    public PaintedCraftingTableBlock(Properties p_52225_) {
        super(p_52225_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return EIOBlockEntities.SINGLE_PAINTED.create(pos, state);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> {
            return new CraftingMenu(p_52229_, p_52230_, ContainerLevelAccess.create(pLevel, pPos)) {
                @Override
                public boolean stillValid(Player pPlayer) {
                    try {
                        return stillValid((ContainerLevelAccess)access.get(this), pPlayer, EIOBlocks.PAINTED_CRAFTING_TABLE.get());
                    } catch (Exception e){
                        return false;
                    }
                }
            };
        }, CONTAINER_TITLE);
    }
}
