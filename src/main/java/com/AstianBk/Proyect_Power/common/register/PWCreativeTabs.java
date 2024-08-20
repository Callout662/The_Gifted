package com.AstianBk.Proyect_Power.common.register;

import com.AstianBk.Proyect_Power.common.ProjectPower;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PWCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProjectPower.MODID);

    public static final RegistryObject<CreativeModeTab> PW_MOBS_TAB = TABS.register(ProjectPower.MODID,()-> CreativeModeTab.builder()
            .icon(()->new ItemStack(Items.FIRE_CHARGE))
            .title(Component.translatable("itemGroup.project_power"))
            .displayItems((s,a)-> {
                //a.accept(PWItems.SWORD.get());
            })
            .build());
}
