package com.AstianBk.the_gifted.server.capability;

import com.AstianBk.the_gifted.common.api.IAnimationPlayer;
import com.AstianBk.the_gifted.common.api.IPowerPlayer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AnimationPlayerCapability implements IAnimationPlayer, GeoEntity {
    AnimatableInstanceCache cache= GeckoLibUtil.createInstanceCache(this);
    Player player;
    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }

    @Override
    public void init(Player player) {
        this.setPlayer(player);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 20, state -> {
            Player player1=this.getPlayer();
            //AnimationPlayerCapability replacedExecutioner = getPatch(player1,AnimationPlayerCapability.class);
            if (player1 == null) return PlayState.STOP;
            boolean isMove= !(state.getLimbSwingAmount() > -0.15F && state.getLimbSwingAmount() < 0.15F);
            if(isMove && player1.isSprinting()) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("flight (arms back)"));
            }else {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("slow flight"));
            }
            return PlayState.CONTINUE;
        }));
    }
    public <P extends AnimationPlayerCapability> P getPatch(LivingEntity replaced, Class<P> pClass){
        return PwCapability.getEntityPatch(replaced,pClass);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static class AnimationPlayerProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

        private final LazyOptional<IAnimationPlayer> instance = LazyOptional.of(AnimationPlayerCapability::new);

        @NonNull
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return PwCapability.ANIMATION_CAPABILITY.orEmpty(cap,instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
