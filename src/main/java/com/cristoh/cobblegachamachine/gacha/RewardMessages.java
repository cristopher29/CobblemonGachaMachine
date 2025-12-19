package com.cristoh.cobblegachamachine.gacha;

import com.cristoh.cobblegachamachine.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RewardMessages {
    public static Text buildRewardMessage(ItemStack reward, String name) {
        Item item = reward.getItem();
        MutableText youWonText = Text.translatable("text.cobble-gacha-machine.you_won");
        if (item == ModItems.LEGENDARY_CAPSULE) {
            return youWonText.formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD).append(Text.literal(name).formatted(Formatting.WHITE));
        } else if (item == ModItems.ULTRA_RARE_CAPSULE) {
            return youWonText.formatted(Formatting.GREEN, Formatting.BOLD).append(Text.literal(name).formatted(Formatting.WHITE));
        } else if (item == ModItems.RARE_CAPSULE) {
            return youWonText.formatted(Formatting.BLUE, Formatting.BOLD).append(Text.literal(name).formatted(Formatting.WHITE));
        } else {
            return youWonText.formatted(Formatting.RED, Formatting.BOLD).append(Text.literal(name).formatted(Formatting.WHITE));
        }
    }
}

