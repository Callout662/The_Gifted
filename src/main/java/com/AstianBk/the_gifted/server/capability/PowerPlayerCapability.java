package com.AstianBk.the_gifted.server.capability;

import com.AstianBk.the_gifted.common.api.IPowerPlayer;
import com.AstianBk.the_gifted.common.register.PWPower;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketHandlerPower;
import com.AstianBk.the_gifted.server.powers.Power;
import com.AstianBk.the_gifted.server.powers.Powers;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
    public Power usingPower=Power.NONE;
    Player player;
    Level level;
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
        if(this.getUsingPower()!=null && this.usingPower()){
            if(this.castingTimer==this.getUsingPower().lauchTime){
                this.handledPower(player,this.getUsingPower());
            }
            this.castingTimer--;
            if(this.castingTimer==0){
                this.stopPower(player,this.getUsingPower());
                this.setUsingPower(Power.NONE);
            }
        }
    }
    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        if(cap!=null){
            for (int i=1;i<6;i++){
                this.powers.addPowers(i, PWPower.FIRE_BOLT);
            }
        }
    }

    @Override
    public void handledPower(Player player,Power power) {
        player.sendSystemMessage(Component.nullToEmpty("entro"));
        power.startPower(player);
    }
    @Override
    public void stopPower(Player player,Power power){
        power.setCooldownTimer(300);
        power.stopPower(player);
    }

    @Override
    public void handledPassive(Player player,Power power) {

    }


    @Override
    public boolean canUsePower() {
        return this.getSelectPower().cooldownTimer<=0;
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
    public void swingHand(Player player) {
        this.getPlayer().sendSystemMessage(Component.nullToEmpty(String.valueOf( "es "+this.level.isClientSide+":"+this.getSelectPower().cooldownTimer)));
        if(this.canUsePower()){
            if(this.usingPower==Power.NONE){
                Power power=this.getSelectPower();
                this.castingTimer=power.castingDuration;
                this.setUsingPower(power);
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
