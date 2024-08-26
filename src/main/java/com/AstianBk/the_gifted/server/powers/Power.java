package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.common.register.PWPower;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;


public class Power {
    public static Power NONE = new Power("",0, 0, 0, null, false, false, false);
    private final String descriptionId;
    public int cooldown;
    public int duration;
    public int cooldownTimer=0;
    public int lauchTime;
    public int castingDuration;
    public ElementPower elementPower;
    public boolean instantUse;
    public boolean isTransform;
    public boolean continuousUse;
    public int level;
    public String name;
    public CompoundTag tag;
    public Map<Attribute, AttributeModifier> attributeModifierMap= Maps.newHashMap();
    public Power(String name,int castingDuration,int cooldown,int lauchTime,@Nullable ElementPower elementPower,
                 boolean instantUse,boolean isTransform,boolean continuousUse){
        this.castingDuration=castingDuration;
        this.cooldown=cooldown;
        this.lauchTime =lauchTime;
        this.elementPower=elementPower;
        this.instantUse=instantUse;
        this.isTransform=isTransform;
        this.continuousUse=continuousUse;
        this.level=1;
        this.name=name;
        this.descriptionId= Component.translatable("power."+name).getString();
    }
    public Power(CompoundTag tag){
        Power power= PWPower.getPowerForName(tag.getString("name"));
        this.name= power.name;
        this.cooldown= power.cooldown;
        this.castingDuration= power.castingDuration;
        this.lauchTime= power.lauchTime;
        this.elementPower=power.elementPower;
        this.instantUse= power.instantUse;
        this.isTransform= power.isTransform;
        this.continuousUse= power.continuousUse;
        this.descriptionId=power.descriptionId;
        this.read(tag);
    }

    public void tick(PowerPlayerCapability player,int pos){
        if(this.isContinuousUse()){
            this.effectPowerForTick(player.getPlayer());
            if(this.duration>0){
                this.duration--;
                if(this.duration==0){
                    this.stopPower(player,pos);
                }
            }
        }

    }
    public void tickCooldown(PowerPlayerCapability player, int pos){
        if(this.cooldownTimer>0){
            this.cooldownTimer--;
        }
    }

    public ElementPower getElementPower(){
        return this.elementPower;
    }

    public void effectPowerForTick(Player player) {
        this.updateAttributes(player);
    }

    public void effectPassiveForTick() {

    }

    public void updateAttributes(Player player){

    }
    public boolean isContinuousUse() {
        return this.continuousUse;
    }

    public boolean isInstantUse() {
        return this.instantUse;
    }

    public boolean isTransform() {
        return this.isTransform;
    }

    public  int getCooldownForLevel(){
        return 0;
    }

    public void startPower(Player player) {

    }
    public void stopPower(PowerPlayerCapability player, int pos){
        System.out.print("\n-Entro-\n");
        this.setCooldownTimer(300);
        player.activesPowers.powers.remove(pos,this);
        player.setLastUsingPower(Power.NONE);
    }

    public void setCooldownTimer(int cooldownTimer) {
        this.cooldownTimer = cooldownTimer;
    }
    public CompoundTag save(CompoundTag tag){
        if(this.tag==null){
            this.tag=tag;
        }
        tag.putInt("cooldownTimer",this.cooldownTimer);
        return tag;
    }

    public void read(CompoundTag tag){
        if(tag==null){
            tag=new CompoundTag();
        }
        this.setCooldownTimer(tag.getInt("cooldownTimer"));
        this.tag=tag;
    }
    public Power copy(){
        return Power.NONE;
    }

    public boolean useResources() {
        return false;
    }

    public Attributes getResources() {
        return null;
    }

    public void addAttributeModifiers(LivingEntity p_19478_, AttributeMap p_19479_, int p_19480_) {
        for(Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance attributeinstance = p_19479_.getInstance(entry.getKey());
            if (attributeinstance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                attributeinstance.removeModifier(attributemodifier);
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), this.descriptionId + " " + p_19480_, this.getAttributeModifierValue(p_19480_, attributemodifier), attributemodifier.getOperation()));
            }
        }

    }

    public Power addAttributeModifier(Attribute p_19473_, String p_19474_, double p_19475_, AttributeModifier.Operation p_19476_) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(p_19474_),this.name, p_19475_, p_19476_);
        this.attributeModifierMap.put(p_19473_, attributemodifier);
        return this;
    }

    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return p_19458_.getAmount() * (double)(p_19457_ + 1);
    }

    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.attributeModifierMap;
    }

    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        for(Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance attributeinstance = p_19470_.getInstance(entry.getKey());
            if (attributeinstance != null) {
                attributeinstance.removeModifier(entry.getValue());
            }
        }

    }

}
