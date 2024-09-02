package com.AstianBk.the_gifted.client.renderers;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.powers.LaserPower;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderUtil {

    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation(TheGifted.MODID,"textures/entity/beacon_beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityTranslucentEmissive(GUARDIAN_BEAM_LOCATION,true);
    private static final ResourceLocation RAY = new ResourceLocation(TheGifted.MODID,"textures/entity/ray.png");


    public static void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, Player player, float pPartialTicks) {
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        if(cap!=null){
            if(cap.durationEffect.hasDurationForPower("laser")){
                for(int i=0;i<2;i++){
                    pMatrixStack.pushPose();
                    pMatrixStack.translate(0.0F,player.getEyeHeight()+0.15F,0.0F);
                    float f1 = (float)player.level().getDayTime() + pPartialTicks;
                    Vec3 vec32 = player.getLookAngle();
                    vec32 = vec32.normalize();
                    int j = 255;
                    int k = 0;
                    int l = 0;
                    float f5 = (float)Math.atan2(vec32.y, vec32.x*vec32.x+vec32.z*vec32.z);
                    float f6 = (float)Math.atan2(vec32.z, vec32.x);

                    Vec3 start = Vec3.ZERO;
                    Vec3 end;
                    var hit = LaserPower.raycastForEntity(player.level(),player,10.0F,true,0);
                    float distance = (float) player.getEyePosition(pPartialTicks).distanceTo(hit.getLocation());
                    float radius = 0.12f;
                    pMatrixStack.mulPose(Axis.YP.rotation(-(f6 - 1.5707f)));
                    pMatrixStack.mulPose(Axis.XP.rotation(-(f5)));
                    pMatrixStack.translate(i==0 ? 0.08F : -0.08F,0.0F,0.2F);
                    float deltaTicks = player.tickCount + pPartialTicks;
                    float deltaUV = -deltaTicks % 10;
                    float max = Mth.frac(deltaUV * 0.2F - (float) Mth.floor(deltaUV * 0.1F));
                    float min = -1.0F + max;
                    VertexConsumer vertexconsumer = pBuffer.getBuffer(BEAM_RENDER_TYPE);
                    PoseStack.Pose posestack$pose = pMatrixStack.last();

                    for (int i1 = 1; i1 <= distance; i1++) {
                        Vec3 wiggle = new Vec3(
                                Mth.sin(deltaTicks * .8f) * .02f,
                                Mth.sin(deltaTicks * .8f + 100) * .02f,
                                Mth.cos(deltaTicks * .8f) * .02f
                        );
                        end = new Vec3(0, 0, Math.min(i1, distance)).add(wiggle);
                        drawHull(start, end, radius, radius, posestack$pose, vertexconsumer, j, k, l, 127, min, max);
                        VertexConsumer outer = pBuffer.getBuffer(RenderType.entityTranslucent(RAY));
                        drawQuad(start, end, radius * 4f, 0,posestack$pose, outer,  j, k, l, 127, min, max);
                        drawQuad(start, end, 0, radius * 4f, posestack$pose, outer,  j, k, l, 127, min, max);

                        start=end;
                    }

                    pMatrixStack.popPose();
                }
            }
        }
    }

    private static void drawHull(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        //Bottom
        drawQuad(from.subtract(0, height * .5f, 0), to.subtract(0, height * .5f, 0), width, 0, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Top
        drawQuad(from.add(0, height * .5f, 0), to.add(0, height * .5f, 0), width, 0, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Left
        drawQuad(from.subtract(width * .5f, 0, 0), to.subtract(width * .5f, 0, 0), 0, height, pose, consumer, r, g, b, a, uvMin, uvMax);
        //Right
        drawQuad(from.add(width * .5f, 0, 0), to.add(width * .5f, 0, 0), 0, height, pose, consumer, r, g, b, a, uvMin, uvMax);
    }

    private static void drawQuad(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        float halfWidth = width * .5f;
        float halfHeight = height * .5f;

        consumer.vertex(poseMatrix, (float) from.x - halfWidth, (float) from.y - halfHeight, (float) from.z).color(r, g, b, a).uv(0f, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) from.x + halfWidth, (float) from.y + halfHeight, (float) from.z).color(r, g, b, a).uv(1f, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) to.x + halfWidth, (float) to.y + halfHeight, (float) to.z).color(r, g, b, a).uv(1f, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, (float) to.x - halfWidth, (float) to.y - halfHeight, (float) to.z).color(r, g, b, a).uv(0f, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0f, 1f, 0f).endVertex();

    }

}
