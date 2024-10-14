package com.AstianBk.the_gifted.client;

import com.AstianBk.the_gifted.client.models.GeckoPlayerModel;
import com.AstianBk.the_gifted.client.particle.PWParticles;
import com.AstianBk.the_gifted.client.renderers.GeckoPlayerRenderer;
import com.AstianBk.the_gifted.client.renderers.RenderUtil;
import com.AstianBk.the_gifted.common.api.Limbs;
import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PwCapability;
import com.AstianBk.the_gifted.server.manager.RegenerationInstance;
import com.AstianBk.the_gifted.server.powers.LaserPower;
import com.AstianBk.the_gifted.server.powers.Power;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModEventClient {
    @SubscribeEvent
    public static void renderEvent(RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event){
        boolean isGeckoModel=false;
        if(event.getEntity() instanceof Player player && PowerPlayerCapability.get(player).durationEffect.hasDurationForPower("fly")){
            Minecraft mc = Minecraft.getInstance();
            Entity entity = event.getEntity();

            EntityRendererProvider.Context context = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(),
                    mc.getItemRenderer(),mc.getBlockRenderer(),mc.gameRenderer.itemInHandRenderer,
                    mc.getResourceManager(),mc.getEntityModels(),mc.font);
            AnimationPlayerCapability animatable = PwCapability.getEntityPatch(event.getEntity(), AnimationPlayerCapability.class);
            EntityRenderer renderer= new GeckoPlayerRenderer(context,new GeckoPlayerModel(),((PlayerRenderer)event.getRenderer()).getTextureLocation((AbstractClientPlayer)entity),animatable);
            if(renderer instanceof GeckoPlayerRenderer geoRenderer  && animatable!=null){
                //geoRenderer.setCurrentEntity(event.getEntity());
                entity.setInvisible(true);
                isGeckoModel=true;
                geoRenderer.renderGeckoPlayer(entity,animatable,0.0F,event.getPartialTick(),event.getPoseStack(),event.getMultiBufferSource(),event.getPackedLight());
            }
        }
        if(event.getEntity() instanceof Player player){
            RenderUtil.render(event.getPoseStack(),event.getMultiBufferSource(),player,event.getPartialTick(),false,isGeckoModel);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void clientTick(TickEvent.PlayerTickEvent event){
        if (event.side.isClient() && event.phase == TickEvent.Phase.END && event.player!=null) {
            PowerPlayerCapability cap = PowerPlayerCapability.get(event.player);
            if(cap!=null) {
                if(cap.getActiveEffectDuration()!=null && cap.getActiveEffectDuration().hasDurationForPower("laser")){
                    Vec3 impact = LaserPower.raycastForEntity(event.player.level(), event.player, 12, true,0).getLocation().subtract(0, .25, 0);
                    for (int i = 0; i < 8; i++) {
                        Vec3 motion = new Vec3(
                                getRandomScaled(.2f),
                                getRandomScaled(.2f),
                                getRandomScaled(.2f)
                        );
                        event.player.level().addParticle(ParticleTypes.SMOKE, impact.x + motion.x, impact.y + motion.y, impact.z + motion.z, 0.0F, motion.y, 0.0f);
                    }
                }
            }
        }
    }
    public static double getRandomScaled(double scale) {
        return (2.0D * Math.random() - 1.0D) * scale;
    }

}
