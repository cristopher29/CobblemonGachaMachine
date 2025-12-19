package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import net.minecraft.client.sound.Sound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static SoundEvent GACHA_RUNNING;
    public static SoundEvent GACHA_DIAL;
    public static SoundEvent GACHA_DISPENSE;
    public static SoundEvent CAPSULE_OPEN;
    public static SoundEvent INSERT_COIN;
    public static SoundEvent REWARD_NORMAL;
    public static SoundEvent REWARD_RARE;
    public static SoundEvent REWARD_ULTRA_RARE;
    public static SoundEvent REWARD_LEGENDARY;

    private static SoundEvent registerSound(String name) {
        Identifier id = Identifier.of(CobbleGachaMachine.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        CobbleGachaMachine.LOGGER.info("Registering custom sounds for " + CobbleGachaMachine.MOD_ID);
        GACHA_RUNNING = registerSound("gacha_running");
        GACHA_DIAL = registerSound("gacha_dial");
        GACHA_DISPENSE = registerSound("gacha_dispense");
        CAPSULE_OPEN = registerSound("capsule_open");
        INSERT_COIN = registerSound("insert_coin");
        REWARD_NORMAL = registerSound("reward_normal");
        REWARD_RARE = registerSound("reward_rare");
        REWARD_ULTRA_RARE = registerSound("reward_ultra_rare");
        REWARD_LEGENDARY = registerSound("reward_legendary");
    }
}