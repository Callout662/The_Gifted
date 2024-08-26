package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketHandlerPower;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FireBoltPower extends Power{
    public FireBoltPower() {
        super("fire_bolt", 20, 400, 20, ElementPower.FIRE, true, false, false);
    }

    @Override
    public int getCooldownForLevel() {
        return this.cooldown;
    }

    @Override
    public void startPower(Player player) {
        if(!player.level().isClientSide){
            player.sendSystemMessage(Component.nullToEmpty("Si quiso lanzar las flechas"));
            player.level().playSound(player,player, SoundEvents.CHICKEN_DEATH, SoundSource.PLAYERS,2.0f,1.0f);
            List<LivingEntity> livings=player.level().getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(10.0d),e->e!=player);
            for (LivingEntity living: livings){
                Arrow arrow=new Arrow(player.level(),player);
                Vec3 vec3=living.getEyePosition().add(0,2.0F,0).subtract(player.getEyePosition());
                arrow.setPos(player.getEyePosition());
                arrow.shoot(vec3.x,vec3.y,vec3.z,1.0f,0.0f);
                player.level().addFreshEntity(arrow);
            }
        }
    }

    @Override
    public Power copy() {
        Power power=new FireBoltPower();
        power.read(this.tag);
        return power;
    }
}
