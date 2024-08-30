package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.AnimationPlayerCapability;
import com.AstianBk.the_gifted.server.network.Handler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHandlerAnimations implements Packet<PacketListener> {
    private final int id;
    private final Entity entity;

    public PacketHandlerAnimations(FriendlyByteBuf buf) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        this.entity = mc.level.getEntity(buf.readInt());
        this.id = buf.readInt();
    }

    public PacketHandlerAnimations(Entity entity, int id) {
        this.entity = entity;
        this.id = id;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entity.getId());
        buf.writeInt(this.id);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            handlerAnim();
        });
        context.get().setPacketHandled(true);
    }

    private void handlerAnim() {
        AnimationPlayerCapability cap=AnimationPlayerCapability.get((Player) entity);
        if(cap!=null){
            Handler.handlerManagerAnimation(this.id,cap);
        }
    }

    @Override
    public void handle(PacketListener p_131342_) {

    }
}
