package com.AstianBk.the_gifted.server.capability;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.common.api.IPowerPlayer;
import com.AstianBk.the_gifted.server.manager.*;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketSyncActivePowers;
import com.AstianBk.the_gifted.server.network.message.PacketSyncData;
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
    public LimbsPartRegeneration limbsPartRegeneration=new LimbsPartRegeneration();
    Map<Integer,Power> passives= Maps.newHashMap();
    int posSelectPower=1;
    int castingTimer=0;
    public PlayerCooldowns cooldowns=new PlayerCooldowns();
    public ActiveEffectDuration durationEffect=new ActiveEffectDuration();
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
    public boolean legsLess(){
        return this.getLimbsPartRegeneration().loseLimb("left_leg") && this.getLimbsPartRegeneration().loseLimb("right_leg");
    }
    public boolean armsLess(){
        return this.getLimbsPartRegeneration().loseLimb("left_arm") && this.getLimbsPartRegeneration().loseLimb("right_arm");
    }
    public boolean bodyLess(){
        return this.getLimbsPartRegeneration().loseLimb("body");
    }
    public boolean headLess(){
        return this.getLimbsPartRegeneration().loseLimb("head");
    }
    public boolean noMoreLimbs(){
        return this.legsLess() && this.bodyLess() && this.armsLess() && this.headLess();
    }
    public boolean cantMove(){
        return this.legsLess() || this.bodyLess();
    }

    @Override
    public void tick(Player player) {
        if(player instanceof ServerPlayer){
            if(this.getLimbsPartRegeneration().hasRegenerationLimbs()){
                this.getLimbsPartRegeneration().tick(player);
            }
            if (this.cooldowns.hasCooldownsActive()){
                this.cooldowns.tick(1);
            }
            if(this.durationEffect.hasDurationsActive()){
                this.durationEffect.tick(1,this);
            }
        }else if(this.level.isClientSide){
            if (this.cooldowns.hasCooldownsActive()){
                this.cooldowns.tick(1);
            }
            if(this.durationEffect.hasDurationsActive()){
                this.durationEffect.tickDurations();
            }
            if(this.getLimbsPartRegeneration().hasRegenerationLimbs()){
                this.getLimbsPartRegeneration().tick(player);
            }
        }
        if(!this.powers.getPowers().isEmpty()){
            this.powers.getPowers().forEach(e->{
                if(this.durationEffect.hasDurationForPower(e.getPower())){
                    e.getPower().tick(this);
                    this.durationEffect.decrementDurationCount(e.power);
                }else if(e.power.isPassive){
                    e.power.effectPassiveForTick(player,this);
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
    public void stopPower(Power power){
        power.stopPower(this);
    }

    @Override
    public void handledPassive(Player player,Power power) {

    }

    public void clone(PowerPlayerCapability newCap,Player player,Player newPlayer){

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



    public void setPosSelectPower(int posSelectPower){
        if(!this.level.isClientSide){
            if(this.getPlayer() instanceof ServerPlayer player){
                PacketHandler.sendToPlayer(new PacketSyncData(posSelectPower),player);

            }
        }else {
            this.posSelectPower=posSelectPower;
        }
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
        return this.durationEffect;
    }

    public LimbsPartRegeneration getLimbsPartRegeneration() {
        return this.limbsPartRegeneration;
    }

    @Override
    public void upPower() {
        this.posSelectPower=Math.min(this.posSelectPower+1,this.powers.powers.size());

        Power power=this.getPowerForHotBar(this.posSelectPower);
        this.stopCasting(power,this.getPlayer());

        this.getPlayer().sendSystemMessage(Component.nullToEmpty(power.name));
        this.setPosSelectPower(this.posSelectPower);

    }

    @Override
    public void downPower() {
        this.posSelectPower=Math.max(this.posSelectPower-1,1);

        Power power=this.getPowerForHotBar(this.posSelectPower);
        this.stopCasting(power,this.getPlayer());

        this.getPlayer().sendSystemMessage(Component.nullToEmpty(power.name));
        this.setPosSelectPower(this.posSelectPower);

    }

    @Override
    public void startCasting(Player player) {
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketSyncActivePowers(player,0,this.getSelectPower()), (ServerPlayer) player);
        }
        if(this.canUsePower()){
            if(!this.durationEffect.hasDurationForPower(this.getSelectPower()) && !this.getSelectPower().isPassive){
                Power power=this.getSelectPower();
                DurationInstance instance=new DurationInstance(power.name,power.level,power.castingDuration,200);
                this.addActiveEffect(instance,player);
                this.setLastUsingPower(this.getSelectPower());
            }
        }
    }

    @Override
    public void stopCasting(Power power, Player player) {
        if(!this.level.isClientSide){
            PacketHandler.sendToPlayer(new PacketSyncActivePowers(player,1,power), (ServerPlayer) player);
        }
        if(power.isCasting()){
            if(this.durationEffect.hasDurationForPower(power)){
                TheGifted.LOGGER.debug("Habilidad removida");
                this.durationEffect.removeDuration(this.durationEffect.getDurationInstance(power.name),DurationResult.CLICKUP);
            }
        }
    }

    public void losePart(String id, RegenerationInstance instance, Player player){
        this.limbsPartRegeneration.addLoseLimb(id,instance);
        if(player instanceof ServerPlayer){
            this.limbsPartRegeneration.syncPlayer();
        }
    }
    public void addActiveEffect(DurationInstance  instance,Player player){
        this.durationEffect.forceAddDuration(instance);
        if(player instanceof  ServerPlayer){
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
}
