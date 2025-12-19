package com.cristoh.cobblegachamachine.gacha;

import com.cristoh.cobblegachamachine.config.DefaultConfig;
import com.cristoh.cobblegachamachine.item.ModItems;
import com.cristoh.cobblegachamachine.sound.ModSounds;
import com.cristoh.cobblegachamachine.sound.PlayDispenseSoundPayload;
import com.cristoh.cobblegachamachine.sound.PlaySpinSoundPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

public class SpinEngine {
    private static final Item[] CAPSULE_TYPES = new Item[] {
            ModItems.NORMAL_CAPSULE,
            ModItems.RARE_CAPSULE,
            ModItems.ULTRA_RARE_CAPSULE,
            ModItems.LEGENDARY_CAPSULE
    };

    public static boolean prepareSpin(ServerPlayerEntity player, Session session) {
        if (session.spinning) return false;

        int poolSize = DefaultConfig.getPoolSize();
        session.pool = new ItemStack[poolSize];
        for (int i = 0; i < poolSize; i++) {
            session.pool[i] = pickWeightedCapsule(player);
        }

        // Shuffle the pool to randomize positions
        shufflePool(player, session.pool);

        session.windowStart = player.getWorld().random.nextInt(poolSize);
        int targetIndex = player.getWorld().random.nextInt(poolSize);
        int desiredWindowStart = (targetIndex - 2 + poolSize) % poolSize;
        int cycles = 3;//2 + player.getWorld().random.nextInt(3);
        session.totalShifts = cycles * poolSize + (desiredWindowStart - session.windowStart + poolSize) % poolSize;
        session.shiftsRemaining = session.totalShifts;

        session.updateDisplaySlots();
        session.spinning = true;
        session.cooldownTicks = 0;
        return true;
    }

    public static void tick(ServerPlayerEntity player, Session session) {
        if (session == null) return;

        if (session.spinning) {
            // If skip animation is enabled, complete instantly
            if (session.skipAnimation) {
                // Calculate final position directly
                int targetShifts = session.totalShifts;
                int poolSize = DefaultConfig.getPoolSize();
                session.windowStart = (session.windowStart + targetShifts) % poolSize;
                session.updateDisplaySlots();
                session.shiftsRemaining = 0;

                // Complete the spin immediately
                session.spinning = false;
                session.cooldownTicks = 40; // 2 seconds at 20 TPS
                ItemStack reward = session.displaySlots[2];
                if (session.gui != null) {
                    session.gui.updatePlayerSessionGui();
                }
                if (reward != null) {
                    if (player.getWorld().isClient()) {
                        player.getWorld().playSound(null, player.getBlockPos(),
                                ModSounds.GACHA_DISPENSE,
                                SoundCategory.PLAYERS, 1.0f, 1.0f);
                    } else {
                        ServerPlayNetworking.send(player, PlayDispenseSoundPayload.INSTANCE);
                    }

                    player.getInventory().offerOrDrop(reward.copy());
                }
                return;
            }

            // Normal animation logic
            session.tickAccumulator++;
            if (session.totalShifts <= 0) session.totalShifts = 1;

            double progress = 1.0 - (double) session.shiftsRemaining / (double) session.totalShifts;
            double ease = progress < 0.92
                    ? Math.pow(progress, 2)
                    : Math.pow(progress, 0.1);//1 - Math.pow(1 - progress, 4);

            final int minInterval = 1;
            final int maxInterval = 11;
            int interval = minInterval + (int) Math.round((maxInterval - minInterval) * ease);
            if (interval < 1) interval = 1;

            if (session.tickAccumulator >= interval) {
                int poolSize = DefaultConfig.getPoolSize();
                session.windowStart = (session.windowStart + 1) % poolSize;
                session.updateDisplaySlots();
                session.shiftsRemaining--;
                session.tickAccumulator = 0;
                if (session.gui != null && session.gui.isOpen()) {
                    if (player.getWorld().isClient()) {
                        player.getWorld().playSound(null, player.getBlockPos(),
                                ModSounds.GACHA_RUNNING,
                                SoundCategory.PLAYERS, 1.0f, 1.0f);
                    } else {
                        ServerPlayNetworking.send(player, PlaySpinSoundPayload.INSTANCE);
                    }
                }
            }

            if (session.shiftsRemaining <= 0) {
                session.spinning = false;
                session.cooldownTicks = 40; // 2 seconds at 20 TPS
                ItemStack reward = session.displaySlots[2];
                if (session.gui != null) {
                    session.gui.updatePlayerSessionGui();
                }
                if (reward != null) {
                    if (player.getWorld().isClient()) {
                        player.getWorld().playSound(null, player.getBlockPos(),
                                ModSounds.GACHA_DISPENSE,
                                SoundCategory.PLAYERS, 1.0f, 1.0f);
                    } else {
                        ServerPlayNetworking.send(player, PlayDispenseSoundPayload.INSTANCE);
                    }

                    player.getInventory().offerOrDrop(reward.copy());
                }
            }
        } else if (session.cooldownTicks > 0) {
            session.cooldownTicks--;
            if (session.cooldownTicks == 0) {
                session.clearDisplaySlots();
                if (session.gui != null) {
                    session.gui.updatePlayerSessionGui();
                }
            }
        }
    }

    private static void shufflePool(ServerPlayerEntity player, ItemStack[] pool) {
        // Fisher-Yates shuffle algorithm
        for (int i = pool.length - 1; i > 0; i--) {
            int j = player.getWorld().random.nextInt(i + 1);
            // Swap pool[i] and pool[j]
            ItemStack temp = pool[i];
            pool[i] = pool[j];
            pool[j] = temp;
        }
    }

    private static ItemStack pickWeightedCapsule(ServerPlayerEntity player) {
        double[] capsuleWeights = new double[] {
                DefaultConfig.getConfigForRarity("normal").capsuleWeight,
                DefaultConfig.getConfigForRarity("rare").capsuleWeight,
                DefaultConfig.getConfigForRarity("ultra_rare").capsuleWeight,
                DefaultConfig.getConfigForRarity("legendary").capsuleWeight
        };

        double total = 0;
        for (double w : capsuleWeights) total += w;
        double r = player.getWorld().random.nextDouble() * total;
        double acc = 0;
        for (int i = 0; i < CAPSULE_TYPES.length; i++) {
            acc += capsuleWeights[i];
            if (r < acc) {
                return CAPSULE_TYPES[i].getDefaultStack();
            }
        }
        return CAPSULE_TYPES[0].getDefaultStack();
    }
}

