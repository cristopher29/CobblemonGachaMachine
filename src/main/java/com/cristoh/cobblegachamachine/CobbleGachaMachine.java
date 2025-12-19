package com.cristoh.cobblegachamachine;

import com.cristoh.cobblegachamachine.block.ModBlocks;
import com.cristoh.cobblegachamachine.block.entity.ModBlockEntities;
import com.cristoh.cobblegachamachine.command.GachaCommand;
import com.cristoh.cobblegachamachine.config.DefaultConfig;
import com.cristoh.cobblegachamachine.item.ModItemGroups;
import com.cristoh.cobblegachamachine.item.ModItems;
import com.cristoh.cobblegachamachine.sound.*;
import com.cristoh.cobblegachamachine.ui.GachaUIManager;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobbleGachaMachine implements ModInitializer {

	public static final String MOD_ID = "cobble-gacha-machine";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModLootTables.register();
        DefaultConfig.loadConfig(FabricLoader.getInstance().getConfigDir());
        ModSounds.register();
        ModItems.register();
        ModBlocks.register();
        ModItemGroups.register();
        ModBlockEntities.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            GachaCommand.register(dispatcher);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                GachaUIManager.tick(player);
            }
        });

        ServerPlayerEvents.LEAVE.register((player) -> {
            GachaUIManager.removeSession(player.getUuid());
        });

        PayloadTypeRegistry.playS2C().register(PlaySpinSoundPayload.ID, PlaySpinSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayCoinInsertSoundPayload.ID, PlayCoinInsertSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayDialSoundPayload.ID, PlayDialSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayDispenseSoundPayload.ID, PlayDispenseSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayRewardNormalSoundPayload.ID, PlayRewardNormalSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayRewardRareSoundPayload.ID, PlayRewardRareSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayRewardUltraRareSoundPayload.ID, PlayRewardUltraRareSoundPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayRewardLegendarySoundPayload.ID, PlayRewardLegendarySoundPayload.CODEC);
    }
}