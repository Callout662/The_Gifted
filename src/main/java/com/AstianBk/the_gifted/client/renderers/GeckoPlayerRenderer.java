package com.AstianBk.the_gifted.client.renderers;

import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class GeckoPlayerRenderer<T extends AnimationPlayerCapability,P extends Player> extends GeoReplacedEntityRenderer<P,T> implements GeoRenderer<T> {
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
}
