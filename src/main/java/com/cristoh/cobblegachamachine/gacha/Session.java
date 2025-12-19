package com.cristoh.cobblegachamachine.gacha;

import com.cristoh.cobblegachamachine.config.DefaultConfig;
import com.cristoh.cobblegachamachine.ui.CustomSimpleGUI;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * Represents the per-player gacha machine session state.
 */
public class Session {
    public int coinCount = 0;
    public ItemStack[] displaySlots = new ItemStack[5];
    public boolean spinning = false;
    public Item coinItem;
    public CustomSimpleGUI gui;
    public BlockPos blockPos;

    public ItemStack[] pool;
    public int windowStart = 0;
    public int totalShifts = 0;
    public int shiftsRemaining = 0;
    public int tickAccumulator = 0;

    // Cooldown after spin completes before clearing UI (in ticks)
    public int cooldownTicks = 0;

    // Skip animation flag - if true, spin completes instantly
    public boolean skipAnimation = false;

    public void updateDisplaySlots() {
        if (pool == null) return;
        for (int i = 0; i < 5; i++) {
            displaySlots[i] = pool[(windowStart + i) % DefaultConfig.getPoolSize()].copy();
        }
    }

    public void clearDisplaySlots() {
        for (int i = 0; i < 5; i++) {
            displaySlots[i] = null;
        }
    }
}

