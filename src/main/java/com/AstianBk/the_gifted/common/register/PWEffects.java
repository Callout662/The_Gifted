package com.AstianBk.the_gifted.common.register;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.common.effect.SuperSpeed;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PWEffects {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TheGifted.MODID);

    public static final RegistryObject<MobEffect> SUPER_SPEED = EFFECT.register("super_speed", SuperSpeed::new);

}
