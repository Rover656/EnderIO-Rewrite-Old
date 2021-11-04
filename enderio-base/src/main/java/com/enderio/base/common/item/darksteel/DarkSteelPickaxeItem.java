package com.enderio.base.common.item.darksteel;

import com.enderio.base.common.capability.EIOCapabilities;
import com.enderio.base.common.capability.darksteel.DarkSteelUpgradeable;
import com.enderio.base.common.capability.darksteel.IDarkSteelUpgradable;
import com.enderio.base.common.item.EIOItems;
import com.enderio.base.common.item.darksteel.upgrades.SpoonUpgrade;
import com.enderio.base.common.item.util.IEnergyBar;
import com.enderio.core.common.capability.IMultiCapabilityItem;
import com.enderio.core.common.capability.MultiCapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DarkSteelPickaxeItem extends PickaxeItem implements IEnergyBar, IMultiCapabilityItem {

    public DarkSteelPickaxeItem(Properties pProperties) {
        super(EIOItems.DARK_STEEL_TIER, 1, -2.8F, pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        //TODO: has been upgraded
        return false;
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
        boolean hasSpoon = false;
        LazyOptional<IDarkSteelUpgradable> cap = pStack.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE);
        if(cap.isPresent()) {
            hasSpoon = cap.resolve().get().hasUpgrade(SpoonUpgrade.NAME);
        }
        System.out.println("Has spoon: " + hasSpoon);
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        //TODO: Spoon
        return super.isCorrectToolForDrops(stack, state);
    }

    @Nullable
    @Override
    public MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.addSerialized(EIOCapabilities.DARK_STEEL_UPGRADABLE, LazyOptional.of(DarkSteelUpgradeable::new));
        //provider.addSerialized("Energy", CapabilityEnergy.ENERGY, LazyOptional.of(() -> new EnergyStorage(getMaxEnergy())));
        return provider;
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab pCategory, @Nonnull NonNullList<ItemStack> pItems) {
        if (allowdedIn(pCategory)) {
            ItemStack is = new ItemStack(this);
            pItems.add(is.copy());

            is.getCapability(EIOCapabilities.DARK_STEEL_UPGRADABLE).ifPresent(upgradable -> upgradable.addUpgrade(new SpoonUpgrade()));
            pItems.add(is);
        }
    }
}
