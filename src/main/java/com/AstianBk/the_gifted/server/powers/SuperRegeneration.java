package com.AstianBk.the_gifted.server.powers;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class SuperRegeneration extends Power{
    public SuperRegeneration() {
        super("super_regeneration",0,0,0,ElementPower.NORMAL,false,false,false,true);
    }

    @Override
    public void effectPassiveForTick(Player player) {
        if(this.isDurationEffectTick(this.level,player.tickCount)){
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(1.0F);
            }
        }
    }
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int k = 50 >> p_19456_;
        if (k > 0) {
            return p_19455_ % k == 0;
        } else {
            return true;
        }
    }
}
