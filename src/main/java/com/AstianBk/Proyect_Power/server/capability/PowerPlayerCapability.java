package com.AstianBk.Proyect_Power.server.capability;

import com.AstianBk.Proyect_Power.server.powers.Power;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.jar.Attributes;

public class PowerPlayerCapability implements IPowerPlayer {
    public Power usingPower;
    Player player;
    Map<Integer,Power> powers;
    Map<Integer,Power> passives;
    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public int getCooldownPower() {
        return 0;
    }

    @Override
    public void setCooldownPower() {

    }

    @Override
    public int getCastingTimer() {
        return 0;
    }

    @Override
    public int getStartTime() {
        return 0;
    }

    @Override
    public boolean usingPower() {
        return false;
    }

    @Override
    public Power getUsingPower() {
        return null;
    }

    @Override
    public void tick() {

    }

    @Override
    public void onJoinGame() {

    }

    @Override
    public void handledPower() {

    }

    @Override
    public void handledPassive() {

    }

    @Override
    public void effectPowerForTick() {

    }

    @Override
    public void effectPassiveForTick() {

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
        return null;
    }

    @Override
    public Map<Integer, Power> getHotBarPower() {
        return null;
    }

    @Override
    public boolean useResources() {
        return false;
    }

    @Override
    public Attributes getResources() {
        return null;
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
