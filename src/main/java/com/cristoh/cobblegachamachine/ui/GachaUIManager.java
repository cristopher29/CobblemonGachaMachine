package com.cristoh.cobblegachamachine.ui;

import com.cristoh.cobblegachamachine.gacha.RewardMessages;
import com.cristoh.cobblegachamachine.gacha.Session;
import com.cristoh.cobblegachamachine.gacha.SessionManager;
import com.cristoh.cobblegachamachine.gacha.SpinEngine;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class GachaUIManager {

    public static Session getOrCreateSession(ServerPlayerEntity player, BlockPos pos) {
        Session session = SessionManager.getOrCreate(player.getUuid());
        session.blockPos = pos;
        return session;
    }

    public static void removeSession(UUID playerId) {
        SessionManager.remove(playerId);
    }

    public static boolean prepareSpin(ServerPlayerEntity player, Session session) {
        return SpinEngine.prepareSpin(player, session);
    }

    public static void tick(ServerPlayerEntity player) {
        Session session = SessionManager.get(player.getUuid());
        if (session == null) return;

        boolean wasSpinning = session.spinning;
        SpinEngine.tick(player, session);

        if (wasSpinning && !session.spinning) {
            ItemStack reward = session.displaySlots[2];
            if (reward != null) {
                String rewardName = reward.getName().getString();
                Text message = RewardMessages.buildRewardMessage(reward, rewardName);
                player.sendMessage(message, false);
            }
        }
    }
}
