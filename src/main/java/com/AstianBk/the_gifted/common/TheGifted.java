package com.AstianBk.the_gifted.common;

import com.AstianBk.the_gifted.common.register.PWCreativeTabs;
import com.AstianBk.the_gifted.common.register.PWItems;
import com.AstianBk.the_gifted.common.register.PWPower;
import com.AstianBk.the_gifted.server.network.PacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TheGifted.MODID)
public class TheGifted
{
    public static final String MODID = "the_gifted";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TheGifted()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        PacketHandler.registerMessages();
        PWPower.init();
        PWItems.ITEMS.register(modEventBus);
        PWCreativeTabs.TABS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}