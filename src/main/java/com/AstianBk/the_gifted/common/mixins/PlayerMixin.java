package com.AstianBk.the_gifted.common.mixins;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow public abstract boolean canBeSeenAsEnemy();

    private static final UUID SLOW_FALLING_ID = UUID.fromString("A5B6CF2A-2F7C-31EF-9022-7C3E7D5E6ABA");
    private static final AttributeModifier SLOW_FALLING = new AttributeModifier(SLOW_FALLING_ID, "Slow falling acceleration reduction", -0.07, AttributeModifier.Operation.ADDITION); // Add -0.07 to 0.08 so we get the vanilla default of 0.01

    @Inject(method = "travel",cancellable = true,at = @At("HEAD"))
    public void travelInject(Vec3 p_21280_, CallbackInfo ci){
        if(((Object)this) instanceof Player player){
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            if(cap!=null && cap.durationEffect.hasDurationForPower("fly")){
                ci.cancel();
                float f = player.xxa * 0.5F;
                float f1 = player.zza;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }
                player.setSpeed(player.isSprinting() ? (float) player.getAttribute(Attributes.MOVEMENT_SPEED).getValue()*4 : (float) player.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                p_21280_= new Vec3(0.0D, 0.0D, 1.0D);
                double d3 = player.getLookAngle().y;
                double d4 = d3 < -0.2D ? 0.085D : 0.06D;
                if(f==0.0F || f1==0.0F){
                    d4-=d4;
                }
                double d5 = player.isSprinting() ? (d3 - player.getDeltaMovement().y)* d4+(f+f1)*d3: player.getDeltaMovement().y* 0.6D;
                this.travelFly(p_21280_,player);
                Vec3 vec31 = player.getDeltaMovement();
                player.setDeltaMovement(vec31.x, d5 , vec31.z);
                player.resetFallDistance();
                ((EntityAccessor)player).set(7, false);
                ((EntityAccessor)player).tryCheck();
            }
        }
    }

    public void travelFly(Vec3 p_21280_,Player player) {
        if (player.isControlledByLocalInstance() ) {
            double d0 = 0.08D;
            AttributeInstance gravity = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
            boolean flag = player.getDeltaMovement().y <= 0.0D;
            if (flag && player.hasEffect(MobEffects.SLOW_FALLING)) {
                if (!gravity.hasModifier(SLOW_FALLING)) gravity.addTransientModifier(SLOW_FALLING);
            } else if (gravity.hasModifier(SLOW_FALLING)) {
                gravity.removeModifier(SLOW_FALLING);
            }
            d0 = gravity.getValue();

            FluidState fluidstate = player.level().getFluidState(player.blockPosition());
            if ((player.isInWater() || (player.isInFluidType(fluidstate) && fluidstate.getFluidType() != net.minecraftforge.common.ForgeMod.LAVA_TYPE.get())) && player.isAffectedByFluids() && !player.canStandOnFluid(fluidstate)) {
                if (player.isInWater() || (player.isInFluidType(fluidstate) && !player.moveInFluid(fluidstate, p_21280_, d0))) {
                    double d9 = player.getY();
                    float f4 = player.isSprinting() ? 0.9F : ((LivingEntityAccessor)player).getWater$SlowDown();
                    float f5 = 0.02F;
                    float f6 = (float) EnchantmentHelper.getDepthStrider(player);
                    if (f6 > 3.0F) {
                        f6 = 3.0F;
                    }

                    if (!player.onGround()) {
                        f6 *= 0.5F;
                    }

                    if (f6 > 0.0F) {
                        f4 += (0.54600006F - f4) * f6 / 3.0F;
                        f5 += (player.getSpeed() - f5) * f6 / 3.0F;
                    }

                    if (player.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                        f4 = 0.96F;
                    }

                    f5 *= (float)player.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
                    player.moveRelative(f5, p_21280_);
                    player.move(MoverType.SELF, player.getDeltaMovement());
                    Vec3 vec36 = player.getDeltaMovement();
                    if (player.horizontalCollision && player.onClimbable()) {
                        vec36 = new Vec3(vec36.x, 0.2D, vec36.z);
                    }

                    player.setDeltaMovement(vec36.multiply((double)f4, (double)0.8F, (double)f4));
                    Vec3 vec32 = player.getFluidFallingAdjustedMovement(d0, flag, player.getDeltaMovement());
                    player.setDeltaMovement(vec32);
                    if (player.horizontalCollision && player.isFree(vec32.x, vec32.y + (double)0.6F - player.getY() + d9, vec32.z)) {
                        player.setDeltaMovement(vec32.x, (double)0.3F, vec32.z);
                    }
                }
            } else if (player.isInLava() && player.isAffectedByFluids() && !player.canStandOnFluid(fluidstate)) {
                double d8 = player.getY();
                player.moveRelative(0.02F, p_21280_);
                player.move(MoverType.SELF, player.getDeltaMovement());
                if (player.getFluidHeight(FluidTags.LAVA) <= player.getFluidJumpThreshold()) {
                    player.setDeltaMovement(player.getDeltaMovement().multiply(0.5D, (double)0.8F, 0.5D));
                    Vec3 vec33 = player.getFluidFallingAdjustedMovement(d0, flag, player.getDeltaMovement());
                    player.setDeltaMovement(vec33);
                } else {
                    player.setDeltaMovement(player.getDeltaMovement().scale(0.5D));
                }

                if (!player.isNoGravity()) {
                    player.setDeltaMovement(player.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
                }

                Vec3 vec34 = player.getDeltaMovement();
                if (player.horizontalCollision && player.isFree(vec34.x, vec34.y + (double)0.6F - player.getY() + d8, vec34.z)) {
                    player.setDeltaMovement(vec34.x, (double)0.3F, vec34.z);
                }
            } else if (player.isFallFlying()) {
                player.checkSlowFallDistance();
                Vec3 vec3 = player.getDeltaMovement();
                Vec3 vec31 = player.getLookAngle();
                float f = player.getXRot() * ((float)Math.PI / 180F);
                double d1 = Math.sqrt(vec31.x * vec31.x + vec31.z * vec31.z);
                double d3 = vec3.horizontalDistance();
                double d4 = vec31.length();
                double d5 = Math.cos((double)f);
                d5 = d5 * d5 * Math.min(1.0D, d4 / 0.4D);
                vec3 = player.getDeltaMovement().add(0.0D, d0 * (-1.0D + d5 * 0.75D), 0.0D);
                if (vec3.y < 0.0D && d1 > 0.0D) {
                    double d6 = vec3.y * -0.1D * d5;
                    vec3 = vec3.add(vec31.x * d6 / d1, d6, vec31.z * d6 / d1);
                }

                if (f < 0.0F && d1 > 0.0D) {
                    double d10 = d3 * (double)(-Mth.sin(f)) * 0.04D;
                    vec3 = vec3.add(-vec31.x * d10 / d1, d10 * 3.2D, -vec31.z * d10 / d1);
                }

                if (d1 > 0.0D) {
                    vec3 = vec3.add((vec31.x / d1 * d3 - vec3.x) * 0.1D, 0.0D, (vec31.z / d1 * d3 - vec3.z) * 0.1D);
                }

                player.setDeltaMovement(vec3.multiply((double)0.99F, (double)0.98F, (double)0.99F));
                player.move(MoverType.SELF, player.getDeltaMovement());
                if (player.horizontalCollision && !player.level().isClientSide) {
                    double d11 = player.getDeltaMovement().horizontalDistance();
                    double d7 = d3 - d11;
                    float f1 = (float)(d7 * 10.0D - 3.0D);
                    if (f1 > 0.0F) {
                        player.playSound(((LivingEntityAccessor)player).getFall$Sound((int)f1), 1.0F, 1.0F);
                        player.hurt(player.damageSources().flyIntoWall(), f1);
                    }
                }

                if (player.onGround() && !player.level().isClientSide) {
                    ((EntityAccessor)player).set(7, false);
                }
            } else {
                BlockPos blockpos = ((EntityAccessor)player).getBlockPos();
                float f2 = player.level().getBlockState(((EntityAccessor)player).getBlockPos()).getFriction(player.level(),  ((EntityAccessor)player).getBlockPos(), player);
                float f3 = player.onGround() ? f2 * 0.91F : 0.91F;
                Vec3 vec35 = player.handleRelativeFrictionAndCalculateMovement(p_21280_, f2);
                double d2 = vec35.y;
                if (player.hasEffect(MobEffects.LEVITATION)) {
                    d2 += (0.05D * (double)(player.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2D;
                } else if (player.level().isClientSide && !player.level().hasChunkAt(blockpos)) {
                    if (player.getY() > (double)player.level().getMinBuildHeight()) {
                        d2 = -0.1D;
                    } else {
                        d2 = 0.0D;
                    }
                } else if (!player.isNoGravity()) {
                    d2 -= d0;
                }

                if (player.shouldDiscardFriction()) {
                    player.setDeltaMovement(vec35.x, d2, vec35.z);
                } else {
                    player.setDeltaMovement(vec35.x * (double)f3, d2 * (double)0.98F, vec35.z * (double)f3);
                }
            }
        }

        player.calculateEntityAnimation(player instanceof FlyingAnimal);
    }
}
