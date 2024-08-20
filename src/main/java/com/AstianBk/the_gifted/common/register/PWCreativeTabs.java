package com.AstianBk.the_gifted.common.register;

import com.AstianBk.the_gifted.common.TheGifted;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PWCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TheGifted.MODID);

    public static final RegistryObject<CreativeModeTab> PW_MOBS_TAB = TABS.register(TheGifted.MODID,()-> CreativeModeTab.builder()
            .icon(()->new ItemStack(Items.FIRE_CHARGE))
            .title(Component.translatable("itemGroup.the_gifted"))
            .displayItems((s,a)-> {
                //a.accept(PWItems.SWORD.get());
            })
            .build());
}
