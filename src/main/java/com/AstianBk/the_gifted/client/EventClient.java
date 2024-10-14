package com.AstianBk.the_gifted.client;

import com.AstianBk.the_gifted.client.layers.RegenerationLayer;
import com.AstianBk.the_gifted.client.layers.SuperSpeedLayer;
import com.AstianBk.the_gifted.client.particle.PWParticles;
import com.AstianBk.the_gifted.client.particle.custom.BloodBKParticles;
import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.common.gui.HotBarGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid = TheGifted.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class EventClient {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerArmorRenderers(EntityRenderersEvent.AddLayers event){
        event.getSkins().forEach(s -> {
            event.getSkin(s).addLayer(new RegenerationLayer(event.getSkin(s)));
            event.getSkin(s).addLayer(new SuperSpeedLayer(event.getSkin(s)));
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerGui(RegisterGuiOverlaysEvent event){
        event.registerAbove(VanillaGuiOverlay.PLAYER_HEALTH.id(), "actiona_actually",new HotBarGui());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        if(PWParticles.BLOOD_PARTICLES.isPresent()){
            event.registerSpriteSet(PWParticles.BLOOD_PARTICLES.get(), BloodBKParticles.Factory::new);
        }
    }

}
