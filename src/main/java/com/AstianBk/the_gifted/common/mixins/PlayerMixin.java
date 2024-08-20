package com.AstianBk.the_gifted.common.mixins;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketHandlerPower;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class PlayerMixin {
    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;)V",at = @At("TAIL"))
    public void onSwing(InteractionHand p_21007_, CallbackInfo ci){
        if (((Object)this) instanceof Player player) {
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            if(cap!=null){
                cap.swingHand(player);
            }
        }
    }
}
