package com.AstianBk.the_gifted.server;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.common.api.Limbs;
import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PwCapability;
import com.AstianBk.the_gifted.server.manager.RegenerationInstance;
import com.AstianBk.the_gifted.server.powers.Power;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
    public static void onTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity() instanceof Player){
            PowerPlayerCapability cap = PwCapability.getEntityCap(event.getEntity(), PowerPlayerCapability.class);
            if(cap!=null && event.getEntity().isAlive()){
                cap.tick((Player) event.getEntity());
                if(cap.cantMove()){
                    event.getEntity().setDeltaMovement(0,0,0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event){
        Player player=event.getOriginal();
        Player newPlayer=event.getEntity();
        player.reviveCaps();
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        PowerPlayerCapability newCap=PowerPlayerCapability.get(newPlayer);
        newCap.clone(cap,player,newPlayer);
        player.invalidateCaps();
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
    @SubscribeEvent
    public static void hurtEntity(LivingHurtEvent event){
        LivingEntity target=event.getEntity();
        DamageSource source=event.getSource();
        if(target instanceof Player player){
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            if(cap!=null){
                Power reg=cap.getHotBarPower().getForName("super_regeneration");
                if(reg!=null && !cap.getCooldowns().isOnCooldown(reg)){
                    if(source.is(DamageTypeTags.IS_FALL) && source.getEntity()==null){
                        if(!cap.legsLess()){
                            loseLegs(cap,player);
                        }else if(!cap.bodyLess()){
                            loseBody(cap,player);
                        }else {
                            cap.losePart("head",new RegenerationInstance(50),player);
                        }
                    }else if(source.is(DamageTypeTags.IS_EXPLOSION)){
                        cap.losePart("head",new RegenerationInstance(50),player);
                        loseBody(cap,player);
                        cap.getCooldowns().addCooldown(reg,200);
                        event.setCanceled(true);
                        player.setHealth(1.0F);
                    }else if(source.getEntity()!=null && source.getEntity() instanceof LivingEntity){
                        LivingEntity living = (LivingEntity) source.getEntity();
                        Limbs[] limbs=Limbs.values();
                        double amount=event.getAmount();
                        boolean hasSword=living.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof TieredItem;
                        if((hasSword || player.getRandom().nextFloat()<(0.3F+(0.4F*(Math.min(6.0F/amount,1.0F))))) && !cap.noMoreLimbs()){
                            if (amount>1.0F && amount<3.0F){
                                int i=player.getRandom().nextInt(0,5);
                                String name= limbs[i].name().toLowerCase();
                                while (name.equals("body") && (!cap.legsLess() || !cap.armsLess() || !cap.headLess())){
                                    i= Mth.clamp(player.getRandom().nextInt(-3,3)+i,0,5);
                                    name=limbs[i].name();
                                }
                                cap.losePart(name,new RegenerationInstance(50),player);
                            }else if(amount>3.0F && amount<8.0F){
                                for (int j=0;j<2;j++){
                                    if(!cap.noMoreLimbs()){
                                        int i=player.getRandom().nextInt(0,5);
                                        String name= limbs[i].name().toLowerCase();
                                        while (name.equals("body") && (!cap.legsLess() || !cap.armsLess() || !cap.headLess())){
                                            i= Mth.clamp(player.getRandom().nextInt(-3,3)+i,0,5);
                                            name=limbs[i].name();
                                        }
                                        cap.losePart(name,new RegenerationInstance(50),player);
                                    }else {
                                        break;
                                    }
                                }
                            }else if(amount>8.0F && amount<16.0F){
                                for (int j=0;j<3;j++){
                                    if(!cap.noMoreLimbs()){
                                        int i=player.getRandom().nextInt(0,5);
                                        String name= limbs[i].name().toLowerCase();
                                        while (name.equals("body") && (!cap.legsLess() || !cap.armsLess() || !cap.headLess())){
                                            i= Mth.clamp(player.getRandom().nextInt(-3,3)+i,0,5);
                                            name=limbs[i].name();
                                        }
                                        cap.losePart(name,new RegenerationInstance(50),player);
                                    }else {
                                        break;
                                    }
                                }
                            }else if(amount<16.0F){
                                cap.losePart("head",new RegenerationInstance(50),player);
                                loseBody(cap,player);
                                cap.getCooldowns().addCooldown(reg,200);
                                event.setCanceled(true);
                                player.setHealth(1.0F);
                            }
                        }
                    }
                }
            }
        }
    }


    public static void loseLegs(PowerPlayerCapability cap,Player player){
        cap.losePart("right_leg",new RegenerationInstance(50),player);
        cap.losePart("left_leg",new RegenerationInstance(50),player);
    }
    public static void loseBody(PowerPlayerCapability cap,Player player){
        cap.losePart("body",new RegenerationInstance(100),player);
        cap.losePart("right_arm",new RegenerationInstance(50),player);
        cap.losePart("left_arm",new RegenerationInstance(50),player);
        loseLegs(cap,player);
    }
}
