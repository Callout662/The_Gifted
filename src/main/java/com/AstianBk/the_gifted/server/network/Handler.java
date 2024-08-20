package com.AstianBk.the_gifted.server.network;

import com.AstianBk.the_gifted.common.api.IPowerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Handler {

    public static void handledForKey(int key,IPowerPlayer player){
        switch (key){
            case 0x5A->{
                upPower(player);
            }
            case 0x43->{
                downPower(player);
            }
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static void upPower(IPowerPlayer player){
        player.upPower();
    }
    @OnlyIn(Dist.CLIENT)
    public static void downPower(IPowerPlayer player){
        player.downPower();
    }

    public static void handlerManager(int id, IPowerPlayer entity) {
        switch (id){
            case 0->{
                entity.swingHand(entity.getPlayer());
            }
            case 1->{
                entity.getUsingPower().startPower(entity.getPlayer());
            }
        }
    }
}
