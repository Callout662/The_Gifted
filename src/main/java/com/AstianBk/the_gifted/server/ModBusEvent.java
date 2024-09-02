package com.AstianBk.the_gifted.server;

import com.AstianBk.the_gifted.client.models.GeckoPlayerModel;
import com.AstianBk.the_gifted.client.renderers.GeckoPlayerRenderer;
import com.AstianBk.the_gifted.client.renderers.RenderUtil;
import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PwCapability;
import com.AstianBk.the_gifted.server.powers.LaserPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoEntity;

@Mod.EventBusSubscriber()
public class ModBusEvent {
    @SubscribeEvent
    public static void onJoinGame(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player){
            PowerPlayerCapability cap = PwCapability.getEntityCap(event.getEntity(), PowerPlayerCapability.class);
            if(cap!=null){
                cap.onJoinGame((Player) event.getEntity(),event);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event){
        ItemStack stack=event.getEntity().getItemInHand(event.getHand());
        if(stack.is(Items.STICK)){
            PowerPlayerCapability cap = PwCapability.getEntityCap(event.getEntity(), PowerPlayerCapability.class);
            if(cap!=null){
                event.getEntity().sendSystemMessage(Component.nullToEmpty("Se a subido un nivel al poder "+cap.getSelectPower()+" su nivel paso de "+cap.getSelectPower().level +" -> "+(cap.getSelectPower().level+1)));
                cap.getSelectPower().level=Math.min(cap.getSelectPower().level+1,5);
            }
        }
    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity() instanceof Player){
            PowerPlayerCapability cap = PwCapability.getEntityCap(event.getEntity(), PowerPlayerCapability.class);
            if(cap!=null){
                cap.tick((Player) event.getEntity());
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        PowerPlayerCapability oldCap = PwCapability.getEntityCap(event.getObject(), PowerPlayerCapability.class);

        if (oldCap == null) {
            PowerPlayerCapability.PowerPlayerProvider prov = new PowerPlayerCapability.PowerPlayerProvider();
            PowerPlayerCapability cap=prov.getCapability(PwCapability.POWER_CAPABILITY).orElse(null);
            if(event.getObject() instanceof Player player){
                cap.init(player);
                event.addCapability(new ResourceLocation(TheGifted.MODID, "power_cap"), prov);
            }
        }

        AnimationPlayerCapability oldPatch=PwCapability.getEntityPatch(event.getObject(), AnimationPlayerCapability.class);
        if (oldPatch==null){
            AnimationPlayerCapability.AnimationPlayerProvider prov = new AnimationPlayerCapability.AnimationPlayerProvider();
            AnimationPlayerCapability cap=prov.getCapability(PwCapability.ANIMATION_CAPABILITY).orElse(null);
            if(event.getObject() instanceof Player player){
                cap.init(player);
                event.addCapability(new ResourceLocation(TheGifted.MODID, "animation_patch"), prov);
            }
        }
    }

}
