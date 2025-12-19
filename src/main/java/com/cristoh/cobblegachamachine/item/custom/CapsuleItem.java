package com.cristoh.cobblegachamachine.item.custom;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import com.cristoh.cobblegachamachine.ModLootTables;
import com.cristoh.cobblegachamachine.pokemon.PokemonHandler;
import com.cristoh.cobblegachamachine.pokemon.PokemonSelector;
import com.cristoh.cobblegachamachine.sound.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class CapsuleItem extends Item {
    private final CapsuleRarity rarity;
    private Identifier lootTableId;

    public CapsuleItem(Item.Settings settings, CapsuleRarity rarity) {
        super(settings);
        this.rarity = rarity;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.consume(user.getStackInHand(hand));
        }

        ItemStack capsule = user.getStackInHand(hand);
        boolean pokemonSpawned = spawnPokemonIfNeeded((ServerWorld)world, user);

        if (pokemonSpawned) {
            playEffects(world, user);
        } else {
            List<ItemStack> drops = getDrops((ServerWorld)world, user.getPos(), user);

            for (ItemStack stack : drops) {
                user.getInventory().offerOrDrop(stack);
            }

            world.playSound(null, user.getBlockPos(), ModSounds.CAPSULE_OPEN,
                    SoundCategory.PLAYERS, 1.0f, 1.0f);
        }

        ItemStack resultStack = capsule.copy();
        resultStack.decrement(1);

        return TypedActionResult.success(resultStack);
    }

    private List<ItemStack> getDrops(ServerWorld world, Vec3d pos, Entity user) {
        if (getLootTableId() == null) {
            return Collections.emptyList();
        }

        LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder(world)
                .add(LootContextParameters.ORIGIN, pos)
                .addOptional(LootContextParameters.THIS_ENTITY, user)
                .build(ModLootTables.CAPSULE);

        LootTable lootTable = world.getServer()
                .getReloadableRegistries()
                .getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, getLootTableId()));

        return lootTable.generateLoot(lootContextParameterSet);
    }

    private boolean spawnPokemonIfNeeded(ServerWorld world, PlayerEntity player) {
        if (world.random.nextDouble() < rarity.getPokemonSpawnChance()) {
            spawnPokemon(world, player);
            return true;
        }
        return false;
    }

    private void spawnPokemon(ServerWorld world, PlayerEntity player) {
        try {
            Pokemon pokemon = PokemonSelector.selectPokemonByRarity(world, player, rarity);
            if (pokemon != null) {
                PokemonHandler.addPokemonToPlayer(player, pokemon, rarity);
            }
        } catch (Exception e) {
            CobbleGachaMachine.LOGGER.error("Pokemon spawn error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void playEffects(World world, PlayerEntity player) {
        SoundEvent sound = rarity.getSound();

        if (world instanceof ServerWorld serverWorld) {
            switch (rarity.getName()) {
                case "normal":
                    ServerPlayNetworking.send((ServerPlayerEntity) player, PlayRewardNormalSoundPayload.INSTANCE);
                    break;
                case "rare":
                    ServerPlayNetworking.send((ServerPlayerEntity) player, PlayRewardRareSoundPayload.INSTANCE);
                    break;
                case "ultra_rare":
                    ServerPlayNetworking.send((ServerPlayerEntity) player, PlayRewardUltraRareSoundPayload.INSTANCE);
                    break;
                case "legendary":
                    ServerPlayNetworking.send((ServerPlayerEntity) player, PlayRewardLegendarySoundPayload.INSTANCE);
                    break;
            }
            serverWorld.spawnParticles(
                    rarity.getParticle(),
                    player.getX(),
                    player.getY() + 1.0,
                    player.getZ(),
                    rarity.getParticleCount(),
                    0.5, 0.5, 0.5, 0.1
            );
        } else {
            world.playSound(null, player.getBlockPos(), sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    public final Identifier getLootTableId() {
        if(lootTableId == null) {
            lootTableId = Registries.ITEM.getId(this).withPrefixedPath("capsules/");
        }
        return lootTableId;
    }

    public CapsuleRarity getRarity() {
        return this.rarity;
    }
}
