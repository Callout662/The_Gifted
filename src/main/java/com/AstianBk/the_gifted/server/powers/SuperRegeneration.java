package com.AstianBk.the_gifted.server.powers;

import com.AstianBk.the_gifted.client.particle.PWParticles;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.manager.RegenerationInstance;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class SuperRegeneration extends Power{
    public SuperRegeneration() {
        super("super_regeneration",0,0,0,ElementPower.NORMAL,false,false,false,true);
    }

    @Override
    public void effectPassiveForTick(Player player, PowerPlayerCapability powerPlayerCapability) {
        if(this.isDurationEffectTick(player.tickCount,this.level+4) && !PowerPlayerCapability.get(player).noMoreLimbs()){
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(1.0F);
            }
        }
        if(player.level().isClientSide){
            if(powerPlayerCapability.noMoreLimbs()){
                RegenerationInstance instance=powerPlayerCapability.getLimbsPartRegeneration().loseLimbs.get("head");
                if(instance!=null){
                    float porcentBlood=instance.getCooldownPercent();
                    float f=5.0F*porcentBlood;
                    ParticleOptions particleoptions = PWParticles.BLOOD_PARTICLES.get();
                    int i;
                    float f1;
                    i = Mth.ceil((float)Math.PI * f * f);
                    f1 = f;

                    for(int k=0;k<10;k++){
                        for(int j = 0; j < i; ++j) {
                            float f2 = player.getRandom().nextFloat() * ((float)Math.PI * 2F);
                            float f3 = Mth.sqrt(player.getRandom().nextFloat()) * f1;
                            double d0 = player.getX() + (double)(Mth.cos(f2) * f3);
                            double d2 = player.getY()+0.3D;
                            double d4 = player.getZ() + (double)(Mth.sin(f2) * f3);

                            player.level().addParticle(particleoptions, d0, d2, d4, 0.0F, 0.1F, 0.0F);
                        }
                    }
                }
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
