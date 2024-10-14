package com.AstianBk.the_gifted.server.network;

import com.AstianBk.the_gifted.common.api.IPowerPlayer;
import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;

public class Handler {

    public static void handledForKey(int key,IPowerPlayer player,int action){
        switch (key){
            case 0x5A->{
                upPower(player);
            }
            case 0x43->{
                downPower(player);
            }
            case 1->{
                if(action==0){
                    player.stopCasting(player.getSelectPower(),player.getPlayer());
                }else if(action==1){
                    player.startCasting(player.getPlayer());
                }
            }
        }
    }
    public static void upPower(IPowerPlayer player){
        player.upPower();
    }
    public static void downPower(IPowerPlayer player){
        player.downPower();
    }

    public static void handlerManagerAnimation(int id, AnimationPlayerCapability entity) {
        entity.handledPoseFly(id);
    }

}
