package dev.sts15.fargos.network;

import dev.sts15.fargos.Fargos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
import dev.sts15.fargos.init.FargosConfig;

public class ConfigUpdatePacket {
    private final String key;
    private final boolean value;

    public ConfigUpdatePacket(String key, boolean value) {
        this.key = key;
        this.value = value;
    }

    public static void encode(ConfigUpdatePacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.key);
        buf.writeBoolean(msg.value);
    }

    public static ConfigUpdatePacket decode(FriendlyByteBuf buf) {
        return new ConfigUpdatePacket(buf.readUtf(32767), buf.readBoolean());
    }

    public static void handle(ConfigUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Apply the configuration update
            FargosConfig.updateConfigValue(msg.key, msg.value, ctx.get().getSender());
            Fargos.LOGGER.debug("Trying to update config for player");
        });
        ctx.get().setPacketHandled(true);
    }
}
