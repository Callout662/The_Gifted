package com.AstianBk.the_gifted.server.manager;

import com.AstianBk.the_gifted.common.api.Limbs;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketSyncLimbRegeneration;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.nio.file.LinkOption;
import java.util.Map;

public class LimbsPartRegeneration {
    public ServerPlayer serverPlayer;
    public Map<String,RegenerationInstance> loseLimbs;
    public LimbsPartRegeneration(ServerPlayer player){
        this.loseLimbs= Maps.newHashMap();
        this.serverPlayer=player;
    }
    @OnlyIn(Dist.CLIENT)
    public LimbsPartRegeneration(Map<String,RegenerationInstance> loseLimbs){
        this.loseLimbs= loseLimbs;
        this.serverPlayer=null;
    }

    public boolean loseLimb(String id){
        return this.loseLimbs.containsKey(id);
    }

    public boolean loseLimb(Limbs limbs){
        return this.loseLimbs.containsKey(limbs.name());
    }
    public void addLoseLimbDefault(String id){
        this.loseLimbs.put(id,new RegenerationInstance(300));
    }
    public void addLoseLimb(String id,int timer,int remaining){
        this.loseLimbs.put(id,new RegenerationInstance(timer,remaining));
    }
    public void regenerateLimb(String id){
        this.loseLimbs.remove(id);
    }
    public void clearLimbs(){
        this.loseLimbs.clear();
    }
    public void syncPlayer(){
        PacketHandler.sendToPlayer(new PacketSyncLimbRegeneration(this.loseLimbs),this.serverPlayer);
    }
    public void tick(){
        this.loseLimbs.forEach((s, regenerationInstance) ->{
            regenerationInstance.decrement();
            if(regenerationInstance.getRegerationTimerRemaining()<=0){
                this.loseLimbs.remove(s);
            }
        });
    }
    public ListTag saveNBTData() {
        var listTag = new ListTag();
        loseLimbs.forEach((powerId, cooldown) -> {
            if (cooldown.getRegerationTimerRemaining() > 0) {
                CompoundTag ct = new CompoundTag();
                ct.putString("name", powerId);
                ct.putInt("timer", cooldown.getRegerationTimer());
                ct.putInt("remaining", cooldown.getRegerationTimerRemaining());
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
