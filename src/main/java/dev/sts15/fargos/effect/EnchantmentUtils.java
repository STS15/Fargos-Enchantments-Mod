package dev.sts15.fargos.effect;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.common.ForgeMod;
import java.util.UUID;

public class EnchantmentUtils {

    // Removes the reach modifier from the player
    public static void removePlayerReach(UUID id, Player player) {
        AttributeInstance attr = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
        attr.removeModifier(id);
    }

    // Sets the player's reach distance
    public static void setPlayerReach(UUID id, Player player, double reachBoost) {
        removePlayerReach(id, player); // Remove existing modifier
        AttributeInstance attr = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
        AttributeModifier modifier = new AttributeModifier(id, "ArchitectEnchantment", reachBoost, AttributeModifier.Operation.ADDITION);
        attr.addPermanentModifier(modifier);
    }
}

