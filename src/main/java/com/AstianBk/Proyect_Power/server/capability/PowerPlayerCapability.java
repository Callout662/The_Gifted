package com.AstianBk.Proyect_Power.server.capability;

import com.AstianBk.Proyect_Power.common.api.IPowerPlayer;
import com.AstianBk.Proyect_Power.server.powers.Power;
import com.AstianBk.Proyect_Power.server.powers.Powers;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PowerPlayerCapability implements IPowerPlayer {
    public Power usingPower;
    Player player;
    Powers powers=new Powers(Maps.newHashMap());
    Map<Integer,Power> passives= Maps.newHashMap();
    int posSelectPower=1;
    int castingTimer=0;
    @Override
    public Player getPlayer() {
        return this.player;
    }
    public static PowerPlayerCapability get(Player player){
        return PwCapability.getEntityPatch(player,PowerPlayerCapability.class);
    }
    @Override
    public void setPlayer(Player player) {
        this.player=player;
    }

    @Override
    public Power getSelectPower() {
        Power power = this.powers.get(this.posSelectPower);
        return power;
    }

    @Override
    public int getCooldownPower() {
        return this.getSelectPower().cooldown;
    }


    @Override
    public int getCastingTimer() {
        return this.castingTimer;
    }

    @Override
    public int getStartTime() {
        return this.getUsingPower().lauchTime;
    }

    @Override
    public boolean usingPower() {
        return this.getUsingPower()!=Power.NONE;
    }

    @Override
    public Power getUsingPower() {
        return this.usingPower;
    }

    @Override
    public void setUsingPower(Power power) {
        this.usingPower=power;
    }

    @Override
    public void tick(Player player) {
        if (!this.powers.powers.isEmpty()){
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            if(cap!=null){
                for (Power power:this.powers.getPowers()){
                    power.tick(cap);
                }
            }

        }
        if(this.getUsingPower()!=Power.NONE){
            if(this.getUsingPower().instantUse && this.castingTimer==this.getStartTime()){
                this.handledPower(player,this.getUsingPower());
            }
        }

    }
    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {

    }

    @Override
    public void handledPower(Player player,Power power) {
        power.startPower(player);
    }

    @Override
    public void handledPassive(Player player,Power power) {

    }


    @Override
    public boolean canUsePower() {
        return false;
    }

    @Override
    public boolean haveAlterEgo() {
        return false;
    }

    @Override
    public Map<Integer, Power> getPassives() {
        return this.passives;
    }

    @Override
    public Powers getHotBarPower() {
        return this.powers;
    }

    @Override
    public void syncPower(Player player) {

    }

    @Override
    public void upPower() {
        this.posSelectPower=Math.min(this.posSelectPower+1,5);
        if (this.getPlayer()!=null){
            this.getPlayer().sendSystemMessage(Component.nullToEmpty("Se cambio al"+this.getSelectPower().name));
        }
    }

    @Override
    public void downPower() {
        this.posSelectPower=Math.max(this.posSelectPower-1,1);
        if (this.getPlayer()!=null){
            this.getPlayer().sendSystemMessage(Component.nullToEmpty("Se cambio al"+this.getSelectPower().name));
        }
    }

    @Override
    public void swingHand(InteractionHand hand,Player player) {

    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        this.powers.save(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.powers=new Powers(nbt);
    }

    public void init(Player player) {
        this.setPlayer(player);
    }

    public static class PowerPlayerProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

        private final LazyOptional<IPowerPlayer> instance = LazyOptional.of(PowerPlayerCapability::new);

        @NonNull
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return PwCapability.POWER_CAPABILITY.orEmpty(cap,instance.cast());
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
