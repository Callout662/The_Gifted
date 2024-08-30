package com.AstianBk.the_gifted.server.network;

import com.AstianBk.the_gifted.common.api.IPowerPlayer;
import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
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

    public static void handlerManagerAnimation(int id, AnimationPlayerCapability entity) {
        entity.handledPoseFly(id);
    }
    public static void handlerManagerPower(int id, PowerPlayerCapability entity) {
        entity.activesPowers.get(id).stopPower(entity);
    }
}
