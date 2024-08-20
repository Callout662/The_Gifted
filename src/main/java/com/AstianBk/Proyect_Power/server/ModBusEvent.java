package com.AstianBk.Proyect_Power.server;

import com.AstianBk.Proyect_Power.common.ProjectPower;
import com.AstianBk.Proyect_Power.server.capability.PowerPlayerCapability;
import com.AstianBk.Proyect_Power.server.capability.PwCapability;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class ModBusEvent {
    @SubscribeEvent
    public static void onJoinGame(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player){
            PowerPlayerCapability cap = PwCapability.getEntityPatch(event.getEntity(), PowerPlayerCapability.class);
            if(cap!=null){
                cap.onJoinGame((Player) event.getEntity(),event);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static <T extends LivingEntity,M extends EntityModel<T>> void renderEvent(RenderLivingEvent.Pre<T,M> event){
        EntityType<?> type = event.getEntity().getType();
    }


    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity() instanceof Player){
            PowerPlayerCapability cap = PwCapability.getEntityPatch(event.getEntity(), PowerPlayerCapability.class);
            if(cap!=null){
                cap.tick((Player) event.getEntity());
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        PowerPlayerCapability oldCap = PwCapability.getEntityPatch(event.getObject(), PowerPlayerCapability.class);

        if (oldCap == null) {
            PowerPlayerCapability.PowerPlayerProvider prov = new PowerPlayerCapability.PowerPlayerProvider();
            PowerPlayerCapability cap=prov.getCapability(PwCapability.POWER_CAPABILITY).orElse(null);
            if(event.getObject() instanceof Player player){
                cap.init(player);
                event.addCapability(new ResourceLocation(ProjectPower.MODID, "power_cap"), prov);
            }
        }
    }

}
