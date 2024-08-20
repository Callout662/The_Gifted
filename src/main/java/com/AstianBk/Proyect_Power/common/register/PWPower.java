package com.AstianBk.Proyect_Power.common.register;

import com.AstianBk.Proyect_Power.server.powers.FireBoltPower;
import com.AstianBk.Proyect_Power.server.powers.Power;
import com.google.common.collect.Maps;

import java.util.Map;

public class PWPower {
    public static Map<String,Power> POWERS= Maps.newHashMap();

    public static void register(String name, Power power){
        POWERS.put(name,power);
    }

    public static void init(){
        register("fire_bolt",new FireBoltPower());
    }

    public static Power getPowerForName(String name){
        return POWERS.get(name);
    }
}
