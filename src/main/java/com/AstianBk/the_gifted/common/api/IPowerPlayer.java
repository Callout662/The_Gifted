package com.AstianBk.the_gifted.common.api;

import com.AstianBk.the_gifted.server.powers.Power;
import com.AstianBk.the_gifted.server.powers.Powers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import java.util.Map;

public interface IPowerPlayer extends INBTSerializable<CompoundTag> {
    Player getPlayer();
    void setPlayer(Player player);
    Power getSelectPower();
    Power getPowerForHotBar(int pos);
    int getCooldownPower();
    int getCastingTimer();
    int getStartTime();
    boolean lastUsingPower();
    Power getLastUsingPower();
    void setLastUsingPower(Power power);
    void tick(Player player);
    void onJoinGame(Player player, EntityJoinLevelEvent event);
    void handledPower(Player player,Power power);
    public void stopPower(Power power);
    void handledPassive(Player player,Power power);
    boolean canUsePower();
    boolean haveAlterEgo();
    Map<Integer,Power> getPassives();
    Powers getHotBarPower();
    void syncPower(Player player);
    void upPower();
    void downPower();
    void startCasting(Player player);
    void stopCasting(Power power,Player player);


}
