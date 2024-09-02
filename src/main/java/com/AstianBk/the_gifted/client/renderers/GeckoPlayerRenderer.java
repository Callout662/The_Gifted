package com.AstianBk.the_gifted.client.renderers;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.powers.LaserPower;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class GeckoPlayerRenderer<T extends AnimationPlayerCapability,P extends Player> extends GeoReplacedEntityRenderer<P,T> implements GeoRenderer<T> {
    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation(TheGifted.MODID,"textures/entity/laser.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);

    public final ResourceLocation textureSkin;
    public GeckoPlayerRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model,ResourceLocation textures,GeoEntity animatable) {
        super(renderManager, model, (T) animatable);
        this.textureSkin=textures;
    }

    @Override
    public ResourceLocation getTextureLocation(P entity) {
        return this.textureSkin;
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return this.textureSkin;
    }

    public void renderGeckoPlayer(Entity entity, GeoEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){
        super.render((P) entity,entityYaw,partialTick,poseStack,bufferSource,packedLight);
    }

    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, Player player, float pPartialTicks, float pNetHeadYaw, float pHeadPitch) {
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        if(cap!=null){
            if(cap.durationEffect.hasDurationForPower("laser")){
                for(int i=0;i<2;i++){
                    LaserPower power= (LaserPower) cap.powers.getForName("laser");
                    pMatrixStack.pushPose();
                    float f1 = (float)player.level().getDayTime() + pPartialTicks;
                    float f2 = f1 * 0.5F % 1.0F;
                    Vec3 vec32 = this.calculateViewVector(pHeadPitch,-pNetHeadYaw);
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
                    pMatrixStack.translate(i==0 ? 0.08F : -0.08F,1.65F,0F);
                    pMatrixStack.scale(0.5F,0.5F,0.5F);

                    pMatrixStack.mulPose(Axis.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
                    pMatrixStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));


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

    @Override
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();

        LivingEntity livingEntity = this.currentEntity;
        boolean shouldSit = this.currentEntity.isPassenger() && (this.currentEntity.getVehicle() != null && this.currentEntity.getVehicle().shouldRiderSit());
        float lerpBodyRot = livingEntity == null ? 0 : Mth.rotLerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        float lerpHeadRot = livingEntity == null ? 0 : Mth.rotLerp(partialTick, livingEntity.yHeadRotO, livingEntity.yHeadRot);
        float netHeadYaw = lerpHeadRot - lerpBodyRot;

        if (shouldSit && this.currentEntity.getVehicle() instanceof LivingEntity livingentity) {
            lerpBodyRot = Mth.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
            netHeadYaw = lerpHeadRot - lerpBodyRot;
            float clampedHeadYaw = Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85, 85);
            lerpBodyRot = lerpHeadRot - clampedHeadYaw;

            if (clampedHeadYaw * clampedHeadYaw > 2500f)
                lerpBodyRot += clampedHeadYaw * 0.2f;

            netHeadYaw = lerpHeadRot - lerpBodyRot;
        }

        if (this.currentEntity.getPose() == Pose.SLEEPING && livingEntity != null) {
            Direction bedDirection = livingEntity.getBedOrientation();

            if (bedDirection != null) {
                float eyePosOffset = livingEntity.getEyeHeight(Pose.STANDING) - 0.1F;

                poseStack.translate(-bedDirection.getStepX() * eyePosOffset, 0, -bedDirection.getStepZ() * eyePosOffset);
            }
        }

        float ageInTicks = this.currentEntity.tickCount + partialTick;
        float limbSwingAmount = 0;
        float limbSwing = 0;

        applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);

        if (!shouldSit && this.currentEntity.isAlive() && livingEntity != null) {
            limbSwingAmount = livingEntity.walkAnimation.speed(partialTick);
            limbSwing = livingEntity.walkAnimation.position(partialTick);

            if (livingEntity.isBaby())
                limbSwing *= 3f;

            if (limbSwingAmount > 1f)
                limbSwingAmount = 1f;
        }

        float headPitch = Mth.lerp(partialTick, this.currentEntity.xRotO, this.currentEntity.getXRot());
        float motionThreshold = getMotionAnimThreshold(animatable);
        boolean isMoving;

        if (livingEntity != null) {
            Vec3 velocity = livingEntity.getDeltaMovement();
            float avgVelocity = (float)(Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f;

            isMoving = avgVelocity >= motionThreshold && limbSwingAmount != 0;
        }
        else {
            isMoving = (limbSwingAmount <= -motionThreshold || limbSwingAmount >= motionThreshold);
        }

        if (!isReRender) {
            AnimationState<T> animationState = new AnimationState<T>(animatable, limbSwing, limbSwingAmount, partialTick, isMoving);
            long instanceId = getInstanceId(animatable);

            animationState.setData(DataTickets.TICK, animatable.getTick(this.currentEntity));
            animationState.setData(DataTickets.ENTITY, this.currentEntity);
            animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, livingEntity != null && livingEntity.isBaby(), -netHeadYaw, -headPitch));
            this.model.addAdditionalStateData(animatable, instanceId, animationState::setData);
            this.model.handleAnimations(animatable, instanceId, animationState);
        }


        if (renderType != null){
            updateAnimatedTextureFrame(animatable);

            for (GeoBone group : model.topLevelBones()) {
                renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight,
                        packedOverlay, red, green, blue, alpha);
            }
        }
        if(PowerPlayerCapability.get(animatable.getPlayer())!=null && PowerPlayerCapability.get(animatable.getPlayer()).durationEffect.hasDurationForPower("laser")){
            //render(poseStack,bufferSource, animatable.getPlayer(), partialTick, -netHeadYaw,-headPitch);
        }
        poseStack.translate(0, 0.01f, 0);

        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        poseStack.popPose();
    }



    protected final Vec3 calculateViewVector(float p_20172_, float p_20173_) {
        float f = p_20172_ * ((float)Math.PI / 180F);
        float f1 = -p_20173_ * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(f5), (double)(f2 * f4));
    }
    private static void vertex(VertexConsumer p_253637_, Matrix4f p_253920_, Matrix3f p_253881_, float p_253994_, float p_254492_, float p_254474_, int p_254080_, int p_253655_, int p_254133_, float p_254233_, float p_253939_) {
        p_253637_.vertex(p_253920_, p_253994_, p_254492_, p_254474_).color(p_254080_, p_253655_, p_254133_, 127).uv(p_254233_, p_253939_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_253881_, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
