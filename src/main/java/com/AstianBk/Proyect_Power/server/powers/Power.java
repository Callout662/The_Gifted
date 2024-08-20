package com.AstianBk.Proyect_Power.server.powers;

import com.AstianBk.Proyect_Power.common.api.IPowerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;


public abstract class Power {
    public static Power NONE = new Power("",0, 0, 0, null, false, false, false) {
        @Override
        public int getCooldownForLevel() {
            return 0;
        }

        @Override
        public void startPower(Player player) {

        }

        @Override
        public void stopPower(Player player) {

        }
    };
    private final String descriptionId;
    public int cooldown;
    public int lauchTime;
    public int castingDuration;
    public ElementPower elementPower;
    public boolean instantUse;
    public boolean isTransform;
    public boolean continuousUse;
    public int level;
    public String name;
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

    public void tick(IPowerPlayer player){
        if(this.isContinuousUse()){
            this.effectPowerForTick(player.getPlayer());
        }
        if(this.cooldown>0){
            this.cooldown--;
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

    public abstract int getCooldownForLevel();

    public abstract void startPower(Player player);
    public abstract void stopPower(Player player);


    public boolean useResources() {
        return false;
    }

    public Attributes getResources() {
        return null;
    }

}
