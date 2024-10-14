package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.powers.Power;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncActivePowers {
    private final int id;
    private final Entity entity;
    private final String powerId;

    public PacketSyncActivePowers(FriendlyByteBuf buf) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        this.entity = mc.level.getEntity(buf.readInt());
        this.id = buf.readInt();
        this.powerId =buf.readUtf();
    }

    public PacketSyncActivePowers(Entity entity,int id, Power power) {
        this.entity = entity;
        this.id = id;
        this.powerId=power.name;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entity.getId());
        buf.writeInt(this.id);
        buf.writeUtf(this.powerId);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(this::handlerAnim);
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void handlerAnim() {
        PowerPlayerCapability cap=PowerPlayerCapability.get((Player) entity);
        switch (this.id){
            case 0->{
                cap.startCasting((Player) entity);
            }
            case 1->{
                cap.stopCasting(cap.getHotBarPower().getForName(this.powerId), (Player) entity);
            }
        }
    }

}
