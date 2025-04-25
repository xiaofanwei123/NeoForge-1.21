package com.xiaofanwei.xfws_someitems.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.xiaofanwei.xfws_someitems.MoreAC;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ManaStarRenderer extends EntityRenderer<ManaStar> {
    public ManaStarRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.shadowRadius = 0F;
        this.shadowStrength = 0.75F;
    }

    @Override
    protected int getBlockLightLevel(ManaStar entity, BlockPos pos) {
        return Mth.clamp(super.getBlockLightLevel(entity, pos) + 8, 0, 15);
    }

    @Override
    public void render(ManaStar entity, float yaw, float pitch, PoseStack poseStack, MultiBufferSource buffer, int light) {
        //阴影
        this.shadowRadius = 0.001F;

        poseStack.pushPose();

        VertexConsumer consumer = buffer.getBuffer(RenderType.itemEntityTranslucentCull(getTextureLocation(entity)));
        PoseStack.Pose pose = poseStack.last();

        float scale = (float) (0.5F + Math.sin(entity.tickCount * 0.1F) * 0.05F);

        poseStack.scale(scale, scale, scale);

        //高阶段实体渲染位置更高
        poseStack.translate(0.0F, 0.3F, 0.0F);

        //始终面向玩家视角
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

        //透明度呼吸效果
        int alpha = (int) Math.min(255, 255 * (0.75F + Math.sin(entity.tickCount * 0.25F) * 0.1F));

        vertex(consumer, pose, -0.5F, -0.5F, alpha, 0, 1);
        vertex(consumer, pose, 0.5F, -0.5F, alpha, 1, 1);
        vertex(consumer, pose, 0.5F, 0.5F, alpha, 1, 0);
        vertex(consumer, pose, -0.5F, 0.5F, alpha, 0, 0);

        poseStack.popPose();

        super.render(entity, yaw, pitch, poseStack, buffer, light);
    }

    //定义渲染实体的顶点数据
    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, int alpha, float u, float v) {
        consumer.addVertex(pose, x, y, 0F).setColor(255, 255, 255, alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0F, 1F, 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(ManaStar entity) {
        return ResourceLocation.fromNamespaceAndPath(MoreAC.MODID, "textures/entity/mana_star.png");
    }
}