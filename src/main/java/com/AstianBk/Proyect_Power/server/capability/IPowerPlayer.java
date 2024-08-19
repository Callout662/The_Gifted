package com.AstianBk.Proyect_Power.server.capability;

import com.AstianBk.Proyect_Power.server.powers.Power;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.jar.Attributes;

public interface IPowerPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();

    int getCooldownPower();
    void setCooldownPower();
    int getCastingTimer();
    int getStartTime();
    boolean usingPower();
    Power getUsingPower();
    void tick();
    void onJoinGame();
    void handledPower();
    void handledPassive();
    void effectPowerForTick();
    void effectPassiveForTick();
    boolean canUsePower();
    boolean haveAlterEgo();
    Map<Integer,Power> getPassives();
    Map<Integer,Power> getHotBarPower();
    boolean useResources();
    Attributes getResources();
}
