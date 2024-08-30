package com.AstianBk.the_gifted.client.layers;

import com.AstianBk.the_gifted.common.register.PWEffects;
import com.AstianBk.the_gifted.common.register.PWPower;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class SuperSpeedLayer<T extends Player,M extends EntityModel<T>> extends RenderLayer<T,M>{

    private final PlayerModel<T> model;

    public SuperSpeedLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.model= new PlayerModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER),false);
    }
    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T player, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        if(cap!=null){
            if(cap.durationEffect.hasDurationForSpell("super_speed")){
                pMatrixStack.pushPose();

                Vec3 renderingAt = new Vec3(Mth.lerp(pPartialTicks, player.xo, player.getX()), Mth.lerp(pPartialTicks, player.yo, player.getY()), Mth.lerp(pPartialTicks, player.zo, player.getZ()));
                Vec3 pos=player.getPosition(pPartialTicks);
                Vec3 delta=player.getDeltaMovement();
                Vec3 backPos=pos.add(-delta.x*2,-delta.y,-delta.z*2);
                for(int i=0;i<3;i++){
                    Vec3 move=backPos.subtract(renderingAt);
                    Vec3 moveOri=pos.subtract(renderingAt);
                    Vec3 lastPos=moveOri.subtract(move);
                    if(lastPos.z<0){
                        lastPos= lastPos.multiply(1.0f,1.0f,-1.0f);
                    }
                    if(lastPos.x<0){
                        lastPos=lastPos.multiply(-1.0f,1.0f,1.0f);
                    }
                    double d0=lastPos.z+lastPos.x;
                    pMatrixStack.translate(0,lastPos.y,d0);
                    this.getParentModel().copyPropertiesTo(this.model);
                    this.model.setupAnim(player,pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                    this.model.renderToBuffer(pMatrixStack,pBuffer.getBuffer(RenderType.entityTranslucentEmissive(this.getTextureLocation(player))),pPackedLight, OverlayTexture.NO_OVERLAY,0.0f,0.0f,0.1f,0.2f);
                    pos=backPos;
                    backPos=backPos.add(-delta.x*2,-delta.y,-delta.z*2);
                }
                pMatrixStack.popPose();
            }
        }
    }
}