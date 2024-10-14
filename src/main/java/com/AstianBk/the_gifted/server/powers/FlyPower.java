package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FlyPower extends Power{
    public FlyPower() {
        super("fly", 300, 100, 1, ElementPower.NORMAL, true, true, false,false);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,"91AEAA56-376B-4498-935B-2F7F68070635", (double)0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void startPower(Player player) {
        Vec3 push=player.position().multiply(0,1,0).add(0,10,0).normalize();
        player.push(push.x,5.0F,push.z);
    }

    @Override
    public void tick(PowerPlayerCapability player) {
        super.tick(player);
        player.getPlayer().getAbilities().flying=true;
        player.getPlayer().onUpdateAbilities();

    }

    @Override
    public void effectPowerForTick(Player player) {
        super.effectPowerForTick(player);
    }


    @Override
    public void updateAttributes(Player player) {
        this.addAttributeModifiers(player,player.getAttributes(),4);
    }

    @Override
    public void stopPower(PowerPlayerCapability player) {
        super.stopPower(player);
        if(!player.getPlayer().isCreative()){
            player.getPlayer().getAbilities().flying=false;
            player.getPlayer().onUpdateAbilities();
        }
        this.removeAttributeModifiers(player.getPlayer(),player.getPlayer().getAttributes(),4);
    }
}
