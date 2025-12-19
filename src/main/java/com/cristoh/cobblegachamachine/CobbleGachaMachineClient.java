package com.cristoh.cobblegachamachine;

import com.cristoh.cobblegachamachine.block.ModBlocks;
import com.cristoh.cobblegachamachine.sound.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.SoundEvents;

public class CobbleGachaMachineClient implements ClientModInitializer {
    private static boolean wasFocused = true;

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GACHA_BLOCK, RenderLayer.getCutout());

//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            boolean isFocused = client.isWindowFocused();
//
//            if (wasFocused && !isFocused) {
//                if (client.player != null && client.player.currentScreenHandler != null) {
//                    client.player.closeHandledScreen();
//                }
//            }
//
//            wasFocused = isFocused;
//        });


        ClientPlayNetworking.registerGlobalReceiver(PlaySpinSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player != null) {
                    context.client().player.playSound(ModSounds.GACHA_RUNNING, 1.0f, 1.0f);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayCoinInsertSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player != null) {
                    context.client().player.playSound(ModSounds.INSERT_COIN, 1.0f, 1.0f);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayDialSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player != null) {
                    context.client().player.playSound(ModSounds.GACHA_DIAL, 1.0f, 1.0f);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayDispenseSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player != null) {
                    context.client().player.playSound(ModSounds.GACHA_DISPENSE, 1.0f, 1.0f);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayRewardNormalSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player != null) {
                    context.client().player.playSound(ModSounds.REWARD_NORMAL, 1.0f, 1.0f);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayRewardRareSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player != null) {
                    context.client().player.playSound(ModSounds.REWARD_RARE, 1.0f, 1.0f);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayRewardUltraRareSoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player != null) {
                    context.client().player.playSound(ModSounds.REWARD_ULTRA_RARE, 1.0f, 1.0f);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PlayRewardLegendarySoundPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player != null) {
                    context.client().player.playSound(ModSounds.REWARD_LEGENDARY, 1.0f, 1.0f);
                }
            });
        });
    }
}
