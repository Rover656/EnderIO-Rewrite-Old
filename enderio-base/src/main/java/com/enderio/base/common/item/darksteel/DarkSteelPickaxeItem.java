package com.enderio.base.common.item.darksteel;

import com.enderio.base.common.item.EIOItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DarkSteelPickaxeItem extends PickaxeItem {

    public DarkSteelPickaxeItem(Properties pProperties) {
        super(EIOItems.DARK_STEEL_TIER, 1, -2.8F, pProperties);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        //TODO: check powered and obsidian
        if(pState.getBlock() == Blocks.OBSIDIAN) {
            return 50;
        }
        return super.getDestroySpeed(pStack,pState);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        //TODO: use power, explosive
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        //TODO: Spoon
        return super.isCorrectToolForDrops(stack, state);
    }
}
