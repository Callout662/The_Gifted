package com.AstianBk.Proyect_Power.server.network.message;

import com.AstianBk.Proyect_Power.server.capability.PowerPlayerCapability;
import com.AstianBk.Proyect_Power.server.network.Handler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKeySync implements Packet<PacketListener> {
    private final int key;


    public PacketKeySync(FriendlyByteBuf buf) {
        this.key=buf.readInt();
    }

    public PacketKeySync(int key) {
        this.key = key;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.key);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            Player player = context.get().getSender();
            if(player!=null){
                handlerAnim(player);
            }
        });
        context.get().setPacketHandled(true);
    }

    private void handlerAnim(LivingEntity entity) {
        if(entity instanceof Player player){
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            Handler.handledForKey(this.key,cap);
        }
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }
}
