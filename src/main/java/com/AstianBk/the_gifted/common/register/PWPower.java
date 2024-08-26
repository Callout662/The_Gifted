package com.AstianBk.the_gifted.common.register;

import com.AstianBk.the_gifted.server.powers.FireBoltPower;
import com.AstianBk.the_gifted.server.powers.FlyPower;
import com.AstianBk.the_gifted.server.powers.Power;
import com.AstianBk.the_gifted.server.powers.SuperSpeedPower;
import com.google.common.collect.Maps;

import java.util.Map;

public class PWPower {
    public static Map<String,Power> POWERS= Maps.newHashMap();

    public static Power register(String name, Power power){
        return POWERS.put(name,power);
    }
    public static Power FIRE_BOLT=new FireBoltPower();
    public static Power SUPER_SPEED =new SuperSpeedPower();
    public static Power FLY=new FlyPower();

    public static void init(){
        register("fire_bolt",FIRE_BOLT);
        register("super_speed", SUPER_SPEED);
        register("fly",FLY);
    }

    public static Power getPowerForName(String name){
        return POWERS.get(name);
    }
}
