package com.cristoh.cobblegachamachine.command;

import com.cristoh.cobblegachamachine.config.DefaultConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class GachaCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gacha")
                .then(CommandManager.literal("reload")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(GachaCommand::reloadConfig)));
    }

    private static int reloadConfig(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        
        try {
            DefaultConfig.loadConfig(FabricLoader.getInstance().getConfigDir());
            source.sendFeedback(() -> Text.translatable("text.cobble-gacha-machine.config_reloaded_success")
                    .formatted(Formatting.GREEN), true);
            return 1;
        } catch (Exception e) {
            source.sendFeedback(() -> Text.translatable("text.cobble-gacha-machine.config_reloaded_failed", e.getMessage())
                    .formatted(Formatting.RED), false);
            return 0;
        }
    }

}
