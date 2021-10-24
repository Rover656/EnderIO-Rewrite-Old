package com.enderio.base.common.menu;

import com.enderio.base.EnderIO;
import com.enderio.base.client.screen.CoordinateMenuScreen;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.ContainerEntry;

public class EIOMenus {
    private static final Registrate REGISTRATE = EnderIO.registrate();

    public static final ContainerEntry<CoordinateMenu> COORDINATE = REGISTRATE.container("coordinate", CoordinateMenu::new, () -> CoordinateMenuScreen::new).register();//tileEntity("grave", (s,p,t) -> new GraveBlockEntity(t, s, p)).validBlock(EIOBlocks.GRAVE).register();

    public static void register() {}
}
