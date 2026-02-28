package org.arcticquests.dev.oneironaut.oneironautt.network;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.arcticquests.dev.oneironaut.oneironautt.Oneironaut;

public final class OneironautNetwork {
    private OneironautNetwork() {}

    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named( ResourceLocation.fromNamespaceAndPath(Oneironaut.MODID, "main"))
            .networkProtocolVersion(() -> PROTOCOL)
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .simpleChannel();

    private static int id = 0;

    public static void register() {
        CHANNEL.messageBuilder(HoverliftAntiDesyncPacket.class, id++)
                .encoder(HoverliftAntiDesyncPacket::encode)
                .decoder(HoverliftAntiDesyncPacket::decode)
                .consumerMainThread(HoverliftAntiDesyncPacket::handle)
                .add();
    }
}