package com.AstianBk.the_gifted.common.mixins;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker("getWaterSlowDown")
    float getWater$SlowDown();
    @Invoker("getFallDamageSound")
    SoundEvent getFall$Sound(int sound);
}
