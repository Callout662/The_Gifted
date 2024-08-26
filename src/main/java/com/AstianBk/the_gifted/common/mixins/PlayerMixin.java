package com.AstianBk.the_gifted.common.mixins;

import com.AstianBk.the_gifted.common.register.PWEffects;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketHandlerPower;
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
public abstract class PlayerMixin {
    @Shadow public abstract boolean hasEffect(MobEffect p_21024_);

    @Shadow @Nullable public abstract MobEffectInstance getEffect(MobEffect p_21125_);

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;)V",at = @At("TAIL"))
    public void onSwing(InteractionHand p_21007_, CallbackInfo ci){
        if (((Object)this) instanceof Player player) {
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            if(cap!=null){
                cap.swingHand(player);
            }
        }
    }
    @Inject(method = "canStandOnFluid",cancellable = true,at = @At("RETURN"))
    public void canStandInFluid(FluidState p_204042_, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(this.hasEffect(PWEffects.SUPER_SPEED.get()) && this.getEffect(PWEffects.SUPER_SPEED.get()).getAmplifier()>1);
    }
}
