package com.AstianBk.Proyect_Power.common;

import com.AstianBk.Proyect_Power.common.keybind.BKKeybinds;
import com.AstianBk.Proyect_Power.common.register.PWCreativeTabs;
import com.AstianBk.Proyect_Power.common.register.PWItems;
import com.AstianBk.Proyect_Power.common.register.PWPower;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ProjectPower.MODID)
public class ProjectPower
{
    public static final String MODID = "project_power";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ProjectPower()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        PWPower.init();
        PWItems.ITEMS.register(modEventBus);
        PWCreativeTabs.TABS.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
