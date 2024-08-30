package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.server.capability.PowerInstance;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.*;

public class Powers {
    public Map<Integer, PowerInstance> powers= Maps.newHashMap();

    public Powers(Map<Integer,PowerInstance> powers){
        this.powers=powers;
    }

    public Powers(CompoundTag tag){
        if(tag.contains("powers",9)){
            ListTag listTag = tag.getList("powers",10);
            for(int i = 0 ; i<listTag.size() ; i++){
                CompoundTag tag1=listTag.getCompound(i);
                if(tag1.contains("name")){
                    int pos=tag1.getInt("pos");
                    Power power = new Power(tag1);
                    this.powers.put(pos, new PowerInstance(power,0));
                }
            }
        }
    }

    public void save(CompoundTag tag){
        ListTag listtag = new ListTag();
        for (int i=1;i<this.powers.size()+1;i++){
            if(this.powers.get(i)!=null){
                Power power=this.powers.get(i).getPower();
                CompoundTag tag1=new CompoundTag();
                tag1.putString("name",power.name);
                tag1.putInt("pos",i);
                power.save(tag1);
                listtag.add(tag1);
            }
        }
        if(!listtag.isEmpty()){
            tag.put("powers",listtag);
        }
    }

    public Power getForName(String name){
        Power power =Power.NONE;
        for (PowerInstance powerInstance:this.getPowers()){
            Power power1=powerInstance.getPower();
            if(power1.name.equals(name)){
                System.out.print("\n----Entro----\n");
                power=power1;
            }
        }
        return power;
    }

    public void addPowers(int pos,Power power){
        this.powers.put(pos,new PowerInstance(power,0));
    }

    public Collection<PowerInstance> getPowers() {
        return this.powers.values();
    }

    public Power get(int pos){
        return this.powers.get(pos).getPower();
    }
}
