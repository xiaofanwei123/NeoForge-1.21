package com.xiaofanwei.xfws_someitems.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;

public class ParryParticle extends Particle {
    private final Font font;
    private final Component text; // 需要显示的文字
    private final int textColor; // 文字颜色（RGB）
    private final int shadowColor; // 阴影颜色
    private float scale = 0.01f; // 基础缩放比例
    private float fadeout = 1.0f; // 淡出控制（1为完全显示，0为消失）
    private float tiltAngle= 30f * Mth.DEG_TO_RAD;

    public ParryParticle(ClientLevel level, double x, double y, double z, Component text, int color, int lifetime) {
        super(level, x, y, z);
        this.text = text;
        this.font = Minecraft.getInstance().font;
        this.textColor = color;
        this.shadowColor = FastColor.ARGB32.color(255,
                (int)(FastColor.ARGB32.red(color) * 0.25f),
                (int)(FastColor.ARGB32.green(color) * 0.25f),
                (int)(FastColor.ARGB32.blue(color) * 0.25f));
        this.lifetime = lifetime; //粒子存在时间
        this.hasPhysics = false; //无物理效果
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, xo, this.x) - camPos.x());
        float y = (float)(Mth.lerp(partialTicks, yo, this.y) - camPos.y());
        float z = (float)(Mth.lerp(partialTicks, zo, this.z) - camPos.z());

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.XP.rotation(tiltAngle));
        poseStack.mulPose(camera.rotation());//始终面向摄像机
        float distanceScale = (float)camPos.distanceTo(this.getPos());//根据距离动态缩放
        poseStack.scale(scale * distanceScale, -scale * distanceScale, scale * distanceScale);
        poseStack.translate(0, 4 * (1 - fadeout), 0);//淡出
        poseStack.scale(fadeout, fadeout, fadeout);
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        font.drawInBatch(text, -font.width(text)/2f, 0,
                textColor,
                false,
                poseStack.last().pose(), buffer,
                DisplayMode.NORMAL, 0, 15728880);
        //轻微偏移阴影
        poseStack.translate(0.7, 0.7, -0.1);
        font.drawInBatch(text, -font.width(text)/2f, 0,
                shadowColor, false,
                poseStack.last().pose(), buffer,
                DisplayMode.NORMAL, 0, 15728880);
        buffer.endBatch();
        poseStack.popPose();
    }

    @Override
    public void tick() {
        super.tick();
        float fadeoutLength = 30f; //所需淡出时间
//        float shorttime = 20f;
        float shorttime = 0;
        if (age > lifetime - fadeoutLength) {
            fadeout = (float)(lifetime - age) / fadeoutLength;
//            if(age == lifetime - shorttime){
//                fadeout = 0;
//                this.remove();
//            }
        }
        tiltAngle -= tiltAngle / (lifetime-shorttime);
        y += 0.0005f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class ParticleFactory implements ParticleProvider<SimpleParticleType> {
        public ParticleFactory(SpriteSet spriteSet) {
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParryParticle(worldIn, x, y, z, Component.translatable("tooltip.item.xfws_someitems.ark_of_the_cosmos.3"), Color.HSBtoRGB(System.currentTimeMillis() % 10000 / 10000F,1f, 1F), 60);
        }
    }
}
