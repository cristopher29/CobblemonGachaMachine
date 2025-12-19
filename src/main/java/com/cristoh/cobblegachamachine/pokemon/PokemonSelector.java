package com.cristoh.cobblegachamachine.pokemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.BestSpawner;
import com.cobblemon.mod.common.api.spawning.CobblemonSpawnPools;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import com.cristoh.cobblegachamachine.item.custom.CapsuleRarity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;

import java.util.*;

public class PokemonSelector {

    private static final HashMap<String, HashMap<Species, Float>> buckets = new HashMap<>();
    private static final HashMap<UUID, LinkedList<Species>> playerRecentPokemon = new HashMap<>();
    private static final int MAX_CONSECUTIVE = 2;
    private static final float NEW_POKEMON_WEIGHT_MULTIPLIER = 1.5f;

    private static void initBuckets() {

        for (SpawnBucket bucket : BestSpawner.INSTANCE.getConfig().getBuckets()) {
            buckets.put(bucket.getName(), new HashMap<>());
        }

        var spawnPool = CobblemonSpawnPools.WORLD_SPAWN_POOL;
        for (SpawnDetail spawnDetail : spawnPool.getDetails()) {
            try {
                String bucketName = spawnDetail.getBucket().getName();
                if(!buckets.containsKey(bucketName)) continue;

                String pokemonSpeciesName = ((PokemonSpawnDetail) spawnDetail).getPokemon().getSpecies();
                if(pokemonSpeciesName == null) continue;

                Species species = PokemonSpecies.INSTANCE.getByName(pokemonSpeciesName);
                if(species == null) continue;

                if(species.getLabels().contains("legendary") || species.getLabels().contains("mythical")) {
                    continue;
                }

                // Obtener el peso del spawn (weight bajo = raro, weight alto = común)
                float weight = spawnDetail.getWeight();

                if (!buckets.get(bucketName).containsKey(species)) {
                    buckets.get(bucketName).put(species, weight);
                } else {
                    // Si ya existe, mantener el que tenga mayor weight (más común = más probabilidad)
                    Float existingWeight = buckets.get(bucketName).get(species);
                    if (weight > existingWeight) {
                        buckets.get(bucketName).put(species, weight);
                    }
                }

            } catch (Exception e) {
                continue;
            }
        }

        for(Map.Entry<String, HashMap<Species, Float>> bucket : buckets.entrySet()) {
            CobbleGachaMachine.LOGGER.info("Bucket: " + bucket.getKey() + " has " + bucket.getValue().size() + " pokemons");
        }
    }

    public static Pokemon selectPokemonByRarity(ServerWorld world, PlayerEntity player, CapsuleRarity rarity) {

        if(buckets.isEmpty()) initBuckets();

        List<Map.Entry<Species, Float>> eligibleSpecies = rarity.useCustomList()
                ? getSpeciesByCapsuleCustomList(rarity)
                : getSpeciesByCapsuleRarity(rarity);

        if (eligibleSpecies.isEmpty()) {
            CobbleGachaMachine.LOGGER.warn(Text.translatable("logger.cobble-gacha-machine.pokemon_specie_not_found", rarity.getName()).getString());
            return null;
        }

        Species selectedSpecies = selectSpeciesWithWeights(eligibleSpecies, player, world.random, rarity.useCustomList());
        int level = rarity.getMinLevel() + world.random.nextInt(rarity.getMaxLevel() - rarity.getMinLevel() + 1);
        Pokemon pokemon = selectedSpecies.create(level);

        int shinyChance = rarity.getShinyChance();

        if (rarity.isShinyEnabled() && world.random.nextInt(shinyChance) == 0) {
            pokemon.setShiny(true);
        }

        return pokemon;
    }

    private static List<Map.Entry<Species, Float>> getSpeciesByCapsuleCustomList(CapsuleRarity rarity) {
        return rarity.getCustomList().stream()
                .map(entry -> {
                    try {
                        Species species = PokemonSpecies.INSTANCE.getByName(entry.name);
                        if (species == null) {
                            CobbleGachaMachine.LOGGER.info(Text.translatable(
                                    "logger.cobble-gacha-machine.custom_list_pokemon_not_found",
                                    entry.name, rarity.getName()
                            ).getString());
                            return null;
                        }
                        return Map.entry(species, entry.weight);
                    } catch (Exception e) {
                        CobbleGachaMachine.LOGGER.error(Text.translatable(
                                "logger.cobble-gacha-machine.custom_list_pokemon_load_error",
                                entry.name, rarity.getName()
                        ).getString());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private static boolean hasPlayerCaughtSpecies(PlayerEntity player, Species species) {

        var partyStore = Cobblemon.INSTANCE.getStorage().getParty((ServerPlayerEntity) player);
        var pcStore = Cobblemon.INSTANCE.getStorage().getPC((ServerPlayerEntity) player);

        for (Pokemon pokemon : partyStore) {
            if (pokemon.getSpecies().getName().equals(species.getName())) {
                return true;
            }
        }

        for (Pokemon pokemon : pcStore) {
            if (pokemon.getSpecies().getName().equals(species.getName())) {
                return true;
            }
        }

        return false;
    }

    private static List<Map.Entry<Species, Float>> getSpeciesByLabel(CapsuleRarity rarity) {
        return PokemonSpecies.INSTANCE.getSpecies().stream()
                .filter(species -> {
                    for(String label : rarity.getAllowedBuckets()) {
                        if(species.getLabels().contains(label)) return true;
                    }
                    return false;
                })
                .map(species -> Map.entry(species, 1.0f))
                .toList();
    }

    private static List<Map.Entry<Species, Float>> getSpeciesByCapsuleRarity(CapsuleRarity rarity) {
        var speciesByBucket = Arrays.stream(rarity.getAllowedBuckets())
                .filter(buckets::containsKey)
                .flatMap(bucket -> buckets.get(bucket).entrySet().stream())
                .filter(entry -> {
                    float weight = entry.getValue();
                    return weight >= rarity.getMinWeight() && weight <= rarity.getMaxWeight();
                })
                .toList();
        if (speciesByBucket.isEmpty()) return getSpeciesByLabel(rarity);
        return speciesByBucket;
    }

    private static Species selectSpeciesWithWeights(List<Map.Entry<Species,Float>> weightedSpecies, PlayerEntity player, Random random, boolean useCustomList) {
        if (weightedSpecies.size() == 1) return weightedSpecies.getFirst().getKey();

        UUID playerId = player.getUuid();
        LinkedList<Species> recentPokemon = playerRecentPokemon.computeIfAbsent(playerId, k -> new LinkedList<>());

        // Verificar si los últimos 2 fueron el mismo Pokémon
        Species blockedSpecies = null;
        if (recentPokemon.size() >= MAX_CONSECUTIVE && !useCustomList) {
            Species first = recentPokemon.get(0);
            Species second = recentPokemon.get(1);

            // Si los últimos 2 son iguales, bloquear ese Pokémon
            if (first.equals(second)) {
                blockedSpecies = first;
            }
        }

        // Calcular pesos ajustados
        List<Float> adjustedWeights = new ArrayList<>();
        float totalWeight = 0f;

        for (Map.Entry<Species, Float> entry : weightedSpecies) {
            Species species = entry.getKey();
            float baseWeight = entry.getValue();

            // Si es el Pokémon bloqueado, peso = 0
            if (species.equals(blockedSpecies)) {
                adjustedWeights.add(0f);
                continue;
            }

            // Multiplicar peso si el jugador NO tiene este Pokémon
            float adjustedWeight = hasPlayerCaughtSpecies(player, species)
                    ? baseWeight
                    : baseWeight * NEW_POKEMON_WEIGHT_MULTIPLIER;

            adjustedWeights.add(adjustedWeight);
            totalWeight += adjustedWeight;
        }

        // Si todos tienen peso 0, reiniciar historial y volver a calcular
        if (totalWeight <= 0) {
            recentPokemon.clear();
            return selectSpeciesWithWeights(weightedSpecies, player, random, useCustomList);
        }

        // Selección ponderada
        double randomValue = random.nextDouble() * totalWeight;
        double currentWeight = 0.0;

        Species selectedSpecies = null;
        for (int i = 0; i < weightedSpecies.size(); i++) {
            currentWeight += adjustedWeights.get(i);
            if (randomValue <= currentWeight) {
                selectedSpecies = weightedSpecies.get(i).getKey();
                break;
            }
        }

        if (selectedSpecies == null) {
            selectedSpecies = weightedSpecies.getLast().getKey();
        }

        // Actualizar historial (máximo 2 elementos)
        recentPokemon.addFirst(selectedSpecies);
        if (recentPokemon.size() > MAX_CONSECUTIVE) {
            recentPokemon.removeLast();
        }

        return selectedSpecies;
    }
}