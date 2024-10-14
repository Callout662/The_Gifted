package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncData {
    private final int pos;

    public PacketSyncData(FriendlyByteBuf buf) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        this.pos = buf.readInt();
    }

    public PacketSyncData(int pos) {
        this.pos = pos;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.pos);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(this::sync);
        context.get().setPacketHandled(true);
    }


    @OnlyIn(Dist.CLIENT)
    public void sync(){
        Minecraft mc=Minecraft.getInstance();
        Player player=mc.player;
        assert player!=null;
        PowerPlayerCapability.get(player).setPosSelectPower(this.pos);
    }
}
