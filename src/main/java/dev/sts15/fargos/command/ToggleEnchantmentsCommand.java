package dev.sts15.fargos.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.sts15.fargos.init.FargosConfig;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage.Player;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ToggleEnchantmentsCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
	    dispatcher.register(Commands.literal("fargos")
	        .then(Commands.literal("enchantment")
	            .then(Commands.argument("charm", StringArgumentType.string())
	                .suggests(ToggleEnchantmentsCommand::suggestEnchantmentKeys)
	                .then(Commands.argument("enabled", BoolArgumentType.bool())
	                    .executes(context -> {
	                        String key = StringArgumentType.getString(context, "charm");
							boolean value = BoolArgumentType.getBool(context, "enabled");
							setConfigValue(context, key, value);
	                        return 1;
	                    }))))
	        .then(Commands.literal("weapon")
	            .then(Commands.argument("key", StringArgumentType.string())
	                .suggests(ToggleEnchantmentsCommand::suggestWeaponKeys)
	                .then(Commands.argument("value", BoolArgumentType.bool())
	                    .executes(context -> {
	                        String key = StringArgumentType.getString(context, "key");
							boolean value = BoolArgumentType.getBool(context, "enabled");
							// Process weapon key and value
	                        context.getSource().sendSuccess(Component.literal("Weapon configuration updated."), false);
	                        return 1;
	                    }))))
	        
	    );
	}

	private static CompletableFuture<Suggestions> suggestWeaponKeys(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
	    // Add weapon key suggestions here
	    return builder.buildFuture();
	}


    private static CompletableFuture<Suggestions> suggestEnchantmentKeys(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        FargosConfig.getAllConfigKeys().forEach(builder::suggest);
        return builder.buildFuture();
    }

	private static void setConfigValue(CommandContext<CommandSourceStack> context, String key, boolean value) {
		ServerPlayer player = context.getSource().getPlayer(); // Get the player who executed the command
		FargosConfig.setConfigValue(player, key, value);
		context.getSource().sendSuccess(Component.literal("Configuration updated for " + key), true);
	}

}
