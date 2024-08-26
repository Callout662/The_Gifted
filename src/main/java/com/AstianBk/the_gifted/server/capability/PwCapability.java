package com.AstianBk.the_gifted.server.capability;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class PwCapability {
    public static final Capability<PowerPlayerCapability> POWER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<AnimationPlayerCapability> ANIMATION_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PowerPlayerCapability.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PowerPlayerCapability> T getEntityCap(Entity entity, Class<T> type) {
        if (entity != null) {
            PowerPlayerCapability entitypatch = entity.getCapability(PwCapability.POWER_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends AnimationPlayerCapability> T getEntityPatch(Entity entity, Class<T> type) {
        if (entity != null) {
            AnimationPlayerCapability entitypatch = entity.getCapability(PwCapability.ANIMATION_CAPABILITY).orElse(null);

            if (entitypatch != null && type.isAssignableFrom(entitypatch.getClass())) {
                return (T)entitypatch;
            }
        }

        return null;
    }


}
