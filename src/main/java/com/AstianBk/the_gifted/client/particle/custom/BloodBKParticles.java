package com.AstianBk.the_gifted.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class BloodBKParticles extends WaterDropParticle{
    protected BloodBKParticles(ClientLevel p_108484_, double p_108485_, double p_108486_, double p_108487_, double xSpeed, double ySpeed, double zSpeed ) {
        super(p_108484_, p_108485_, p_108486_, p_108487_);
        this.xd *= 0.1;
        this.yd *= 0.1;
        this.zd *= 0.1;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;
        this.setColor(1.0f,0.0f,0.0f);
        this.scale(2.0F);
        this.lifetime=20;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            if (!this.onGround) {
                this.yd -= (double)this.gravity;
                this.move(this.xd, this.yd, this.zd);
                this.xd *= (double)0.98F;
                this.yd *= (double)0.98F;
                this.zd *= (double)0.98F;
            }
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BloodBKParticles bk_particles=new BloodBKParticles(world,x,y,z,xSpeed,ySpeed,zSpeed);
            bk_particles.pickSprite(this.spriteSet);
            return bk_particles;
        }
    }
}
