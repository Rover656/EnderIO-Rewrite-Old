package com.enderio.base.client.renderers;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.enderio.base.common.blockentity.GraveBlockEntity;
import com.enderio.base.common.capability.EIOCapabilities;
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

import javax.annotation.Nonnull;

//renders grave as a playerskull
public class GraveRenderer implements BlockEntityRenderer<BlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public GraveRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(@Nonnull BlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, @Nonnull MultiBufferSource pBuffer, int pCombinedLight,
        int pCombinedOverlay) {
        GraveBlockEntity grave = (GraveBlockEntity) pBlockEntity;
        Direction direction = null;//TODO if we make the grave rotatable
        SkullModelBase skullmodelbase = new SkullModel(this.context.bakeLayer(ModelLayers.PLAYER_HEAD));
        AtomicReference<RenderType> rendertype = new AtomicReference<>(RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin()));
        grave.getCapability(EIOCapabilities.OWNER).ifPresent((cap) -> {
            if (cap.getUUID() != null) {
                // TODO: Deal with the ugly null checks...
                Player player = Objects.requireNonNull(pBlockEntity.getLevel()).getPlayerByUUID(cap.getUUID());
                rendertype.set(getRenderType(Objects.requireNonNull(player)));
            }
        });
        pMatrixStack.pushPose();
        pMatrixStack.translate(1, 1, 0);
        pMatrixStack.popPose();
        SkullBlockRenderer.renderSkull(direction, 0.0F, 0.0F, pMatrixStack, pBuffer, pCombinedLight, skullmodelbase, rendertype.get());
    }

    public RenderType getRenderType(Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        GameProfile pProfile = new GameProfile(player.getUUID(), player.getDisplayName().getContents());
        Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getInsecureSkinInformation(pProfile);
        return map.containsKey(Type.SKIN) ?
            RenderType.entityTranslucent(minecraft.getSkinManager().registerTexture(map.get(Type.SKIN), Type.SKIN)) :
            RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(pProfile)));
    }

}
