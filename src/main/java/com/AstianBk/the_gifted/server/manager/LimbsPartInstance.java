package com.AstianBk.the_gifted.server.manager;

import com.AstianBk.the_gifted.common.api.Limbs;
import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public class LimbsPartInstance {
    public ServerPlayer serverPlayer;
    public Map<String,Limbs> loseLimbs;
    public LimbsPartInstance(ServerPlayer player){
        this.loseLimbs= Maps.newHashMap();
        this.serverPlayer=player;
    }


}
