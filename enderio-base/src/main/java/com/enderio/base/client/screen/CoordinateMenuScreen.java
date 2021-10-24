package com.enderio.base.client.screen;

import com.enderio.base.EnderIO;
import com.enderio.base.common.menu.CoordinateMenu;
import com.enderio.base.common.network.EIOPackets;
import com.enderio.base.common.network.packets.UpdateCoordinateSelectionNameMenuPacket;
import com.enderio.base.common.util.CoordinateSelection;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CoordinateMenuScreen extends AbstractContainerScreen<CoordinateMenu> {

    private EditBox name;

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(EnderIO.DOMAIN, "textures/gui/coordinate.png"); //TODO move to Enderio.resourceLocation call

    public CoordinateMenuScreen(CoordinateMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 116;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    @Override
    protected void init() {
        super.init();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true);
        this.name = new EditBox(this.font, leftPos + 43 + 4, topPos + 20 + 4, 92 - 12, 18, new TextComponent(""));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(0xFFFFFFFF);
        this.name.setTextColorUneditable(0xFFFFFFFF);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue(menu.getName());
        this.addRenderableWidget(this.name);
        this.setInitialFocus(this.name);
        this.name.setEditable(true);
        setFocused(name);
        this.addRenderableWidget(new Button(getGuiLeft() + imageWidth - 30, getGuiTop() + imageHeight - 30, 20, 20, new TextComponent("Ok"), mouseButton -> Minecraft.getInstance().player.closeContainer())); //TOOD: translation
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = this.name.getValue();
        super.resize(pMinecraft, pWidth, pHeight);
        this.name.setValue(s);
    }

    @Override
    public void removed() {
        super.removed();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) { //ESC has priority
            Minecraft.getInstance().player.closeContainer();
        }

        return this.name.keyPressed(pKeyCode, pScanCode, pModifiers)
            || this.name.canConsumeInput()
            || super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        blit(pPoseStack, getGuiLeft(), getGuiTop(), 0, 0, imageWidth, imageHeight);


        int midX = this.width / 2;
        int y = topPos + 48;
        String txt = getMenu().getPos().toShortString();
        int x = midX - font.width(txt) / 2;

        font.drawShadow(pPoseStack, txt, x, y, 0xFFFFFF);

        txt = CoordinateSelection.getLevelName(getMenu().getDimension());
        y += font.lineHeight + 4;
        x = midX - font.width(txt) / 2;
        font.drawShadow(pPoseStack, txt, x, y, 0xFFFFFF);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {}

    private void onNameChanged(String name) {
        EIOPackets.INSTANCE.sendToServer(new UpdateCoordinateSelectionNameMenuPacket(getMenu().containerId, name));
    }
}
