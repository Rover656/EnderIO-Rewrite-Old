package com.enderio.base.client.screen;

import com.enderio.base.EnderIO;
import com.enderio.base.common.menu.CoordinateMenu;
import com.enderio.base.common.network.EIOPackets;
import com.enderio.base.common.network.packet.UpdateCoordinateSelectionNameMenuPacket;
import com.enderio.core.client.screen.EIOScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CoordinateMenuScreen extends EIOScreen<CoordinateMenu> {


    private static final Pair<Integer, Integer> BG_SIZE = new ImmutablePair<>(176,116);
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(EnderIO.DOMAIN, "textures/gui/40/location_printout.png");

    public CoordinateMenuScreen(CoordinateMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true);

        EditBox name = new EditBox(this.font, leftPos + 43 + 4, topPos + 20 + 4, 92 - 12, 18, new TextComponent("name"));
        name.setCanLoseFocus(false);
        name.setTextColor(0xFFFFFFFF);
        name.setTextColorUneditable(0xFFFFFFFF);
        name.setBordered(false);
        name.setMaxLength(50);
        name.setResponder(this::onNameChanged);
        name.setValue(menu.getName());
        this.addRenderableWidget(name);
        this.setInitialFocus(name);
        name.setEditable(true);
        this.addRenderableWidget(new Button(getGuiLeft() + imageWidth - 30, getGuiTop() + imageHeight - 30, 20, 20, new TextComponent("Ok"), mouseButton -> Minecraft.getInstance().player.closeContainer())); //TOOD: translation
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
        renderGradleWeirdnessBackground(pPoseStack, pPartialTicks, pMouseX, pMouseY);
    }

    @Override
    protected ResourceLocation getBackgroundImage() {
        return BG_TEXTURE;
    }

    @Override
    protected Pair<Integer, Integer> getBackgroundImageSize() {
        return BG_SIZE;
    }


    private void onNameChanged(String name) {
        EIOPackets.INSTANCE.sendToServer(new UpdateCoordinateSelectionNameMenuPacket(getMenu().containerId, name));
    }
}
