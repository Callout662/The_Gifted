package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRemoveActiveEffect {
    private final String powerId;

    public PacketRemoveActiveEffect(String powerId) {
        this.powerId = powerId;
    }

    public PacketRemoveActiveEffect(FriendlyByteBuf buf) {
        powerId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(powerId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            PowerPlayerCapability cap = PowerPlayerCapability.get(mc.player);
            assert cap!=null;
            cap.getActiveEffectDuration().removeDuration(powerId);
        });
        return true;
    }
}
