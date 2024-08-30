package com.AstianBk.the_gifted.server.network.message;

import com.AstianBk.the_gifted.server.capability.PowerPlayerCapability;
import com.AstianBk.the_gifted.server.manager.CooldownInstance;
import com.AstianBk.the_gifted.server.powers.Power;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class PacketSyncCooldown {
    private final Map<String, CooldownInstance> powerCooldowns;

    public static String readPowerID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    public static CooldownInstance readCoolDownInstance(FriendlyByteBuf buffer) {
        int powerCooldown = buffer.readInt();
        int powerCooldownRemaining = buffer.readInt();
        return new CooldownInstance(powerCooldown, powerCooldownRemaining);
    }

    public static void writePowerId(FriendlyByteBuf buf, String powerId) {
        buf.writeUtf(powerId);
    }

    public static void writeCoolDownInstance(FriendlyByteBuf buf, CooldownInstance cooldownInstance) {
        buf.writeInt(cooldownInstance.getPowerCooldown());
        buf.writeInt(cooldownInstance.getCooldownRemaining());
    }

    public PacketSyncCooldown(Map<String, CooldownInstance> powerCooldowns) {
        this.powerCooldowns = powerCooldowns;
    }

    public PacketSyncCooldown(FriendlyByteBuf buf) {
        this.powerCooldowns = buf.readMap(PacketSyncCooldown::readPowerID,PacketSyncCooldown::readCoolDownInstance);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(powerCooldowns, PacketSyncCooldown::writePowerId, PacketSyncCooldown::writeCoolDownInstance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Minecraft minecraft =Minecraft.getInstance();
            Player player= minecraft.player;
            PowerPlayerCapability cap = PowerPlayerCapability.get(player);
            var cooldowns = cap.getCooldowns();
            cooldowns.clearCooldowns();
            this.powerCooldowns.forEach((k, v) -> {
                //irons_powerbooks.LOGGER.debug("ClientboundSyncCooldowns {} {} {}", k, v.getPowerCooldown(), v.getCooldownRemaining());
                cooldowns.addCooldown(k, v.getPowerCooldown(), v.getCooldownRemaining());
            });
        });
        return true;
    }
}
