package com.AstianBk.the_gifted.client.layers;

import com.AstianBk.the_gifted.client.renderers.RenderUtil;
import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.manager.RegenerationInstance;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@OnlyIn(Dist.CLIENT)
public class RegenerationLayer<T extends Player,M extends EntityModel<T>> extends RenderLayer<T,M>{
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    private static final ResourceLocation MUSCLE_LOCATION = new ResourceLocation(TheGifted.MODID,"textures/entity/muscle.png");
    private static final ResourceLocation TRANS_LOCATION = new ResourceLocation(TheGifted.MODID,"textures/entity/trans.png");
    private static final RenderType DECAL_RENDER=RenderType.entityDecal(MUSCLE_LOCATION);
    
    private final HumanoidModel<T> model;
    private final HumanoidModel<T> modelHuman;
    private final HumanoidModel<T> modelHumanReg;
    public RegenerationLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.model= new HumanoidModel<T>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.SKELETON));
        this.modelHuman= new HumanoidModel<T>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER));
        this.modelHumanReg= new HumanoidModel<T>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER));

    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T player, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        if(cap!=null && player.isAlive()){
            if(cap.getHotBarPower().hasPower("super_regeneration")){
                if(cap.getLimbsPartRegeneration().hasRegenerationLimbs()){
                    pMatrixStack.pushPose();
                    this.initModel(this.model,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                    this.initModel(this.modelHuman,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                    this.initModel(this.modelHumanReg,player,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);
                    cap.getLimbsPartRegeneration().getLimbs().forEach(limb->{
                        RegenerationInstance instance=cap.getLimbsPartRegeneration().loseLimbs.get(limb.name().toLowerCase());
                        if(instance!=null){
                            int res= (int) (instance.getRegerationTimer()*0.5F);
                            float porcent=((float) instance.getRegerationTimerRemaining()-res)/ res;
                            float porcentReg= 1.0F-porcent;
                            ModelPart part= RenderUtil.getModelPartForLimbs(limb, this.model);
                            if(porcentReg>0.0F){
                                if(porcentReg<1.0F){
                                    part.yScale=Math.min(porcentReg,0.9F);
                                }else {
                                    part.xScale=0.9F;
                                    part.yScale=0.9F;
                                    part.zScale=0.9F;
                                }
                                VertexConsumer vertexConsumer2=pBuffer.getBuffer(RenderType.entityTranslucent(SKELETON_LOCATION));
                                part.render(pMatrixStack,vertexConsumer2,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F, 1F);
                            }
                        }
                    });
                    if(!cap.getLimbsPartRegeneration().getLimbsMuscle().isEmpty()){
                        cap.getLimbsPartRegeneration().getLimbsMuscle().forEach(limb->{
                            RegenerationInstance instance=cap.getLimbsPartRegeneration().loseLimbs.get(limb.name().toLowerCase());
                            if(instance!=null){
                                int res= (int) (instance.getRegerationTimer()*0.35F);
                                float porcent=((float) instance.getRegerationTimerRemaining()-instance.getRegerationTimer()*0.15F)/ res;
                                ModelPart part= RenderUtil.getModelPartForLimbs(limb, this.modelHuman);
                                if(porcent>0.0F){
                                    VertexConsumer vertexConsumer1=pBuffer.getBuffer(RenderType.dragonExplosionAlpha(TRANS_LOCATION));
                                    part.render(pMatrixStack,vertexConsumer1,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,porcent);
                                    VertexConsumer vertexConsumer2=pBuffer.getBuffer(DECAL_RENDER);
                                    part.render(pMatrixStack,vertexConsumer2,pPackedLight,OverlayTexture.pack(0.0F, false),1.0F,1.0F,1.0F, 1F);
                                }else {
                                    ModelPart part1=RenderUtil.getModelPartForLimbs(limb,this.modelHumanReg);
                                    int res1= (int) (instance.getRegerationTimer()*0.15F);
                                    float porcent1=((float) instance.getRegerationTimerRemaining())/ res1;
                                    part1.xScale=0.9F;
                                    part1.yScale=0.9F;
                                    part1.zScale=0.9F;
                                    VertexConsumer vertexConsumer1=pBuffer.getBuffer(RenderType.entityTranslucent(MUSCLE_LOCATION));
                                    part1.render(pMatrixStack,vertexConsumer1,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,1.0F);
                                    VertexConsumer vertexConsumer=pBuffer.getBuffer(RenderType.dragonExplosionAlpha(TRANS_LOCATION));
                                    part.render(pMatrixStack,vertexConsumer,pPackedLight,OverlayTexture.NO_OVERLAY,1.0F,1.0F,1.0F,porcent1);
                                    VertexConsumer vertexConsumer2=pBuffer.getBuffer(RenderType.entityDecal(this.getTextureLocation(player)));
                                    part.render(pMatrixStack,vertexConsumer2,pPackedLight,OverlayTexture.pack(0.0F, false),1.0F,1.0F,1.0F, 1F);
                                }
                            }
                        });
                    }
                    pMatrixStack.popPose();
                }
            }
        }
    }
    public void initModel(HumanoidModel<T> model, T player, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        this.getParentModel().copyPropertiesTo((EntityModel<T>) model);
        model.prepareMobModel(player,pLimbSwing, 0.0F, 0.0F);
        model.setupAnim(player,pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

    }
}