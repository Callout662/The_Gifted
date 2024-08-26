package com.AstianBk.the_gifted.server.capability;

import com.AstianBk.the_gifted.common.api.IPowerPlayer;
import com.AstianBk.the_gifted.common.register.PWPower;
import com.AstianBk.the_gifted.server.powers.Power;
import com.AstianBk.the_gifted.server.powers.Powers;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    public Power lastUsingPower =Power.NONE;
    Player player;
    Level level;
    public Powers powers=new Powers(Maps.newHashMap());
    public Powers activesPowers=new Powers(Maps.newHashMap());
    Map<Integer,Power> passives= Maps.newHashMap();
    int lastPosSelectPower=1;
    int posSelectPower=1;
    int castingTimer=0;
    @Override
    public Player getPlayer() {
        return this.player;
    }
    public static PowerPlayerCapability get(Player player){
        return PwCapability.getEntityCap(player,PowerPlayerCapability.class);
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
    public Power getPowerForHotBar(int pos) {
        return this.powers.get(pos);
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
        return this.getLastUsingPower().lauchTime;
    }

    @Override
    public boolean lastUsingPower() {
        return this.getLastUsingPower()!=Power.NONE;
    }

    @Override
    public Power getLastUsingPower() {
        return this.lastUsingPower;
    }

    @Override
    public void setLastUsingPower(Power power) {
        this.lastUsingPower =power;
    }

    @Override
    public void tick(Player player) {
        if (!this.powers.powers.isEmpty()){
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            if(cap!=null){
                int i=0;
                for (Power power:this.powers.getPowers()){
                    power.tickCooldown(cap,i);
                    i++;
                }
            }
        }
        if(!this.activesPowers.powers.isEmpty()){
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            if(cap!=null){
                int i=0;
                for (Power power:this.activesPowers.getPowers()){
                    power.tick(cap,i);
                    i++;
                }
            }
        }
        if(this.getLastUsingPower()!=null && this.lastUsingPower()){
            if(this.castingTimer==this.getLastUsingPower().lauchTime){
                this.handledPower(player,this.getLastUsingPower());
            }
            this.castingTimer--;
            if(this.castingTimer==0){
                this.stopPower(player,this.getLastUsingPower());
            }
        }
    }
    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        if(cap!=null){
            this.powers.addPowers(1, PWPower.FIRE_BOLT.copy());
            this.powers.addPowers(2, PWPower.SUPER_SPEED.copy());
            this.powers.addPowers(3, PWPower.FLY.copy());
        }
    }

    @Override
    public void handledPower(Player player,Power power) {
        power.startPower(player);
    }
    @Override
    public void stopPower(Player player,Power power){
        power.setCooldownTimer(300);
        power.stopPower(PowerPlayerCapability.get(player),this.posSelectPower);
    }

    @Override
    public void handledPassive(Player player,Power power) {

    }


    @Override
    public boolean canUsePower() {
        return this.getSelectPower().cooldownTimer<=0 && this.castingTimer<=0;
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
            this.getPlayer().sendSystemMessage(Component.nullToEmpty(this.posSelectPower+" Se cambio al"+this.getSelectPower().name));
        }
    }

    @Override
    public void downPower() {
        this.posSelectPower=Math.max(this.posSelectPower-1,1);
        if (this.getPlayer()!=null){
            this.getPlayer().sendSystemMessage(Component.nullToEmpty(this.posSelectPower+" Se cambio al"+this.getSelectPower().name));
        }
    }

    @Override
    public void swingHand(Player player) {
        if(this.canUsePower()){
            Power power=this.getSelectPower();
            if(!this.activesPowers.powers.containsValue(power)){
                this.castingTimer=power.castingDuration;
                power.duration=300+50*power.level;
                int i=Math.max(this.activesPowers.powers.size()-1,0);
                this.activesPowers.addPowers(i,power);
                this.lastPosSelectPower=this.posSelectPower;
                this.setLastUsingPower(power);
                this.getPlayer().sendSystemMessage(Component.nullToEmpty("Se lanzo el poder"));
            }
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        this.powers.save(tag);
        tag.putInt("select_power",this.posSelectPower);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.powers=new Powers(nbt);
        this.posSelectPower=nbt.getInt("select_power");
    }

    public void init(Player player) {
        this.setPlayer(player);
        this.level=player.level();
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
