package com.enderio.core.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EIOScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private final boolean renderLabels;
    private final List<EditBox> editBoxList = new ArrayList<>();

    protected EIOScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        this(pMenu, pPlayerInventory, pTitle, false);
    }
    protected EIOScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, boolean renderLabels) {
        super(pMenu, pPlayerInventory, pTitle);
        this.renderLabels = renderLabels;
        this.imageWidth = getBackgroundImageSize().getLeft();
        this.imageHeight = getBackgroundImageSize().getRight();
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        Map<String, String> oldEditBoxValues = new HashMap<>();
        for (EditBox editBox: editBoxList) {
            oldEditBoxValues.put(editBox.getMessage().getString(), editBox.getValue());
        }
        editBoxList.clear();
        super.resize(pMinecraft, pWidth, pHeight);
        for (EditBox editBox: editBoxList) {
            editBox.setValue(oldEditBoxValues.getOrDefault(editBox.getMessage().getString(), ""));
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTicks, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getBackgroundImage());
        blit(pPoseStack, getGuiLeft(), getGuiTop(), 0, 0, imageWidth, imageHeight);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) { //ESC has priority
            Minecraft.getInstance().player.closeContainer();
        }
        for (EditBox editBox: editBoxList) {
            if (editBox.keyPressed(pKeyCode, pScanCode, pModifiers)
                || editBox.canConsumeInput()) {
                return true;
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void removed() {
        super.removed();
        if (!editBoxList.isEmpty()) {
            Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
        }
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        if (renderLabels) {
            super.renderLabels(pPoseStack, pMouseX, pMouseY);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        for (EditBox editBox: editBoxList) {
            editBox.tick();
        }
    }

    protected abstract ResourceLocation getBackgroundImage();

    protected abstract Pair<Integer, Integer> getBackgroundImageSize();

    @Override
    protected <U extends GuiEventListener & NarratableEntry> U addWidget(U guiEventListener) {
        if (guiEventListener instanceof EditBox editBox) {
            editBoxList.add(editBox);
            Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true);
        }
        return super.addWidget(guiEventListener);
    }

    @Override
    protected void removeWidget(GuiEventListener guiEventListener) {
        super.removeWidget(guiEventListener);
        if (guiEventListener instanceof EditBox editBox) {
            editBoxList.remove(editBox);
        }
    }
}
