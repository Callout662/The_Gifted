package com.AstianBk.the_gifted.client;

import com.AstianBk.the_gifted.client.layers.LaserLayer;
import com.AstianBk.the_gifted.client.layers.SuperSpeedLayer;
import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.common.gui.HotBarGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
@Mod.EventBusSubscriber(modid = TheGifted.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)

public class EventClient {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerArmorRenderers(EntityRenderersEvent.AddLayers event){
        event.getSkins().forEach(s -> {
            event.getSkin(s).addLayer(new SuperSpeedLayer(event.getSkin(s)));
            event.getSkin(s).addLayer(new LaserLayer(event.getSkin(s)));
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void registerGui(RegisterGuiOverlaysEvent event){
        event.registerAbove(VanillaGuiOverlay.PLAYER_HEALTH.id(), "actiona_actually",new HotBarGui());
    }
}
