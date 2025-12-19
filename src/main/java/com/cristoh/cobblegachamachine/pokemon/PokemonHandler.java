package com.cristoh.cobblegachamachine.pokemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cristoh.cobblegachamachine.item.custom.CapsuleRarity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PokemonHandler {
    public static void addPokemonToPlayer(PlayerEntity player, Pokemon pokemon, CapsuleRarity rarity) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }

        PartyStore party = Cobblemon.INSTANCE.getStorage().getParty(serverPlayer);

        if (party.add(pokemon)) {
            player.sendMessage(
                    Text.translatable("text.cobble-gacha-machine.pokemon_obtained").formatted(Formatting.WHITE)
                            .append(Text.literal(pokemon.getSpecies().getName()).formatted(rarity.getChatColor())),
                    false
            );
            player.sendMessage(Text.translatable("text.cobble-gacha-machine.pokemon_added").formatted(Formatting.WHITE), false);
        } else {
            PCStore pc = Cobblemon.INSTANCE.getStorage().getPC(serverPlayer);
            pc.add(pokemon);
            player.sendMessage(
                    Text.translatable("text.cobble-gacha-machine.pokemon_obtained").formatted(Formatting.WHITE)
                            .append(Text.literal(pokemon.getSpecies().getName()).formatted(rarity.getChatColor())),
                    false
            );
            player.sendMessage(Text.translatable("text.cobble-gacha-machine.pokemon_added_to_pc").formatted(Formatting.WHITE), false);
        }
    }
}
