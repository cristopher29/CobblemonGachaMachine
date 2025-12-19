package com.cristoh.cobblegachamachine.item.custom;

import com.cristoh.cobblegachamachine.config.DefaultConfig;
import com.cristoh.cobblegachamachine.sound.ModSounds;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import java.util.List;

public enum CapsuleRarity {
    NORMAL(
            "normal",
            Formatting.RED,
            ModSounds.REWARD_NORMAL,
            ParticleTypes.ELECTRIC_SPARK,
            10
    ),
    RARE(
            "rare",
            Formatting.AQUA,
            ModSounds.REWARD_RARE,
            ParticleTypes.FISHING,
            35
    ),
    ULTRA_RARE(
            "ultra_rare",
            Formatting.GREEN,
            ModSounds.REWARD_ULTRA_RARE,
            ParticleTypes.HAPPY_VILLAGER,
            30
    ),
    LEGENDARY(
            "legendary",
            Formatting.LIGHT_PURPLE,
            ModSounds.REWARD_LEGENDARY,
            ParticleTypes.DRAGON_BREATH,
            30
    );

    private final String name;
    private final Formatting chatColor;
    private final SoundEvent sound;
    private final ParticleEffect particle;
    private final int particleCount;

    CapsuleRarity(String name, Formatting chatColor, SoundEvent sound, ParticleEffect particle, int particleCount) {
        this.name = name;
        this.chatColor = chatColor;
        this.sound = sound;
        this.particle = particle;
        this.particleCount = particleCount;
    }

    private DefaultConfig.CapsuleConfigData getConfig() {
        return DefaultConfig.getConfigForRarity(name);
    }

    public String getName() {
        return name;
    }

    public double getPokemonSpawnChance() {
        return getConfig().pokemonSpawnChance;
    }

    public int getMinLevel() {
        return getConfig().pokemonMinLevel;
    }

    public int getMaxLevel() {
        return getConfig().pokemonMaxLevel;
    }

    public String[] getAllowedBuckets() {
        return getConfig().pokemonPool;
    }

    public boolean isShinyEnabled() {
        return getConfig().shinyChance > 0;
    }

    public int getShinyChance() {
        return getConfig().shinyChance;
    }

    public double getCapsuleWeight() {
        return getConfig().capsuleWeight;
    }

    public float getMinWeight() {
        return getConfig().poolMinWeight;
    }

    public float getMaxWeight() {
        return getConfig().poolMaxWeight;
    }

    public boolean useCustomList() {
        return getConfig().customList != null && !getConfig().customList.isEmpty();
    }

    public List<DefaultConfig.CustomPokemonEntry> getCustomList() {
        return getConfig().customList;
    }

    public Formatting getChatColor() {
        return chatColor;
    }

    public SoundEvent getSound() {
        return sound;
    }

    public ParticleEffect getParticle() {
        return particle;
    }

    public int getParticleCount() {
        return particleCount;
    }
}