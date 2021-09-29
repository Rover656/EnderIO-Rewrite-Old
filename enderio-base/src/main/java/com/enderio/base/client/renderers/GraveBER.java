package com.enderio.base.client.renderers;

import java.util.Map;

import com.enderio.base.common.blockentity.GraveBE;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

//renders grave as a playerskull
public class GraveBER implements BlockEntityRenderer<BlockEntity>{
    private BlockEntityRendererProvider.Context context;
    
    public GraveBER(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(BlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        GraveBE grave = (GraveBE) pBlockEntity;
        Direction direction = null;//TODO if we make the grave rotatable
        SkullModelBase skullmodelbase = new SkullModel(this.context.bakeLayer(ModelLayers.PLAYER_HEAD));
        RenderType rendertype = RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin());
        if (grave.getUuid() != null) {
            rendertype = getRenderType(pBlockEntity.getLevel().getPlayerByUUID(grave.getUuid()));
        }
        pMatrixStack.pushPose();
        pMatrixStack.translate(1, 1, 0);
        pMatrixStack.popPose();
        SkullBlockRenderer.renderSkull(direction, 0.0F, 0.0F, pMatrixStack, pBuffer, pCombinedLight, skullmodelbase, rendertype);
    }
    
    public RenderType getRenderType(Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        GameProfile pProfile = new GameProfile(player.getUUID(), player.getDisplayName().getContents());
        Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getInsecureSkinInformation(pProfile);
        return map.containsKey(Type.SKIN) ? RenderType.entityTranslucent(minecraft.getSkinManager().registerTexture(map.get(Type.SKIN), Type.SKIN)) : RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(pProfile)));
    }

    

}
