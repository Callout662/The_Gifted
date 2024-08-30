package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.common.register.PWPower;
import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.network.Handler;
import com.AstianBk.the_gifted.server.powers.Power;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PacketSyncActivePowers implements Packet<PacketListener> {
    private final int id;
    private final Entity entity;
    private final List<Power> powers;

    public PacketSyncActivePowers(FriendlyByteBuf buf) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        this.entity = mc.level.getEntity(buf.readInt());
        this.id = buf.readInt();
        this.powers=buf.readList(e->{
            Power power= PWPower.getPowerForLocation(buf.readResourceLocation());
            power.level=buf.readInt();
            power.duration=buf.readInt();
            return power;
        });
    }

    public PacketSyncActivePowers(Entity entity, List<Power> powers) {
        this.entity = entity;
        this.id = entity.getId();
        this.powers=powers;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entity.getId());
        buf.writeInt(this.id);
        buf.writeCollection(this.powers,(e,power)->{
            e.writeResourceLocation(new ResourceLocation(TheGifted.MODID,power.name));
            e.writeInt(power.level);
            e.writeInt(power.duration);
        });
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() ->{
            handlerAnim();
        });
        context.get().setPacketHandled(true);
    }

    private void handlerAnim() {
        PowerPlayerCapability cap=PowerPlayerCapability.get((Player) entity);

    }

    @Override
    public void handle(PacketListener p_131342_) {

    }
}
