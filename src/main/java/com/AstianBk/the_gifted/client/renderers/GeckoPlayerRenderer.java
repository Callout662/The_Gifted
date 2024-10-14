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

public class GeckoPlayerRenderer extends GeoReplacedEntityRenderer<Player,AnimationPlayerCapability> implements GeoRenderer<AnimationPlayerCapability> {
    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation(TheGifted.MODID,"textures/entity/laser.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);

    public final ResourceLocation textureSkin;
    public GeckoPlayerRenderer(EntityRendererProvider.Context renderManager, GeoModel<?> model,ResourceLocation textures,GeoEntity animatable) {
        super(renderManager, (GeoModel<AnimationPlayerCapability>) model, (AnimationPlayerCapability) animatable);
        this.textureSkin=textures;
    }

    @Override
    public ResourceLocation getTextureLocation(Player entity) {
        return this.textureSkin;
    }

    @Override
    public ResourceLocation getTextureLocation(AnimationPlayerCapability animatable) {
        return this.textureSkin;
    }

    public void renderGeckoPlayer(Entity entity, GeoEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){
        super.render((Player) entity,entityYaw,partialTick,poseStack,bufferSource,packedLight);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, AnimationPlayerCapability animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
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
            AnimationState<AnimationPlayerCapability> animationState = new AnimationState<AnimationPlayerCapability>(animatable, limbSwing, limbSwingAmount, partialTick, isMoving);
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
