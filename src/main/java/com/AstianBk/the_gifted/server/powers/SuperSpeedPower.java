package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SuperSpeedPower extends Power{
    public int chargedTime=0;
    public SuperSpeedPower() {
        super("super_speed", 10, 300, 1, ElementPower.NORMAL, true, false, true,false);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,"91AEAA56-376B-4498-935B-2F7F68070635", (double)0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void startPower(Player player) {

    }

    @Override
    public void tick(PowerPlayerCapability player) {
        super.tick(player);
        Player player1=player.getPlayer();
        if(player1.isSprinting()){
            this.chargedTime=Math.min(this.chargedTime+2,100);
        }else {
            this.chargedTime=Math.max(this.chargedTime-1,0);
        }
    }

    @Override
    public void effectPowerForTick(Player player) {
        super.effectPowerForTick(player);
        if(this.level>=2){
            if(this.level>=3){
                player.level().getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(1.2D), e->e!=player).forEach(e->{
                    e.hurt(player.damageSources().playerAttack(player),5.0F);
                    if(this.level>=4 && this.chargedTime>=100){
                        if(player.level() instanceof ServerLevel level){
                            level.explode(player,e.getX(),e.getY(),e.getZ(),1.0F,false, Level.ExplosionInteraction.MOB);
                        }
                        this.chargedTime=0;
                    }
                });

            }

        }


    }


    public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
        return p_19458_.getAmount() * (double)(p_19457_ + 1);
    }

    @Override
    public void updateAttributes(Player player) {
        this.addAttributeModifiers(player,player.getAttributes(),4);
    }

    @Override
    public void stopPower(PowerPlayerCapability player) {
        super.stopPower(player);
        this.removeAttributeModifiers(player.getPlayer(),player.getPlayer().getAttributes(),4);
    }
}
