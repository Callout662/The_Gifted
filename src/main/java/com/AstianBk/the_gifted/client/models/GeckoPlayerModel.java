package com.AstianBk.the_gifted.client.models;

import com.AstianBk.the_gifted.common.TheGifted;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;

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
}
