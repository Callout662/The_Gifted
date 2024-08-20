package com.AstianBk.Proyect_Power.common.keybind;

import com.AstianBk.Proyect_Power.common.ProjectPower;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.KeyEvent;

@Mod.EventBusSubscriber(modid = ProjectPower.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BKKeybinds {
    public static KeyMapping attackKey1;


    @SubscribeEvent
    public static void register(final RegisterKeyMappingsEvent event) {
        attackKey1 = create("attack_key1", KeyEvent.VK_Z);

        event.register(attackKey1);
    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + ProjectPower.MODID + "." + name, key, "key.category." +ProjectPower.MODID);
    }
}
