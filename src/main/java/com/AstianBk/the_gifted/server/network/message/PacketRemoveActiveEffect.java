package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRemoveActiveEffect {
    private final int id;
    private final String powerId;

    public PacketRemoveActiveEffect(String powerId,int id) {
        this.powerId = powerId;
        this.id=id;
    }

    public PacketRemoveActiveEffect(FriendlyByteBuf buf) {
        this.powerId = buf.readUtf();
        this.id=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.powerId);
        buf.writeInt(this.id);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(this::sync);
        return true;
    }
    @OnlyIn(Dist.CLIENT)
    public void sync(){
        Minecraft mc = Minecraft.getInstance();
        PowerPlayerCapability cap = PowerPlayerCapability.get(mc.player);
        assert cap!=null;
        if(this.id==0){
            cap.getActiveEffectDuration().removeDuration(powerId);
        }else {
            cap.getLimbsPartRegeneration().regenerateLimbClient(powerId);
        }
    }
}
