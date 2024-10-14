package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.network.Handler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKeySync {
    private final int key;
    private final int action;


    public PacketKeySync(FriendlyByteBuf buf) {
        this.key=buf.readInt();
        this.action=buf.readInt();
    }

    public PacketKeySync(int key,int action) {
        this.key = key;
        this.action = action;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.key);
        buf.writeInt(this.action);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            Player player = context.get().getSender();
            if(player!=null){
                sync(player);
            }
        });
        context.get().setPacketHandled(true);
    }


    private void sync(LivingEntity entity) {
        if(entity instanceof Player player){
            PowerPlayerCapability cap=PowerPlayerCapability.get(player);
            Handler.handledForKey(this.key,cap,action);
        }
    }

}
