package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.manager.DurationInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketActiveEffect {
    private final DurationInstance recastInstance;

    public PacketActiveEffect(DurationInstance recastInstance) {
        this.recastInstance = recastInstance;
    }

    public PacketActiveEffect(FriendlyByteBuf buf) {
        recastInstance = new DurationInstance();
        recastInstance.readFromBuffer(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        if (recastInstance != null) {
            recastInstance.writeToBuffer(buf);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            PowerPlayerCapability cap = PowerPlayerCapability.get(mc.player);
            assert cap!=null;

            cap.getActiveEffectDuration().forceAddDuration(recastInstance);
        });
        return true;
    }
}
