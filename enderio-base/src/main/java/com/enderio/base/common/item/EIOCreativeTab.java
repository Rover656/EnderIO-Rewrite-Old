package com.enderio.base.common.item;

import com.enderio.base.EnderIO;
import com.enderio.base.common.item.registry.EIOItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.function.Supplier;

public class EIOCreativeTab extends CreativeModeTab {
    public static final EIOCreativeTab MAIN = new EIOCreativeTab("main", EIOItems.CREATIVE_ICON_NONE::get);
    public static final EIOCreativeTab SOULS = new EIOCreativeTab("souls", EIOItems.CREATIVE_ICON_MOBS::get);
    // TODO ROVER: New creative tab layout

    private final Supplier<Item> itemIcon;

    public EIOCreativeTab(String name, Supplier<Item> itemIcon) {
        super("enderio." + name);
        this.itemIcon = itemIcon;
        EnderIO
            .registrate()
            .addLang("itemGroup", new ResourceLocation(EnderIO.DOMAIN, name), getEnglish(name));
    }

    protected String getEnglish(String name) {
        if (name.equals("main")) return "EnderIO";
        return "EnderIO " + name.substring(0, 1)
            .toUpperCase(Locale.ENGLISH) + name.substring(1);
    }

    @Override
    public ItemStack makeIcon() {
        if (itemIcon.get() == null)
            return new ItemStack(Items.BEDROCK);
        return new ItemStack(itemIcon.get());
    }
}
