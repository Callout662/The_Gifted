package com.AstianBk.the_gifted.client.models;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GeckoPlayerModel<T extends GeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return new ResourceLocation(TheGifted.MODID,"geo/gecko_player/gecko_player.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return new ResourceLocation(TheGifted.MODID,"animations/gecko_player/gecko_player.animation.json");
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone main = (GeoBone) this.getAnimationProcessor().getBone("Main");
        GeoBone head = (GeoBone) this.getAnimationProcessor().getBone("Head");
        EntityModelData data=animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        Player player= (Player) animationState.getData(DataTickets.ENTITY);
        if(PowerPlayerCapability.get(player)!=null && PowerPlayerCapability.get(player).durationEffect.hasDurationForPower("fly") && player.isSprinting()){
            main.setPosY(20);
            main.setRotX((-90.0F+data.headPitch()) * ((float) Math.PI / 180F));
        }
        head.setRotX(data.headPitch() * ((float) Math.PI / 180F));
        head.setRotY(data.netHeadYaw() * ((float) Math.PI / 180F));

    }
}
