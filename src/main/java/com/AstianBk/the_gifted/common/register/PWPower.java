package com.AstianBk.the_gifted.common.register;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.server.powers.*;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class PWPower {
    public static Map<ResourceLocation,Power> POWERS= Maps.newHashMap();

    public static Power register(ResourceLocation name, Power power){
        return POWERS.put(name,power);
    }
    public static FireBoltPower FIRE_BOLT=new FireBoltPower();
    public static SuperSpeedPower SUPER_SPEED =new SuperSpeedPower();
    public static FlyPower FLY=new FlyPower();
    public static LaserPower LASER=new LaserPower();

    public static void init(){
        register(new ResourceLocation(TheGifted.MODID,"fire_bolt"),FIRE_BOLT);
        register(new ResourceLocation(TheGifted.MODID,"super_speed"), SUPER_SPEED);
        register(new ResourceLocation(TheGifted.MODID,"fly"),FLY);
        register(new ResourceLocation(TheGifted.MODID,"laser"),LASER);
    }

    public static Power getPowerForName(String name){
        ResourceLocation resourceLocation=new ResourceLocation(TheGifted.MODID,name);
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation) : Power.NONE;
    }

    public static Power getPowerForLocation(ResourceLocation resourceLocation){
        return POWERS.get(resourceLocation)!=null ? POWERS.get(resourceLocation) : Power.NONE;

    }
}
