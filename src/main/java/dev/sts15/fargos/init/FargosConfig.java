package dev.sts15.fargos.init;

import dev.sts15.fargos.network.ConfigUpdatePacket;
import dev.sts15.fargos.network.NetworkSetup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FargosConfig {
    private static final Map<String, ForgeConfigSpec.BooleanValue> CONFIG_MAP = new HashMap<>();
    private static final Map<UUID, Map<String, Boolean>> PLAYER_CONFIGS = new HashMap<>();
    public static ForgeConfigSpec SERVER_CONFIG;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        initConfigSection(SERVER_BUILDER, "force_of_overworld",
            "amethyst_enchantment", "redstone_enchantment", "diamond_enchantment",
            "emerald_enchantment", "lapis_enchantment", "gold_enchantment");
        initConfigSection(SERVER_BUILDER, "force_of_nature",
            "earth_enchantment", "fire_enchantment", "water_enchantment", "air_enchantment");
        initConfigSection(SERVER_BUILDER, "force_of_rejectors",
            "dragon_enchantment", "zombie_enchantment", "blaze_enchantment",
            "skeleton_enchantment", "creeper_enchantment", "ghast_enchantment",
            "vindicator_enchantment", "wither_enchantment");
        initConfigSection(SERVER_BUILDER, "force_of_mystic",
            "vampiric_enchantment", "enchanting_enchantment", "librarian_enchantment",
            "witch_enchantment", "shulker_enchantment", "undying_enchantment");
        initConfigSection(SERVER_BUILDER, "force_of_warrior",
            "battle_enchantment", "cactus_enchantment", "void_enchantment",
            "thorny_enchantment", "iron_golem_enchantment");
        initConfigSection(SERVER_BUILDER, "force_of_explorer",
            "architect_enchantment", "enderman_enchantment", "arctic_enchantment",
            "spectral_enchantment", "glowstone_enchantment");
        initConfigSection(SERVER_BUILDER, "soul_of_supersonic",
                "soul_of_supersonic-flight", "soul_of_supersonic-walking");
        initConfigSection(SERVER_BUILDER, "soul_of_flight_mastery",
                "soul_of_flight_mastery-flight");

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    private static void initConfigSection(ForgeConfigSpec.Builder builder, String section, String... keys) {
        builder.comment("Configuration for " + section).push(section);
        for (String key : keys) {
            addConfigValue(builder, key, true);
        }
        builder.pop();
    }

    private static void addConfigValue(ForgeConfigSpec.Builder builder, String key, boolean defaultValue) {
        ForgeConfigSpec.BooleanValue configValue = builder.define(key, defaultValue);
        CONFIG_MAP.put(key, configValue);
    }

    public static boolean getConfigValue(Player player, String key) {
        Map<String, Boolean> playerConfig = PLAYER_CONFIGS.getOrDefault(player.getUUID(), new HashMap<>());
        // Default value could be global default if not set for player
        return playerConfig.getOrDefault(key, false);
    }

    public static void setConfigValue(Player player, String key, boolean value) {
        PLAYER_CONFIGS.computeIfAbsent(player.getUUID(), k -> new HashMap<>()).put(key, value);
        // Consider saving these configurations persistently, e.g., to a file or database
    }

    public static ForgeConfigSpec.BooleanValue getConfigValueObject(String key) {
        return CONFIG_MAP.get(key);
    }

    public static void saveConfig() {
        if (SERVER_CONFIG != null) {
            SERVER_CONFIG.save();
        }
    }

    public static void sendConfigUpdateToServer(String key, boolean newValue) {
        ConfigUpdatePacket packet = new ConfigUpdatePacket(key, newValue);
        NetworkSetup.INSTANCE.sendToServer(packet);
    }

    public static void updateConfigValue(String key, boolean newValue, ServerPlayer player) {
            ForgeConfigSpec.BooleanValue configValue = CONFIG_MAP.get(key);
            if (configValue != null) {
                // Assuming you have a way to directly set the config value; if not, you might need a different approach
                // This is a simplistic approach; actual implementation might require updating the config file or in-memory values differently
                configValue.set(newValue);
                saveConfig(); // Persist changes if necessary
            }
    }
    
    public static Set<String> getAllConfigKeys() {
        return CONFIG_MAP.keySet();
    }
}
