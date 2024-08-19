package com.AstianBk.Proyect_Power.server.powers;

import net.minecraft.world.entity.player.Player;

public abstract class Power {
    public int cooldown;
    public int lauchTime;
    public int castingDuration;
    public ElementPower elementPower;
    public boolean instantUse;
    public boolean isTransform;
    public boolean continuousUse;
    public int level;
    public Power(int castingDuration,int cooldown,int lauchTime,ElementPower elementPower,
                 boolean instantUse,boolean isTransform,boolean continuousUse){
        this.castingDuration=castingDuration;
        this.cooldown=cooldown;
        this.lauchTime =lauchTime;
        this.elementPower=elementPower;
        this.instantUse=instantUse;
        this.isTransform=isTransform;
        this.continuousUse=continuousUse;
        this.level=1;
    }

    void tick(){
        if(this.cooldown>0){
            this.cooldown--;
        }
    }

    public ElementPower getElementPower(){
        return this.elementPower;
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

}
