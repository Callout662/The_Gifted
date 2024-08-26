package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.common.register.PWEffects;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FlyPower extends Power{
    public FlyPower() {
        super("fly", 10, 300, 1, ElementPower.NORMAL, true, false, true);
    }

    @Override
    public void startPower(Player player) {
        Vec3 push=player.position().multiply(0,1,0).add(0,10,0).normalize();
        player.push(push.x,5.0F,push.z);
    }

    @Override
    public void tick(PowerPlayerCapability player, int pos) {
        super.tick(player, pos);
        player.getPlayer().getAbilities().flying=true;
        player.getPlayer().onUpdateAbilities();

    }

    @Override
    public void effectPowerForTick(Player player) {
        super.effectPowerForTick(player);
    }

    @Override
    public Power copy() {
        Power power=new FlyPower();
        power.read(this.tag);
        return power;
    }

    @Override
    public void updateAttributes(Player player) {
        //this.addAttributeModifiers(player,player.getAttributes(),4);
    }

    @Override
    public void stopPower(PowerPlayerCapability player, int pos) {
        super.stopPower(player,pos);
        if(!player.getPlayer().isCreative()){
            player.getPlayer().getAbilities().flying=false;
            player.getPlayer().onUpdateAbilities();
        }
        //this.removeAttributeModifiers(player.getPlayer(),player.getPlayer().getAttributes(),4);
    }
}
