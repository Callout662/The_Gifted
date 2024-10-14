package com.AstianBk.the_gifted.common.mixins;

import com.AstianBk.the_gifted.client.renderers.RenderUtil;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public class EntityRenderDispatcherMixin<T extends Entity> {

    @Shadow @Final private RenderBuffers renderBuffers;

    @Inject(method = "renderLevel",at = @At("TAIL"))
    public void renderHeatVision(PoseStack p_109600_, float p_109601_, long p_109602_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_254120_, CallbackInfo ci){
        if(p_109604_.getEntity()==Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson() && PowerPlayerCapability.get(Minecraft.getInstance().player)!=null){
            MultiBufferSource.BufferSource multibuffersource$buffersource = this.renderBuffers.bufferSource();
            RenderUtil.render(p_109600_,multibuffersource$buffersource, Minecraft.getInstance().player,p_109601_,true,false);
        }
    }
}
