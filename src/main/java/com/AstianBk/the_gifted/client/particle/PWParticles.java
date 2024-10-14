package com.AstianBk.the_gifted.client.particle;

import com.AstianBk.the_gifted.common.TheGifted;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PWParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TheGifted.MODID);

    public static final RegistryObject<SimpleParticleType> BLOOD_PARTICLES =
            PARTICLE_TYPES.register("bloodbk", () -> new SimpleParticleType(false));


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
