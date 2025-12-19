package com.cristoh.cobblegachamachine.config;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultConfig {
    private static final Logger LOGGER = CobbleGachaMachine.LOGGER;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "default.json";

    private static Map<String, CapsuleConfigData> capsuleConfigs = new HashMap<>();
    private static int poolSize = 15;
    private static boolean megashowdownItems = true;
    private static int coinsToSpin = 3;
    private static boolean skipAnimationButton = true;

    /**
     * Config format
     *
     * {
     *      "skipAnimationButton": true,
     *      "megashowdownItems": true,
     *      "poolSize": 15,
     *      "coinsToSpin": 2,
     *      "capsules": {
     *          "normal": {
     *              "capsuleWeight": 75.0,
     *              "pokemonSpawnChance": 0.15,
     *              "shinyChance": 2048
     *              "pokemonMinLevel": 5,
     *              "pokemonMaxLevel": 20,
     *              "pokemonPool": ["common"],
     *              "poolMinWeight": 0.15,
     *              "poolMaxWeight": 100.0,
     *              "customList": [
     *                  {
     *                      "name": "arceus",
     *                      "weight": 1.0
     *                  },
     *                  {
     *                      "name": "pikachu",
     *                      "weight": 1.0
     *                  },
     *              ]
     *          },
     *          "rare": {
     *              "capsuleWeight": 25.0,
     *              "pokemonSpawnChance": 0.30,
     *              ...
     *          }
     *      }
     * }
     */

    public static void loadConfig(Path configDir) {
        Path configPath = configDir.resolve("cobble-gacha-machine").resolve(CONFIG_FILE_NAME);

        try {
            if (!Files.exists(configPath)) {
                LOGGER.info("Config file not found, creating default config at: {}", configPath);
                createDefaultConfig(configPath);
            }

            String json = Files.readString(configPath);
            ConfigRoot config = GSON.fromJson(json, ConfigRoot.class);

            if (config != null && config.capsules != null) {
                capsuleConfigs = config.capsules;
                if (config.poolSize > 0) {
                    poolSize = config.poolSize;
                }
                if (config.coinsToSpin > 0) {
                    coinsToSpin = config.coinsToSpin;
                }
                megashowdownItems = config.megashowdownItems;
                skipAnimationButton = config.skipAnimationButton;
                LOGGER.info("Loaded configuration for {} capsule types", capsuleConfigs.size());
            } else {
                LOGGER.warn("Invalid config format, using defaults");
                loadDefaults();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read config file: {}", e.getMessage());
            loadDefaults();
        } catch (JsonSyntaxException e) {
            LOGGER.error("Invalid JSON syntax in config file: {}", e.getMessage());
            loadDefaults();
        }
    }

    private static void createDefaultConfig(Path configPath) throws IOException {
        Files.createDirectories(configPath.getParent());
        loadDefaults();

        ConfigRoot defaultConfig = new ConfigRoot();
        defaultConfig.capsules = capsuleConfigs;
        defaultConfig.megashowdownItems = megashowdownItems;
        defaultConfig.coinsToSpin = coinsToSpin;
        defaultConfig.skipAnimationButton = skipAnimationButton;

        String json = GSON.toJson(defaultConfig);
        Files.writeString(configPath, json);
        LOGGER.info("Created default config file at: {}", configPath);
    }

    private static void loadDefaults() {
        capsuleConfigs = new HashMap<>();

        // Normal capsule
        CapsuleConfigData normal = new CapsuleConfigData();
        normal.capsuleWeight = 75.0;
        normal.pokemonSpawnChance = 0.15;
        normal.shinyChance = 2048;
        normal.pokemonMinLevel = 5;
        normal.pokemonMaxLevel = 20;
        normal.pokemonPool = new String[]{"common"};
        normal.poolMinWeight = 0.15f;
        normal.poolMaxWeight = 100.0f;
        normal.customList = new ArrayList<>();
        capsuleConfigs.put("normal", normal);

        // Rare capsule
        CapsuleConfigData rare = new CapsuleConfigData();
        rare.capsuleWeight = 17.0;
        rare.pokemonSpawnChance = 0.25;
        rare.shinyChance = 1024;
        rare.pokemonMinLevel = 15;
        rare.pokemonMaxLevel = 35;
        rare.pokemonPool = new String[]{"uncommon", "rare"};
        rare.poolMinWeight = 0.0032f;
        rare.poolMaxWeight = 100.0f;
        rare.customList = new ArrayList<>();
        capsuleConfigs.put("rare", rare);

        // Ultra Rare capsule
        CapsuleConfigData ultraRare = new CapsuleConfigData();
        ultraRare.capsuleWeight = 6.0;
        ultraRare.pokemonSpawnChance = 0.5;
        ultraRare.shinyChance = 512;
        ultraRare.pokemonMinLevel = 30;
        ultraRare.pokemonMaxLevel = 50;
        ultraRare.pokemonPool = new String[]{"ultra-rare"};
        ultraRare.poolMinWeight = 0.002f;
        ultraRare.poolMaxWeight = 10.0f;
        ultraRare.customList = new ArrayList<>();
        capsuleConfigs.put("ultra_rare", ultraRare);

        // Legendary capsule
        CapsuleConfigData legendary = new CapsuleConfigData();
        legendary.capsuleWeight = 2.0;
        legendary.pokemonSpawnChance = 1.0;
        legendary.shinyChance = 256;
        legendary.pokemonMinLevel = 50;
        legendary.pokemonMaxLevel = 100;
        legendary.pokemonPool = new String[]{"legendary", "mythical"};
        legendary.poolMinWeight = 0.0f;
        legendary.poolMaxWeight = 0.0f;
        legendary.customList = new ArrayList<>();
        capsuleConfigs.put("legendary", legendary);
    }

    public static CapsuleConfigData getConfigForRarity(String rarityName) {
        return capsuleConfigs.get(rarityName);
    }

    public static int getPoolSize() {
        return poolSize;
    }

    public static boolean isMegashowdownItemsEnabled() {
        return megashowdownItems;
    }

    public static int getCoinsToSpin() {
        return coinsToSpin;
    }

    public static boolean isSkipAnimationButtonEnabled() {
        return skipAnimationButton;
    }

    public static class ConfigRoot {
        public int poolSize = 15;
        public boolean megashowdownItems = true;
        public int coinsToSpin = 2;
        public boolean skipAnimationButton = true;
        public Map<String, CapsuleConfigData> capsules;
    }

    public static class CapsuleConfigData {
        public double capsuleWeight;
        public double pokemonSpawnChance;
        public int shinyChance;
        public int pokemonMinLevel;
        public int pokemonMaxLevel;
        public String[] pokemonPool;
        public float poolMinWeight;
        public float poolMaxWeight;
        public List<CustomPokemonEntry> customList;
    }

    public static class CustomPokemonEntry {
        public String name;
        public float weight;

        public CustomPokemonEntry() {
        }

        public CustomPokemonEntry(String name, float weight) {
            this.name = name;
            this.weight = weight;
        }
    }
}