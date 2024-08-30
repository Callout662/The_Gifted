package com.AstianBk.the_gifted.server.network;

import com.AstianBk.the_gifted.common.TheGifted;
import com.AstianBk.the_gifted.server.network.message.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static SimpleChannel MOD_CHANNEL;

    public static void registerMessages() {
        int index = 0;
        SimpleChannel channel= NetworkRegistry.ChannelBuilder.named(
                        new ResourceLocation(TheGifted.MODID, "messages"))
                .networkProtocolVersion(()-> PROTOCOL_VERSION)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        MOD_CHANNEL=channel;

        channel.registerMessage(index++, PacketKeySync.class, PacketKeySync::write,
                PacketKeySync::new, PacketKeySync::handle);

        channel.registerMessage(index++, PacketHandlerAnimations.class, PacketHandlerAnimations::write,
                PacketHandlerAnimations::new, PacketHandlerAnimations::handle);
        channel.registerMessage(index++, PacketHandlerPowers.class, PacketHandlerPowers::write,
                PacketHandlerPowers::new, PacketHandlerPowers::handle);
        channel.registerMessage(index++, PacketSyncActivePowers.class, PacketSyncActivePowers::write,
                PacketSyncActivePowers::new, PacketSyncActivePowers::handle);
        channel.registerMessage(index++, PacketSyncData.class, PacketSyncData::write,
                PacketSyncData::new, PacketSyncData::handle);
        channel.registerMessage(index++,PacketSyncCooldown.class,PacketSyncCooldown::toBytes,
                PacketSyncCooldown::new,PacketSyncCooldown::handle);
        channel.messageBuilder(PacketSyncCooldown.class,index++)
                .encoder(PacketSyncCooldown::toBytes)
                .decoder(PacketSyncCooldown::new)
                .consumerNetworkThread(PacketSyncCooldown::handle).add();

        channel.messageBuilder(PacketSyncDurationEffect.class,index++)
                .encoder(PacketSyncDurationEffect::toBytes)
                .decoder(PacketSyncDurationEffect::new)
                .consumerNetworkThread(PacketSyncDurationEffect::handle).add();
        channel.messageBuilder(PacketRemoveActiveEffect.class,index++)
                .encoder(PacketRemoveActiveEffect::toBytes)
                .decoder(PacketRemoveActiveEffect::new)
                .consumerNetworkThread(PacketRemoveActiveEffect::handle).add();
        channel.messageBuilder(PacketActiveEffect.class,index++)
                .encoder(PacketActiveEffect::toBytes)
                .decoder(PacketActiveEffect::new)
                .consumerNetworkThread(PacketActiveEffect::handle).add();



    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        MOD_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),message);
    }

    public static <MSG> void sendToServer(MSG message) {
        MOD_CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToAllTracking(MSG message, LivingEntity entity) {
        MOD_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
