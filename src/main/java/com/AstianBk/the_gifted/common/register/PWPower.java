package com.AstianBk.the_gifted.common.register;

import com.AstianBk.the_gifted.server.powers.FireBoltPower;
import com.AstianBk.the_gifted.server.powers.Power;
import com.google.common.collect.Maps;

import java.util.Map;

public class PWPower {
    public static Map<String,Power> POWERS= Maps.newHashMap();

    public static Power register(String name, Power power){
        return POWERS.put(name,power);
    }
    public static Power FIRE_BOLT=new FireBoltPower();

    public static void init(){
        register("fire_bolt",FIRE_BOLT);
    }

    public static Power getPowerForName(String name){
        return POWERS.get(name);
    }
}
