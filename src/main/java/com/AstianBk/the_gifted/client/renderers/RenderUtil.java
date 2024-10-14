package com.AstianBk.the_gifted.client.renderers;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.common.api.Limbs;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.powers.LaserPower;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class RenderUtil {

    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation(TheGifted.MODID,"textures/entity/beacon_beam.png");

    public static void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, LivingEntity player, float pPartialTicks,boolean isFirstPerson,boolean isGeckoModel) {
        PowerPlayerCapability cap=PowerPlayerCapability.get((Player) player);
        if(cap!=null && cap.durationEffect!=null){
            if(cap.durationEffect.hasDurationForPower("laser")){
                for(int i=0;i<2;i++){
                    pMatrixStack.pushPose();
                    Vec3 vec32 = player.getLookAngle();
                    vec32 = vec32.normalize();
                    float f5 = (float)Math.atan2(vec32.y, vec32.x*vec32.x+vec32.z*vec32.z);
                    float f6 = (float)Math.atan2(vec32.z, vec32.x);
                    var hit = LaserPower.raycastForEntity(player.level(),player,200.0F,true,0);
                    float distance = (float) player.getEyePosition(pPartialTicks).distanceTo(hit.getLocation()) * 4;
                    if(!isFirstPerson){
                        pMatrixStack.translate(0.0F,player.getEyeHeight(),0.0F);
                    }
                    pMatrixStack.mulPose(Axis.YP.rotation(-(f6-1.57F)));
                    pMatrixStack.mulPose(Axis.XP.rotation(-(f5-1.57F)));
                    pMatrixStack.translate(i==0 ? 0.115F : -0.115F,0.0f,0.0F);

                    pMatrixStack.scale(0.25F,0.25F,0.25F);
                    for (int i1 = 1; i1 <= distance; i1++) {
                        float[] f1={1.0F,0.0F,0.0F};
                        renderBeaconBeam(pMatrixStack,pBuffer, GUARDIAN_BEAM_LOCATION, pPartialTicks, 1.0F, player.level().getGameTime(), i1, (int) distance,f1, 0.2F, 0.25F);
                    }

                    pMatrixStack.popPose();
                }
            }
        }
    }
    public static ModelPart getModelPartForLimbs(Limbs limbs, HumanoidModel<?> model){
        ModelPart modelParts=null;
        switch (limbs){
            case HEAD -> {
                modelParts=model.head;
            }
            case BODY -> {
                modelParts=model.body;
            }
            case RIGHT_ARM -> {
                modelParts=model.rightArm;
            }
            case LEFT_ARM -> {
                modelParts=model.leftArm;
            }
            case RIGHT_LEG -> {
                modelParts=model.rightLeg;
            }
            case LEFT_LEG -> {
                modelParts=model.leftLeg;
            }
        }
        return modelParts;
    }

    public static List<ModelPart> getListModelPart(List<Limbs> limbsList, PlayerModel<?> model){
        List<ModelPart> modelParts=new ArrayList<>();
        limbsList.forEach(e->{
            modelParts.addAll(getModelPartForLimbs(e,model));
        });
        return modelParts;
    }
    public static List<ModelPart> getModelPartForLimbs(Limbs limbs, PlayerModel<?> model){
        List<ModelPart> modelParts=new ArrayList<>();
        switch (limbs){
            case HEAD -> {
                modelParts.add(model.head);
            }
            case BODY -> {
                modelParts.add(model.body);
            }
            case RIGHT_ARM -> {
                modelParts.add(model.rightArm);
                modelParts.add(model.rightSleeve);
            }
            case LEFT_ARM -> {
                modelParts.add(model.leftArm);
                modelParts.add(model.leftSleeve);
            }
            case RIGHT_LEG -> {
                modelParts.add(model.rightLeg);
            }
            case LEFT_LEG -> {
                modelParts.add(model.leftLeg);
            }
        }
        return modelParts;
    }

    public static void renderBeaconBeam(PoseStack p_112185_, MultiBufferSource p_112186_, ResourceLocation p_112187_, float p_112188_, float p_112189_, long p_112190_, int p_112191_, int p_112192_, float[] p_112193_, float p_112194_, float p_112195_) {
        int i = p_112191_ + p_112192_;
        float f = (float)Math.floorMod(p_112190_, 40) + p_112188_;
        float f1 = p_112192_ < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float)Mth.floor(f1 * 0.1F));
        float f3 = p_112193_[0];
        float f4 = p_112193_[1];
        float f5 = p_112193_[2];
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f9 = -p_112194_;
        float f12 = -p_112194_;
        float f15 = -1.0F + f2;
        float f16 = (float)p_112192_ * p_112189_ * (0.5F / p_112194_) + f15;
        renderPart(p_112185_, p_112186_.getBuffer(RenderType.eyes(p_112187_)), 1.0F, 1.0F, 1.0F, 0.1F, p_112191_, i, 0.0F, p_112194_, p_112194_, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
        f6 = -p_112195_;
        float f7 = -p_112195_;
        f8 = -p_112195_;
        f9 = -p_112195_;
        f15 = -1.0F + f2;
        f16 = (float)p_112192_ * p_112189_ + f15;
        renderPart(p_112185_, p_112186_.getBuffer(RenderType.eyes(p_112187_)), f3, f4, f5, 1.0F, p_112191_, i, f6, f7, p_112195_, f8, f9, p_112195_, p_112195_, p_112195_, 0.0F, 1.0F, f16, f15);
    }

    private static void renderPart(PoseStack p_112156_, VertexConsumer p_112157_, float p_112158_, float p_112159_, float p_112160_, float p_112161_, int p_112162_, int p_112163_, float p_112164_, float p_112165_, float p_112166_, float p_112167_, float p_112168_, float p_112169_, float p_112170_, float p_112171_, float p_112172_, float p_112173_, float p_112174_, float p_112175_) {
        PoseStack.Pose posestack$pose = p_112156_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_, p_112164_, p_112165_, p_112166_, p_112167_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_, p_112170_, p_112171_, p_112168_, p_112169_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_, p_112166_, p_112167_, p_112170_, p_112171_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, p_112157_, p_112158_, p_112159_, p_112160_, p_112161_, p_112162_, p_112163_, p_112168_, p_112169_, p_112164_, p_112165_, p_112172_, p_112173_, p_112174_, p_112175_);
    }

    private static void renderQuad(Matrix4f p_253960_, Matrix3f p_254005_, VertexConsumer p_112122_, float p_112123_, float p_112124_, float p_112125_, float p_112126_, int p_112127_, int p_112128_, float p_112129_, float p_112130_, float p_112131_, float p_112132_, float p_112133_, float p_112134_, float p_112135_, float p_112136_) {
        addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112128_, p_112129_, p_112130_, p_112134_, p_112135_);
        addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112127_, p_112129_, p_112130_, p_112134_, p_112136_);
        addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112127_, p_112131_, p_112132_, p_112133_, p_112136_);
        addVertex(p_253960_, p_254005_, p_112122_, p_112123_, p_112124_, p_112125_, p_112126_, p_112128_, p_112131_, p_112132_, p_112133_, p_112135_);
    }

    private static void addVertex(Matrix4f p_253955_, Matrix3f p_253713_, VertexConsumer p_253894_, float p_253871_, float p_253841_, float p_254568_, float p_254361_, int p_254357_, float p_254451_, float p_254240_, float p_254117_, float p_253698_) {
        p_253894_.vertex(p_253955_, p_254451_, (float)p_254357_, p_254240_).color(p_253871_, p_253841_, p_254568_, p_254361_).uv(p_254117_, p_253698_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_253713_, 0.0F, 1.0F, 0.0F).endVertex();
    }

}
