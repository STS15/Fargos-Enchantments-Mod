package dev.sts15.fargos.network;

import dev.sts15.fargos.Fargos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class NetworkSetup {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("fargos", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    // Config is localized I think, but with the hashmap empty, it returns false for everything right away.
    // Send a packet on join and leave to add all the local config options to the hashmap

    public static void register() {
        int packetId = 0;
        // Register the packet from client to server
        INSTANCE.registerMessage(packetId++,
                ConfigUpdatePacket.class,
                ConfigUpdatePacket::encode,
                ConfigUpdatePacket::decode,
                ConfigUpdatePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        Fargos.LOGGER.debug("Registered Networking");
    }
}
