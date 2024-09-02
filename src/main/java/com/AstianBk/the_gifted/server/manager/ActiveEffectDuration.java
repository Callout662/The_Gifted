package com.AstianBk.the_gifted.server.manager;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.AstianBk.the_gifted.server.network.message.PacketRemoveActiveEffect;
import com.AstianBk.the_gifted.server.network.message.PacketActiveEffect;
import com.AstianBk.the_gifted.server.network.message.PacketSyncDurationEffect;
import com.AstianBk.the_gifted.server.powers.Power;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ActiveEffectDuration {
    private static final DurationInstance EMPTY = new DurationInstance(Power.NONE.name, 0, 0, 0);
    private final Map<String, DurationInstance> recastLookup;

    private final ServerPlayer serverPlayer;

    public ActiveEffectDuration() {
        this.recastLookup = Maps.newHashMap();
        this.serverPlayer = null;
    }

    public ActiveEffectDuration(ServerPlayer serverPlayer) {
        this.recastLookup = Maps.newHashMap();
        this.serverPlayer = serverPlayer;
    }

    @OnlyIn(Dist.CLIENT)
    public ActiveEffectDuration(Map<String, DurationInstance> recastLookup) {
        this.recastLookup = recastLookup;
        this.serverPlayer = null;
    }

    public boolean addDuration(DurationInstance recastInstance, PowerPlayerCapability magicData) {
        var existingDurationInstance = recastLookup.get(recastInstance.powerId);

        if (!isDurationActive(existingDurationInstance)) {
            magicData.getCooldowns().removeCooldown(recastInstance.powerId);
            recastLookup.put(recastInstance.powerId, recastInstance);
            syncToPlayer(recastInstance);
            return true;
        }

        return false;
    }

    public boolean isDurationActive(DurationInstance recastInstance) {
        return recastInstance != null && recastInstance.remainingDuration > 0 && recastInstance.remainingTicks > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void removeDuration(String powerId) {
        recastLookup.remove(powerId);
    }

    @OnlyIn(Dist.CLIENT)
    public void forceAddDuration(DurationInstance recastInstance) {
        recastLookup.put(recastInstance.powerId, recastInstance);
    }

    @OnlyIn(Dist.CLIENT)
    public void tickDurations() {
        if (!recastLookup.isEmpty()) {
            recastLookup.values().stream().toList().forEach(x -> x.remainingTicks--);
        }
    }

    public boolean hasDurationsActive() {
        return !recastLookup.isEmpty();
    }

    public boolean hasDurationForPower(Power power) {
        return isDurationActive(recastLookup.get(power.name));
    }

    public boolean hasDurationForPower(String powerId) {
        return isDurationActive(recastLookup.get(powerId));
    }

    public int getRemainingDurationsForPower(String powerId) {
        var recastInstance = recastLookup.getOrDefault(powerId, EMPTY);

        if (isDurationActive(recastInstance)) {
            return recastInstance.remainingDuration;
        }

        return 0;
    }

    public int getRemainingDurationsForPower(Power power) {
        return getRemainingDurationsForPower(power.name);
    }

    public DurationInstance getDurationInstance(String powerId) {
        return recastLookup.get(powerId);
    }

    public List<DurationInstance> getAllDurations() {
        return recastLookup.values().stream().toList();
    }

    public List<DurationInstance> getActiveDurations() {
        return recastLookup.values().stream().filter(this::isDurationActive).toList();
    }

    public void decrementDurationCount(String powerId) {
        //IronsSpellbooks.LOGGER.debug("PlayerDurations: {} {}", serverPlayer, powerId);
        var recastInstance = recastLookup.get(powerId);

        if (isDurationActive(recastInstance)) {
            recastInstance.remainingDuration--;

            if (recastInstance.remainingDuration > 0) {
                recastInstance.remainingTicks = recastInstance.ticksToLive;
                syncToPlayer(recastInstance);
            } else {
                removeDuration(recastInstance, DurationResult.TIMEOUT);
            }
        } else if (recastInstance != null) {
            removeDuration(recastInstance, DurationResult.TIMEOUT);
        }
    }

    public void decrementDurationCount(Power power) {
        decrementDurationCount(power.name);
    }

    public void syncAllToPlayer() {
        if (serverPlayer != null) {
            PacketHandler.sendToPlayer(new PacketSyncDurationEffect(recastLookup), serverPlayer);
        }
    }

    public void syncToPlayer(DurationInstance recastInstance) {
        if (serverPlayer != null) {
            PacketHandler.sendToPlayer(new PacketActiveEffect(recastInstance), serverPlayer);
        }
    }

    public void syncRemoveToPlayer(String powerId) {
        if (serverPlayer != null) {
            PacketHandler.sendToPlayer(new PacketRemoveActiveEffect(powerId), serverPlayer);
        }
    }

    private void removeDuration(DurationInstance recastInstance, DurationResult recastResult, boolean doSync) {
        recastLookup.remove(recastInstance.powerId);
        if (doSync) {
            syncRemoveToPlayer(recastInstance.powerId);
        }
    }

    public void removeDuration(DurationInstance recastInstance, DurationResult recastResult) {
        removeDuration(recastInstance, recastResult, true);
    }

    public void removeAll(DurationResult recastResult) {
        recastLookup.values().stream().toList().forEach(recastInstance -> removeDuration(recastInstance, recastResult, false));
        syncAllToPlayer();
    }

    public ListTag saveNBTData() {
        var listTag = new ListTag();
        recastLookup.values().stream().filter(this::isDurationActive).forEach(recastInstance -> {
            if (recastInstance.remainingDuration > 0 && recastInstance.remainingTicks > 0) {
                listTag.add(recastInstance.serializeNBT());
            }
        });
        return listTag;
    }

    public void loadNBTData(ListTag listTag) {
        if (listTag != null) {
            listTag.forEach(tag -> {
                var recastInstance = new DurationInstance();
                recastInstance.deserializeNBT((CompoundTag) tag);
                if (recastInstance.remainingDuration > 0 && recastInstance.remainingTicks > 0) {
                    recastLookup.put(recastInstance.powerId, recastInstance);
                } else {
                    //cull anything leftover not removed. shouldn't get here
                }
            });
        }
    }

    public void tick(int actualTicks, PowerPlayerCapability powerPlayerCapability) {
        if (serverPlayer != null && serverPlayer.level().getGameTime() % actualTicks == 0) {
            recastLookup.values()
                    .stream()
                    .filter(r -> {
                        r.remainingTicks -= actualTicks;
                        return r.remainingTicks <= 0;
                    })
                    .toList()
                    .forEach(recastInstance ->{
                        removeDuration(recastInstance, DurationResult.TIMEOUT);
                        powerPlayerCapability.powers.getPowers().forEach(e->{
                            if(Objects.equals(e.getPower().name, recastInstance.getSpellId())){
                                e.getPower().stopPower(powerPlayerCapability);
                            }
                        });
                    });
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();

        recastLookup.values().forEach(recastInstance -> {
            sb.append(recastInstance.toString());
            sb.append("\n");
        });

        return sb.toString();
    }
}
