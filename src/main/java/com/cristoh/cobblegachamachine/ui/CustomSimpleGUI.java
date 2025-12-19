package com.cristoh.cobblegachamachine.ui;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import com.cristoh.cobblegachamachine.ModTags;
import com.cristoh.cobblegachamachine.config.DefaultConfig;
import com.cristoh.cobblegachamachine.item.ModItems;
import com.cristoh.cobblegachamachine.sound.ModSounds;
import com.cristoh.cobblegachamachine.gacha.Session;
import com.cristoh.cobblegachamachine.sound.PlayCoinInsertSoundPayload;
import com.cristoh.cobblegachamachine.sound.PlayDialSoundPayload;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CustomSimpleGUI extends SimpleGui {

    private Session currentSession;
    private final String NEGATIVE_SPACES = "\uF808";
    private final String GUI_BACKGROUND_EMPTY = "\uE000";
    private final String GUI_BACKGROUND_WITH_COIN = "\uE001";
    private final Identifier CUSTOM_FONT = Identifier.of(CobbleGachaMachine.MOD_ID, "gacha");

    public CustomSimpleGUI(ScreenHandlerType<?> type, ServerPlayerEntity player, boolean manipulatePlayerSlots, BlockPos pos) {
        super(type, player, manipulatePlayerSlots);
        this.currentSession = GachaUIManager.getOrCreateSession(player, pos);
        this.currentSession.gui = this;
        updatePlayerSessionGui();
        //this.setAutoUpdate(true);
    }

    public void updatePlayerSessionGui() {

        // Background change
        if (this.currentSession.coinCount > 0) {
            MutableText title = Text.literal("§f"+ NEGATIVE_SPACES + GUI_BACKGROUND_WITH_COIN)
                    .setStyle(Style.EMPTY.withFont(CUSTOM_FONT));
            setTitle(title);
        } else {
            MutableText title = Text.literal("§f"+ NEGATIVE_SPACES + GUI_BACKGROUND_EMPTY)
                    .setStyle(Style.EMPTY.withFont(CUSTOM_FONT));
            setTitle(title);
        }

        // Coin button
        setSlot(8, createCoinStack());

        // Display capsules
        for (int i = 0; i < 5; i++) {
            GuiElementBuilder displayStack = createDisplayStack(i);
            setSlot(11 + i, displayStack);
        }

        // Spin button
        setSlot(9, createSpinStack());

        // Skip animation button
        if (DefaultConfig.isSkipAnimationButtonEnabled()) {
            setSlot(26, createSkipAnimationStack());
        }
    }

    private GuiElementBuilder createCoinStack() {
        GuiElementBuilder stack;

        if (currentSession.coinCount > 0) {
            stack = new GuiElementBuilder(this.currentSession.coinItem, Math.min(this.currentSession.coinCount, 64));
            stack.setName(Text.translatable("text.cobble-gacha-machine.coins_counter").formatted(Formatting.GREEN)
                            .append(Text.literal(""+this.currentSession.coinCount).formatted(Formatting.WHITE))
            );
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.click_insert_coin").formatted(Formatting.YELLOW));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.to_add_more_coins").formatted(Formatting.GRAY));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.from_inventory").formatted(Formatting.GRAY));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.click_remove_coins").formatted(Formatting.YELLOW));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.to_remove_coin").formatted(Formatting.GRAY));
        } else {
            currentSession.coinItem = null;
            stack = new GuiElementBuilder(ModItems.INVISIBLE_TOOLTIP);
            stack.setName(Text.translatable("text.cobble-gacha-machine.no_coins").formatted(Formatting.RED).formatted(Formatting.BOLD));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.click_insert_coin").formatted(Formatting.YELLOW));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.to_insert_coin").formatted(Formatting.GRAY));
        }
        stack.addLoreLine(Text.literal("").formatted(Formatting.GRAY));
        var coinItems = player.getServer().getRegistryManager()
                .get(net.minecraft.registry.RegistryKeys.ITEM)
                .getEntryList(ModTags.Items.COIN_ITEMS)
                .orElse(null);

        if (coinItems != null) {
            coinItems.stream()
                    .limit(5)
                    .forEach(entry -> {
                        String itemName = entry.value().getName().getString();
                        stack.addLoreLine(Text.literal("  • " + itemName).formatted(Formatting.YELLOW));
                    });
        }

        stack.setCallback((index, type, action) -> {
            if (type == ClickType.MOUSE_LEFT) {
                if (removeCoinFromPlayer(player, 1)) {
                    currentSession.coinCount++;
                    if(player.getWorld().isClient()) {
                        player.getWorld().playSound(null, player.getBlockPos(),
                                ModSounds.INSERT_COIN,
                                SoundCategory.PLAYERS, 1.0f, 1.0f);
                    } else {
                        ServerPlayNetworking.send(player, PlayCoinInsertSoundPayload.INSTANCE);
                    }
                    updatePlayerSessionGui();
                }
            }
            else if (type == ClickType.MOUSE_RIGHT || type == ClickType.MOUSE_LEFT_SHIFT && currentSession.coinCount > 0) {
                if (currentSession.coinItem != null) {
                    ItemStack coinStack = new ItemStack(currentSession.coinItem, 1);
                    player.getInventory().offerOrDrop(coinStack);
                    currentSession.coinCount--;
                    updatePlayerSessionGui();
                }
            }
        });

        return stack;
    }

    private GuiElementBuilder createDisplayStack(int index) {
        GuiElementBuilder stack;

        if (currentSession.displaySlots[index] != null) {
            stack = new GuiElementBuilder(currentSession.displaySlots[index].copy().getItem());
            if (index == 2 && currentSession.spinning) {
                stack.glow();
            }
        } else {
            stack = new GuiElementBuilder(Items.AIR);
        }

        return stack;
    }

    private GuiElementBuilder createSpinStack() {
        GuiElementBuilder stack = new GuiElementBuilder(ModItems.INVISIBLE_TOOLTIP);

        if (currentSession.spinning) {
            stack.setName(Text.translatable("text.cobble-gacha-machine.spinning").formatted(Formatting.RED, Formatting.BOLD));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.please_wait").formatted(Formatting.GRAY));
        } else if (currentSession.coinCount >= DefaultConfig.getCoinsToSpin()) {
            int coinsNeeded = DefaultConfig.getCoinsToSpin();
            stack.setName(Text.translatable("text.cobble-gacha-machine.spin").formatted(Formatting.GREEN, Formatting.BOLD));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.cost").formatted(Formatting.GRAY)
                    .append(Text.translatable("text.cobble-gacha-machine.x_coin", coinsNeeded).formatted(Formatting.GOLD)));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.click_spin").formatted(Formatting.GREEN));
        } else {
            int coinsNeeded = DefaultConfig.getCoinsToSpin();
            stack.setName(Text.translatable("text.cobble-gacha-machine.need_coins_title").formatted(Formatting.RED, Formatting.BOLD));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable(coinsNeeded == 1 ? "text.cobble-gacha-machine.need_coin" : "text.cobble-gacha-machine.need_coins", coinsNeeded).formatted(Formatting.RED));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.insert_coin_to_spin").formatted(Formatting.GRAY));
        }

        stack.setCallback((index, type, action) -> {
            if (type == ClickType.MOUSE_LEFT) {
                if (currentSession.spinning) {
                    return;
                }

                int coinsNeeded = DefaultConfig.getCoinsToSpin();
                if (currentSession.coinCount < coinsNeeded) {
                    return;
                }

                boolean started = GachaUIManager.prepareSpin(player, currentSession);
                if (started) {
                    currentSession.coinCount -= coinsNeeded;
                    if(player.getWorld().isClient()) {
                        player.getWorld().playSound(null, player.getBlockPos(),
                                ModSounds.GACHA_DIAL,
                                SoundCategory.PLAYERS, 1.0f, 1.0f);
                    }  else {
                        ServerPlayNetworking.send(player, PlayDialSoundPayload.INSTANCE);
                    }
                    updatePlayerSessionGui();
                }
            }
        });

        return stack;
    }

    private GuiElementBuilder createSkipAnimationStack() {
        GuiElementBuilder stack;

        if (currentSession.skipAnimation) {
            stack = new GuiElementBuilder(Items.LIME_DYE);
            stack.setName(Text.translatable("text.cobble-gacha-machine.skip_animation").formatted(Formatting.GREEN, Formatting.BOLD));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.skip_animation_enabled").formatted(Formatting.GREEN));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.click_to_disable").formatted(Formatting.YELLOW));
        } else {
            stack = new GuiElementBuilder(Items.GRAY_DYE);
            stack.setName(Text.translatable("text.cobble-gacha-machine.skip_animation").formatted(Formatting.GRAY, Formatting.BOLD));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.skip_animation_disabled").formatted(Formatting.GRAY));
            stack.addLoreLine(Text.literal(""));
            stack.addLoreLine(Text.translatable("text.cobble-gacha-machine.click_to_enable").formatted(Formatting.YELLOW));
        }

        stack.setCallback((index, type, action) -> {
            if (type == ClickType.MOUSE_LEFT) {
                currentSession.skipAnimation = !currentSession.skipAnimation;
                updatePlayerSessionGui();
            }
        });

        return stack;
    }

    private boolean removeCoinFromPlayer(ServerPlayerEntity player, int amount) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().size() && remaining > 0; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if ((currentSession.coinItem == null && stack.isIn(ModTags.Items.COIN_ITEMS)) || (currentSession.coinItem == stack.getItem())) {
                int toRemove = Math.min(remaining, stack.getCount());
                currentSession.coinItem = stack.copy().getItem();
                stack.decrement(toRemove);
                remaining -= toRemove;
            }
        }
        return remaining == 0;
    }

    @Override
    public void onTick() {
        super.onTick();
        if(currentSession.spinning) {
            for (int i = 0; i < 5; i++) {
                GuiElementBuilder displayStack = createDisplayStack(i);
                setSlot(11 + i, displayStack);
            }
        }
    }
}
