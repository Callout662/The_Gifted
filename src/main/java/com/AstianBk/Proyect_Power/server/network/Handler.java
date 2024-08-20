package com.AstianBk.Proyect_Power.server.network;

import com.AstianBk.Proyect_Power.common.api.IPowerPlayer;
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
}
