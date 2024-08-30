package com.AstianBk.the_gifted.common.gui;

import com.AstianBk.the_gifted.common.TheGifted;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Arrays;
import java.util.Comparator;

public class HotBarGui implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        assert player != null;
        if(mc.options.getCameraType().isFirstPerson()){
            guiGraphics.pose().pushPose();
            int i1 = 1;
            int k1 = screenHeight/2+140;
            guiGraphics.pose().scale(0.3F,0.3F,0.3F);
            guiGraphics.blit(getGuiTextures(),i1, k1,0,0, 64,192,64,192);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();
            i1=14;
            k1=screenHeight/2+63;
            guiGraphics.pose().scale(0.45F,0.45F,0.45F);
            guiGraphics.blit(new ResourceLocation(TheGifted.MODID,"textures/gui/hotbar/upward_arrow.png"),i1, k1,0,0, 16,16,16,16);
            guiGraphics.blit(new ResourceLocation(TheGifted.MODID,"textures/gui/hotbar/downward_arrow.png"),i1, k1+19,0,0, 16,16,16,16);

            guiGraphics.pose().popPose();
        }
    }

    public ResourceLocation getGuiTextures(){
        return new ResourceLocation(TheGifted.MODID,"textures/gui/hotbar/skill_selection.png");
    }
    @OnlyIn(Dist.CLIENT)
    enum ActionType {
        FIRST(0,0, 26,7),
        SECOND(1,65, 23,10),
        THIRD(2,123, 31,0);

        private final int id;
        private final int index;
        private final int height;
        private final int distForNextAction;

        private ActionType(int id, int p_168729_, int height,int distForNextAction) {
            this.id = id;
            this.index = p_168729_;
            this.height = height;
            this.distForNextAction=distForNextAction;
        }
        private static final ActionType[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(ActionType::getId)).toArray(ActionType[]::new);


        public int getHeight() {
            return this.height;
        }

        public int getIndex(){
            return this.index;
        }

        public int getDistForNextAction() {
            return distForNextAction;
        }

        public int getId() {
            return this.id;
        }
        public static ActionType byId(int p_30987_) {
            return BY_ID[p_30987_ % BY_ID.length];
        }
    }
}
