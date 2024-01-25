package dev.sts15.fargos.init;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FargosConfig {
    private static final Map<String, ForgeConfigSpec.BooleanValue> CONFIG_MAP = new HashMap<>();
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

    public static boolean getConfigValue(String key) {
        ForgeConfigSpec.BooleanValue value = CONFIG_MAP.get(key);
        return value != null ? value.get() : false;
    }
    
    public static ForgeConfigSpec.BooleanValue getConfigValueObject(String key) {
        return CONFIG_MAP.get(key);
    }

    public static void saveConfig() {
        if (SERVER_CONFIG != null) {
            SERVER_CONFIG.save();
        }
    }
    
    public static Set<String> getAllConfigKeys() {
        return CONFIG_MAP.keySet();
    }
}
