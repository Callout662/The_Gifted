package com.AstianBk.the_gifted.common.keybind;

import com.AstianBk.the_gifted.common.TheGifted;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.KeyEvent;

@Mod.EventBusSubscriber(modid = TheGifted.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BKKeybinds {
    public static KeyMapping attackKey1;
    public static KeyMapping attackKey2;


    @SubscribeEvent
    public static void register(final RegisterKeyMappingsEvent event) {
        attackKey1 = create("attack_key1", KeyEvent.VK_Z);
        attackKey2 = create("attack_key2", KeyEvent.VK_C);

        event.register(attackKey1);
        event.register(attackKey2);
    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + TheGifted.MODID + "." + name, key, "key.category." + TheGifted.MODID);
    }
}
