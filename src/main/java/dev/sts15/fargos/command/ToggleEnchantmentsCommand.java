package dev.sts15.fargos.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import dev.sts15.fargos.init.FargosConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ToggleEnchantmentsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fargos")
                .then(Commands.argument("key", StringArgumentType.string())
                        .suggests(ToggleEnchantmentsCommand::suggestKeys)
                        .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> {
                            String key = StringArgumentType.getString(context, "key");
                            boolean value = BoolArgumentType.getBool(context, "value");
                            setConfigValue(key, value);
                            if (key.contains("soul_of_")) {
                            	context.getSource().sendSuccess(Component.literal("Soul updated. Please re-equip for changes to take effect"), true);
                            } else {
                            	context.getSource().sendSuccess(Component.literal("Force updated."), true);
                            }
                            
                            return 1;
                        }))));
    }

    private static CompletableFuture<Suggestions> suggestKeys(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        FargosConfig.getAllConfigKeys().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static void setConfigValue(String key, boolean value) {
        ForgeConfigSpec.BooleanValue configValue = FargosConfig.getConfigValueObject(key);
        if (configValue != null) {
            configValue.set(value);
            FargosConfig.saveConfig();
        }
    }
}
