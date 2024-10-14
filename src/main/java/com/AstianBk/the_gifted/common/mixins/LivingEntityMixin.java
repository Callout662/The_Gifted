package com.AstianBk.the_gifted.common.mixins;

import com.AstianBk.the_gifted.common.register.PWEffects;
import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract boolean hasEffect(MobEffect p_21024_);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(MobEffect p_21125_);
    @Inject(method = "canStandOnFluid",cancellable = true,at = @At("RETURN"))
    public void canStandInFluid(FluidState p_204042_, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(this.hasEffect(PWEffects.SUPER_SPEED.get()) && this.getEffect(PWEffects.SUPER_SPEED.get()).getAmplifier()>1);
    }

    @Inject(method = "setSprinting",at = @At("TAIL"))
    public void set$Sprinting(boolean p_21284_, CallbackInfo ci){
        if (((Object)this) instanceof Player player) {
            AnimationPlayerCapability cap=AnimationPlayerCapability.get(player);
            if(cap!=null){
                int id=p_21284_ ? player.level().getRandom().nextInt(1,4) : 0;
                cap.handledPoseFly(id);
            }
        }
    }


}
