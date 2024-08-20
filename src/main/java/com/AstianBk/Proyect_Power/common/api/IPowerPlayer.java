package com.AstianBk.Proyect_Power.common.api;

import com.AstianBk.Proyect_Power.server.powers.Power;
import com.AstianBk.Proyect_Power.server.powers.Powers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import java.util.Map;

public interface IPowerPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();
    void setPlayer(Player player);
    Power getSelectPower();
    int getCooldownPower();
    int getCastingTimer();
    int getStartTime();
    boolean usingPower();
    Power getUsingPower();
    void setUsingPower(Power power);
    void tick(Player player);
    void onJoinGame(Player player, EntityJoinLevelEvent event);
    void handledPower(Player player,Power power);
    void handledPassive(Player player,Power power);
    boolean canUsePower();
    boolean haveAlterEgo();
    Map<Integer,Power> getPassives();
    Powers getHotBarPower();
    void syncPower(Player player);
    void upPower();
    void downPower();
    void swingHand(InteractionHand hand,Player player);

}
