package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.common.register.PWPower;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.Collection;
import java.util.Map;

public class Powers {
    public Map<Integer,Power> powers= Maps.newHashMap();

    public Powers(Map<Integer,Power> powers){
        this.powers=powers;
    }

    public Powers(CompoundTag tag){
        if(tag.contains("powers",9)){
            ListTag listTag = tag.getList("powers",10);
            for(int i = 0 ; i<listTag.size() ; i++){
                CompoundTag tag1=listTag.getCompound(i);
                String name=tag1.getString("name");
                int pos=tag1.getInt("pos");
                Power power = PWPower.getPowerForName(name);
                this.powers.put(pos, power);
            }
        }
    }

    public void save(CompoundTag tag){
        ListTag listtag = new ListTag();
        for (int i=1;i<this.powers.size()+1;i++){
            if(this.powers.get(i)!=null){
                Power power=this.powers.get(i);
                CompoundTag tag1=new CompoundTag();
                tag1.putString("name",power.name);
                tag1.putInt("pos",i);
                listtag.add(tag1);
            }
        }
        if(!listtag.isEmpty()){
            tag.put("powers",listtag);
        }
    }


    public void addPowers(int pos,Power power){
        this.powers.put(pos,power);
    }

    public Collection<Power> getPowers() {
        return this.powers.values();
    }

    public Power get(int pos){
        return this.powers.get(pos);
    }
}
