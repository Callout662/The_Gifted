package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.manager.LimbsPartRegeneration;
import com.AstianBk.the_gifted.server.manager.RegenerationInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class PacketSyncLimbRegeneration {
    private final Map<String, RegenerationInstance> regenerationLimbs;

    public static String readPowerID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    public static RegenerationInstance readRegenerationInstance(FriendlyByteBuf buffer) {
        int powerCooldown = buffer.readInt();
        int powerCooldownRemaining = buffer.readInt();
        return new RegenerationInstance(powerCooldown, powerCooldownRemaining);
    }

    public static void writePowerId(FriendlyByteBuf buf, String powerId) {
        buf.writeUtf(powerId);
    }

    public static void writeRegenerationInstance(FriendlyByteBuf buf, RegenerationInstance cooldownInstance) {
        buf.writeInt(cooldownInstance.getRegerationTimer());
        buf.writeInt(cooldownInstance.getRegerationTimerRemaining());
    }

    public PacketSyncLimbRegeneration(Map<String, RegenerationInstance> powerCooldowns) {
        this.regenerationLimbs = powerCooldowns;
    }

    public PacketSyncLimbRegeneration(FriendlyByteBuf buf) {
        this.regenerationLimbs = buf.readMap(PacketSyncLimbRegeneration::readPowerID, PacketSyncLimbRegeneration::readRegenerationInstance);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(regenerationLimbs, PacketSyncLimbRegeneration::writePowerId, PacketSyncLimbRegeneration::writeRegenerationInstance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Minecraft minecraft =Minecraft.getInstance();
            Player player= minecraft.player;
            PowerPlayerCapability cap = PowerPlayerCapability.get(player);
            var cooldowns = cap.getLimbsPartRegeneration();
            cooldowns.clearLimbs();
            this.regenerationLimbs.forEach((k, v) -> {
                cooldowns.addLoseLimb(k, v.getRegerationTimer(), v.getRegerationTimerRemaining());
            });
            cap.setLimbsPartRegeneration(new LimbsPartRegeneration(this.regenerationLimbs));
        });
        return true;
    }
}
