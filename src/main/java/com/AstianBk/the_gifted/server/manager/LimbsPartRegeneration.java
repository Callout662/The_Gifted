package com.AstianBk.the_gifted.server.manager;

import com.AstianBk.the_gifted.client.particle.PWParticles;
import com.AstianBk.the_gifted.common.api.Limbs;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketRemoveActiveEffect;
import com.AstianBk.the_gifted.server.network.message.PacketSyncLimbRegeneration;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

public class LimbsPartRegeneration {
    public ServerPlayer serverPlayer;
    public Map<String,RegenerationInstance> loseLimbs;
    public LimbsPartRegeneration(ServerPlayer player){
        this.loseLimbs= Maps.newHashMap();
        this.serverPlayer=player;
    }

    public LimbsPartRegeneration(){
        this.loseLimbs= Maps.newHashMap();
        this.serverPlayer=null;
    }
    @OnlyIn(Dist.CLIENT)
    public LimbsPartRegeneration(Map<String,RegenerationInstance> loseLimbs){
        this.loseLimbs= loseLimbs;
        this.serverPlayer=null;
    }

    public boolean loseLimb(String id){
        return this.loseLimbs.containsKey(id) && this.loseLimbs.get(id).getCooldownPercent()>0.5F;
    }

    @OnlyIn(Dist.CLIENT)
    public void addLoseLimb(String id,RegenerationInstance instance){
        this.loseLimbs.put(id,instance);
    }
    public void regenerateLimb(String id){
        this.loseLimbs.remove(id);
        if(this.serverPlayer!=null){
            PacketHandler.sendToPlayer(new PacketRemoveActiveEffect(id,1),this.serverPlayer);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public void regenerateLimbClient(String id){
        this.loseLimbs.remove(id);
    }
    public void clearLimbs(){
        this.loseLimbs.clear();
    }
    public void syncPlayer(){
        PacketHandler.sendToPlayer(new PacketSyncLimbRegeneration(this.loseLimbs),this.serverPlayer);
    }
    public List<Limbs> getLimbs(){
        List<Limbs> limbs=new ArrayList<>();
        this.loseLimbs.forEach((s, regenerationInstance) -> {
            if(regenerationInstance.getRegerationTimerRemaining()>0){
                Limbs limbs1=Limbs.valueOf(s.toUpperCase());
                limbs.add(limbs1);
            }
        });
        return limbs;
    }
    public List<Limbs> getLimbsMuscle(){
        List<Limbs> limbs=new ArrayList<>();
        this.loseLimbs.forEach((s, regenerationInstance) -> {
            if(regenerationInstance.getCooldownPercent()<0.5F){
                Limbs limbs1=Limbs.valueOf(s.toUpperCase());
                limbs.add(limbs1);
            }
        });
        return limbs;
    }
    public boolean decrementCooldown(RegenerationInstance c, int amount,String id,List<Limbs> list) {
        if(canRegenerateLimbs(id,list)){
            c.decrementBy(amount);
            return c.getRegerationTimerRemaining() <= 0;
        }else {
            c.resetTimer();
        }
        return false;
    }
    public boolean canRegenerateLimbs(String id,List<Limbs> limbs){
        Limbs limbs1=Limbs.valueOf(id.toUpperCase());
        if(limbs1==Limbs.HEAD){
            return true;
        }else if(limbs1==Limbs.BODY){
            return !limbs.contains(Limbs.HEAD);
        }else {
            return !limbs.contains(Limbs.BODY);
        }
    }
    public void tick(Player player){
        var powers = loseLimbs.entrySet().stream().filter(x -> decrementCooldown(x.getValue(), 1,x.getKey(),this.getLimbs())).toList();
        if(player.level().isClientSide){
            loseLimbs.forEach((x,y)->{
                if(player.getRandom().nextFloat()>0.2F){
                    Vec3 addPos=this.getPosOffSet(x,y,player);
                    player.level().addParticle(PWParticles.BLOOD_PARTICLES.get(), player.getX() + addPos.x, player.getY()+addPos.y, player.getZ() + addPos.z, 0.0F, -0.01F, 0.0F);
                }
            });
        }
        powers.forEach(stringRegenerationInstanceEntry -> regenerateLimb(stringRegenerationInstanceEntry.getKey()));

    }
    public Vec3 getPosOffSet(String name,RegenerationInstance instance,Player player){
        float f = player.yBodyRot * ((float) Math.PI / 180F) + Mth.cos((float) player.tickCount * 0.6662F) * 0.25F;
        float f1 = Mth.cos(f);
        float f2 = Mth.sin(f);
        float porcentBlood=instance.getCooldownPercent();

        switch (name){
            case "head"-> {
                return new Vec3(0.0F,1.7F+0.25F*porcentBlood,0.0F);
            }
            case "right_arm"->{
                return new Vec3(-f1*0.35F,1.30F-0.5F*porcentBlood,-f2*0.35F);
            }
            case "left_arm"->{
                return new Vec3(f1*0.35F,1.30F-0.5F*porcentBlood,f2*0.35F);
            }
            case "right_leg"->{
                return new Vec3(-f1*0.2F,1.0F-0.5F*porcentBlood,-f2*0.2F);
            }
            case "left_leg"->{
                return new Vec3(f1*0.2F,1.0F-0.5F*porcentBlood,f2*0.2F);
            }
            default -> {
                return new Vec3(0.0F,1.7f-0.5F*porcentBlood,0.0f);
            }
        }
    }
    public boolean hasRegenerationLimbs(){
        return !this.loseLimbs.isEmpty();
    }
    public ListTag saveNBTData() {
        var listTag = new ListTag();
        loseLimbs.forEach((powerId, timer) -> {
            if (timer.getRegerationTimerRemaining() > 0) {
                CompoundTag ct = new CompoundTag();
                ct.putString("name", powerId);
                ct.putInt("timer", timer.getRegerationTimer());
                ct.putInt("remaining", timer.getRegerationTimerRemaining());
                listTag.add(ct);
            }
        });
        return listTag;
    }

    public void loadNBTData(ListTag listTag) {
        if (listTag != null) {
            listTag.forEach(tag -> {
                CompoundTag t = (CompoundTag) tag;
                String powerId = t.getString("name");
                int powerCooldown = t.getInt("timer");
                int cooldownRemaining = t.getInt("remaining");
                loseLimbs.put(powerId, new RegenerationInstance(powerCooldown, cooldownRemaining));
            });
        }
    }
}
