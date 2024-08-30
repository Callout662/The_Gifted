package com.AstianBk.the_gifted.client.layers;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.common.register.PWEffects;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.powers.LaserPower;
import com.AstianBk.the_gifted.server.powers.Power;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


@OnlyIn(Dist.CLIENT)
public class LaserLayer<T extends Player,M extends EntityModel<T>> extends RenderLayer<T,M>{
    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation(TheGifted.MODID,"textures/entity/laser.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);


    public LaserLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }
    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T player, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        if(cap!=null){
            if(cap.durationEffect.hasDurationForSpell("laser")){
                for(int i=0;i<2;i++){
                    LaserPower power= (LaserPower) cap.powers.getForName("laser");
                    pMatrixStack.pushPose();
                    float f1 = (float)player.level().getDayTime() + pPartialTicks;
                    float f2 = f1 * 0.5F % 1.0F;
                    Vec3 vec32 = this.calculateViewVector(pHeadPitch,-pNetHeadYaw).reverse();
                    float f4 = (float)(power.start.subtract(power.end).length() + 1.0D);
                    vec32 = vec32.normalize();
                    int j = 255;
                    int k = 0;
                    int l = 0;
                    float f5 = (float)Math.acos(vec32.y);
                    float f6 = (float)Math.atan2(vec32.z, vec32.x);
                    float f7 = 1.0F;

                    float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
                    float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
                    float f13 = Mth.cos(f7 + ((float)Math.PI / 4F)) * 0.282F;
                    float f14 = Mth.sin(f7 + ((float)Math.PI / 4F)) * 0.282F;
                    float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
                    float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
                    float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
                    float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
                    float f19 = Mth.cos(f7 + (float)Math.PI) * 0.2F;
                    float f20 = Mth.sin(f7 + (float)Math.PI) * 0.2F;
                    float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
                    float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
                    float f23 = Mth.cos(f7 + ((float)Math.PI / 2F)) * 0.2F;
                    float f24 = Mth.sin(f7 + ((float)Math.PI / 2F)) * 0.2F;
                    float f25 = Mth.cos(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
                    float f26 = Mth.sin(f7 + ((float)Math.PI * 1.5F)) * 0.2F;

                    pMatrixStack.mulPose(Axis.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
                    pMatrixStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
                    pMatrixStack.translate(i==0 ? 0.08F : -0.08F,0,0.2F);
                    pMatrixStack.scale(0.5F,0.5F,0.5F);
                    VertexConsumer vertexconsumer = pBuffer.getBuffer(BEAM_RENDER_TYPE);
                    PoseStack.Pose posestack$pose = pMatrixStack.last();
                    Matrix4f matrix4f = posestack$pose.pose();
                    Matrix3f matrix3f = posestack$pose.normal();
                    float f29 = -1.0F + f2;
                    float f30 = 20.0F * 2.5F + f29;
                    vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
                    vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
                    vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
                    vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
                    vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
                    vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
                    vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
                    vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
                    float f31 = 0.0F;
                    if (player.tickCount % 2 == 0) {
                        f31 = 0.5F;
                    }

                    vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
                    vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
                    vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
                    vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);

                    pMatrixStack.popPose();
                }
            }
        }
    }
    protected final Vec3 calculateViewVector(float p_20172_, float p_20173_) {
        float f = p_20172_ * ((float)Math.PI / 180F);
        float f1 = -p_20173_ * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }
    private static void vertex(VertexConsumer p_253637_, Matrix4f p_253920_, Matrix3f p_253881_, float p_253994_, float p_254492_, float p_254474_, int p_254080_, int p_253655_, int p_254133_, float p_254233_, float p_253939_) {
        p_253637_.vertex(p_253920_, p_253994_, p_254492_, p_254474_).color(p_254080_, p_253655_, p_254133_, 255).uv(p_254233_, p_253939_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_253881_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private Vec3 getPosition(LivingEntity pLivingEntity, double p_114804_, float p_114805_) {
        double d0 = Mth.lerp((double)p_114805_, pLivingEntity.xOld, pLivingEntity.getX());
        double d1 = Mth.lerp((double)p_114805_, pLivingEntity.yOld, pLivingEntity.getY()) + p_114804_;
        double d2 = Mth.lerp((double)p_114805_, pLivingEntity.zOld, pLivingEntity.getZ());
        return new Vec3(d0, d1, d2);
    }
}