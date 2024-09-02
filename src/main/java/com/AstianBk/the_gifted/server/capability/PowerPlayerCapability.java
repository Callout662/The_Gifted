package com.AstianBk.the_gifted.server.capability;

import com.AstianBk.the_gifted.common.api.ILimbsLose;
import com.AstianBk.the_gifted.common.api.IPowerPlayer;
import com.AstianBk.the_gifted.server.manager.DurationInstance;
import com.AstianBk.the_gifted.server.manager.LimbsPartRegeneration;
import com.AstianBk.the_gifted.server.manager.PlayerCooldowns;
import com.AstianBk.the_gifted.server.manager.ActiveEffectDuration;
import com.AstianBk.the_gifted.server.powers.*;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
    public Power lastUsingPower =Power.NONE;
    Player player;
    Level level;
    public Powers powers=new Powers(Maps.newHashMap());
    public Powers activesPowers=new Powers(Maps.newHashMap());
    public LimbsPartRegeneration limbsPartRegeneration;
    Map<Integer,Power> passives= Maps.newHashMap();
    int posSelectPower=1;
    int castingTimer=0;
    public PlayerCooldowns cooldowns=new PlayerCooldowns();
    public ActiveEffectDuration durationEffect;
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
        if(player instanceof ServerPlayer){
            if (this.cooldowns.hasCooldownsActive()){
                this.cooldowns.tick(1);
            }
            if(this.durationEffect.hasDurationsActive()){
                this.durationEffect.tick(1,this);
            }
            this.syncPower(player);
        }else if(this.level.isClientSide){
            if (this.cooldowns.hasCooldownsActive()){
                this.cooldowns.tick(1);
            }
            if(this.durationEffect.hasDurationsActive()){
                this.durationEffect.tickDurations();
            }
        }
        if(!this.powers.getPowers().isEmpty()){
            this.powers.getPowers().forEach(e->{
                if(this.durationEffect.hasDurationForPower(e.getPower())){
                    e.getPower().tick(this);
                    this.durationEffect.decrementDurationCount(e.power);
                }else if(e.power.isPassive){
                    e.power.effectPassiveForTick(player);
                }
            });
        }
        if(this.getLastUsingPower()!=null && this.lastUsingPower()){
            if(this.castingTimer==this.getLastUsingPower().lauchTime){
                this.handledPower(player,this.getLastUsingPower());
            }
            this.castingTimer--;
        }
    }

    @Override
    public void onJoinGame(Player player, EntityJoinLevelEvent event) {
        PowerPlayerCapability cap=PowerPlayerCapability.get(player);
        if(cap!=null){
            this.syncPower(player);
            this.powers.addPowers(1,new FireBoltPower());
            this.powers.addPowers(2,new SuperSpeedPower());
            this.powers.addPowers(3,new FlyPower());
            this.powers.addPowers(4,new LaserPower());
            this.powers.addPowers(5,new SuperRegeneration());
        }
    }

    @Override
    public void handledPower(Player player,Power power) {
        power.startPower(player);
    }
    @Override
    public void stopPower(Player player,Power power){
    }

    @Override
    public void handledPassive(Player player,Power power) {

    }


    @Override
    public boolean canUsePower() {
        return this.getSelectPower()!=null && !this.cooldowns.isOnCooldown(this.getSelectPower()) && this.castingTimer<=0;
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

    public void syncData(int posSelectPower){
        this.posSelectPower=posSelectPower;
    }
    @Override
    public void syncPower(Player player) {
        if(player instanceof ServerPlayer serverPlayer){
            this.cooldowns.syncToPlayer(serverPlayer);
            this.durationEffect.syncAllToPlayer();
            this.limbsPartRegeneration.syncPlayer();
        }
    }

    public ActiveEffectDuration getDurationEffect() {
        return durationEffect;
    }

    public LimbsPartRegeneration getLimbsPartRegeneration() {
        return limbsPartRegeneration;
    }

    @Override
    public void upPower() {
        this.posSelectPower=Math.min(this.posSelectPower+1,this.powers.powers.size());
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
            if(!this.durationEffect.hasDurationForPower(this.getSelectPower()) && !this.getSelectPower().isPassive){
                Power power=this.getSelectPower();
                DurationInstance instance=new DurationInstance(power.name,power.level,power.castingDuration+50*power.level,200);
                this.castingTimer=this.getSelectPower().castingDuration;
                this.addActiveEffect(instance,player);
                this.setLastUsingPower(this.getSelectPower());
                this.getPlayer().sendSystemMessage(Component.nullToEmpty("Se lanzo el poder"));
            }
        }
    }
    public void addActiveEffect(DurationInstance  instance,Player player){
        this.durationEffect.forceAddDuration(instance);
        if(player instanceof  ServerPlayer serverPlayer){
            this.durationEffect.syncToPlayer(instance);
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag=new CompoundTag();
        this.powers.save(tag);
        tag.putInt("select_power",this.posSelectPower);
        if(this.cooldowns.hasCooldownsActive()){
            tag.put("cooldowns",this.cooldowns.saveNBTData());
        }
        if(this.durationEffect.hasDurationsActive()){
            tag.put("activeEffect",this.durationEffect.saveNBTData());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.powers=new Powers(nbt);
        this.posSelectPower=nbt.getInt("select_power");
        if(nbt.contains("cooldowns")){
            ListTag listTag=new ListTag();
            this.cooldowns.loadNBTData(listTag);
        }
        if(nbt.contains("activeEffect")){
            ListTag listTag=new ListTag();
            this.durationEffect.loadNBTData(listTag);
        }

    }

    public void init(Player player) {
        this.setPlayer(player);
        this.level=player.level();
        if(player instanceof ServerPlayer serverPlayer){
            this.durationEffect=new ActiveEffectDuration(serverPlayer);
            this.limbsPartRegeneration=new LimbsPartRegeneration(serverPlayer);
        }
    }

    public PlayerCooldowns getCooldowns() {
        return this.cooldowns;
    }
    public ActiveEffectDuration getActiveEffectDuration(){
        return this.durationEffect;
    }
    public void setActiveEffectDuration(ActiveEffectDuration activeEffectDuration){
        this.durationEffect=activeEffectDuration;
    }
    public void setLimbsPartRegeneration(LimbsPartRegeneration limbsPartRegeneration){
        this.limbsPartRegeneration=limbsPartRegeneration;
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

    public record SyncData(int pos) {
    }
}
