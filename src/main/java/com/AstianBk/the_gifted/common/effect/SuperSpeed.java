package com.AstianBk.the_gifted.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;


public class SuperSpeed extends MobEffect {
    public SuperSpeed(){
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "mob_effect.the_gifted.super_speed";
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }



    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {

    }
}