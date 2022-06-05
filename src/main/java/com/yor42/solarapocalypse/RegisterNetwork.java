package com.yor42.solarapocalypse;

import com.yor42.solarapocalypse.utils.SyncStagePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class RegisterNetwork {
    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(Constants.MODID, "network");
    public static final String NETWORK_VERSION = new ResourceLocation(Constants.MODID, "1").toString();
    public static SimpleChannel getChannel(){
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .simpleChannel();

        channel.messageBuilder(SyncStagePacket.class, 1)
                .decoder(SyncStagePacket::decode)
                .encoder(SyncStagePacket::encode)
                .consumer(SyncStagePacket::handle)
                .add();

        return channel;
    }
}
