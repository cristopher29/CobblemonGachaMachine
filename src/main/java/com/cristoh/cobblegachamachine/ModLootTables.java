package com.cristoh.cobblegachamachine;

import com.cristoh.cobblegachamachine.config.DefaultConfig;
import com.cristoh.cobblegachamachine.mixin.LootContextTypesAccessor;
import com.google.common.collect.BiMap;
import net.fabricmc.fabric.api.loot.v3.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.mixin.loot.LootTableAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModLootTables {

    public static LootContextType CAPSULE;

    protected static LootContextType registerLootContext(Identifier id, Consumer<LootContextType.Builder> type) {
        LootContextType.Builder builder = LootContextType.create();
        type.accept(builder);
        LootContextType lootContextType = builder.build();

        @SuppressWarnings("unchecked")
        BiMap<Identifier, LootContextType> map = LootContextTypesAccessor.getMAP();

        LootContextType check = map.put(id, lootContextType);
        if (check != null) {
            throw new IllegalStateException("Loot table parameter set " + id + " is already registered");
        }
        return lootContextType;
    }

    public static void registerModifiers() {
        // Modificar loot tables cuando se cargan
        LootTableEvents.REPLACE.register((key, table, source, lookupProvider) -> {
            // Obtener el ID de la loot table
            Identifier tableId = key.getValue();

            // Verificar si es una loot table de cápsulas
            if (source.isBuiltin() && tableId.getNamespace().equals(CobbleGachaMachine.MOD_ID) &&
                    tableId.getPath().startsWith("capsules/")) {

                String capsuleType = tableId.getPath().replace("capsules/", "");


                LootTable.Builder builder = LootTable.builder();
                LootTableAccessor accessor = (LootTableAccessor) table;

                builder.type(table.getType());
                var pools = accessor.fabric_getPools();
                var pool = FabricLootPoolBuilder.copyOf(pools.getFirst());

                if (DefaultConfig.isMegashowdownItemsEnabled() &&
                        FabricLoader.getInstance().isModLoaded("mega_showdown")) {
                    CobbleGachaMachine.LOGGER.info("Modifying loot table with MegaShowdownPool");
                    addMegaShowdownPool(pool, capsuleType);
                }

                builder.apply(accessor.fabric_getFunctions());
                accessor.fabric_getRandomSequenceId().ifPresent(builder::randomSequenceId);
                builder.pool(pool.build());
                return builder.build();
            }

            return null;
        });
    }

    private static void addMegaShowdownPool(LootPool.Builder poolBuilder, String capsuleType) {

        // Agregar items según el tipo de cápsula
        switch (capsuleType) {
            case "normal_capsule":
                // Tera Shards - cantidad entre 1-3 (peso 5 = más difícil pero no imposible)
                addItemWithCount(poolBuilder, "mega_showdown:bug_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:dark_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:dragon_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:electric_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:fairy_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:fighting_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:fire_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:flying_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:ghost_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:grass_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:ground_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:ice_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:normal_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:poison_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:psychic_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:rock_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:steel_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:water_tera_shard", 1, 5);
                addItemWithCount(poolBuilder, "mega_showdown:stellar_tera_shard", 1, 5);

                // Items especiales - cantidad fija de 1 (peso 5)
                addItemWithCount(poolBuilder, "mega_showdown:max_honey", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:max_mushroom", 1, 1);
                break;

            case "rare_capsule":
                // Ogerpon mask items
                addItemWithCount(poolBuilder, "mega_showdown:wellspring_mask", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:cornerstone_mask", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:hearthflame_mask", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:reassembly_unit", 1, 1);

                // Oricorio form items
                addItemWithCount(poolBuilder, "mega_showdown:pink_nectar", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:purple_nectar", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:red_nectar", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:yellow_nectar", 1, 1);

                addItemWithCount(poolBuilder, "mega_showdown:blue_orb", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:red_orb", 1, 1);

                // Rotom form items
                addItemWithCount(poolBuilder, "mega_showdown:rotom_fan", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:rotom_washing_machine", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:rotom_fridge", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:rotom_mow", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:rotom_oven", 1, 1);

                // Arceus plates
//                addItemWithCount(poolBuilder, "mega_showdown:flameplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:splashplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:zapplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:meadowplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:icicleplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:fistplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:toxicplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:earthplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:skyplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:mindplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:insectplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:stoneplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:spookyplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:dracoplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:dreadplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:ironplate", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:pixieplate", 1, 1);

                // Tera Shards - cantidad entre 1-3 (peso 5 = más difícil pero no imposible)
                addItemWithCount(poolBuilder, "mega_showdown:bug_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:dark_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:dragon_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:electric_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:fairy_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:fighting_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:fire_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:flying_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:ghost_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:grass_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:ground_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:ice_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:normal_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:poison_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:psychic_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:rock_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:steel_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:water_tera_shard", 10, 25);
                addItemWithCount(poolBuilder, "mega_showdown:stellar_tera_shard", 10, 25);

                // Items especiales - cantidad fija de 1 (peso 5)
                addItemWithCount(poolBuilder, "mega_showdown:max_honey", 2, 5);
                addItemWithCount(poolBuilder, "mega_showdown:max_mushroom", 2, 5);

                // Silvally Memories - cantidad 1 (peso 10 = probabilidad normal/equilibrada)
//                addItemWithCount(poolBuilder, "mega_showdown:bugmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:darkmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:dragonmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:electricmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:fairymemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:fightingmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:firememory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:flyingmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:ghostmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:grassmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:groundmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:icememory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:poisonmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:psychicmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:rockmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:steelmemory", 1, 1);
//                addItemWithCount(poolBuilder, "mega_showdown:watermemory", 1, 1);

                // Items especiales (peso 10 = normal)
                addItemWithCount(poolBuilder, "mega_showdown:mega_stone", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:sparkling_stone_light", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:sparkling_stone_dark", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:dynamax_candy", 1, 2);
                addItemWithCount(poolBuilder, "mega_showdown:ash_cap", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:max_soup", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:sweet_max_soup", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:zygarde_core", 1, 2);
                addItemWithCount(poolBuilder, "mega_showdown:zygarde_cell", 1, 5);

                // Items más raros (peso 8 = ligeramente menos común)
                addItemWithCount(poolBuilder, "mega_showdown:keystone", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:wishing_star", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:blank_z", 1, 1);
                break;

            case "ultra_rare_capsule":
                addItemWithCount(poolBuilder, "mega_showdown:deoxys_meteorite", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:dna_splicer", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:gracidea_flower", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:n_lunarizer", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:n_solarizer", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:reins_of_unity", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:reveal_glass", 1, 1);


                addItemWithCount(poolBuilder, "mega_showdown:zygarde_cube", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:zygarde_core", 5, 5);
                addItemWithCount(poolBuilder, "mega_showdown:zygarde_cell", 50, 90);

                addItemWithCount(poolBuilder, "mega_showdown:rotom_catalogue", 1, 1);

                // Orbs y items legendarios (peso 50 = más fácil de obtener)

                addItemWithCount(poolBuilder, "mega_showdown:tera_orb", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:mega_bracelet", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:z_ring", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:rusted_shield", 1, 1);
                addItemWithCount(poolBuilder, "mega_showdown:dynamax_band", 1, 1);

                // Z-Crystals (peso 50 = más fácil)
                String[] zCrystals = {
                        "aloraichium_z", "decidium_z", "eevium_z", "fairium_z", "fightinium_z",
                        "incinium_z", "kommonium_z", "lunalium_z", "lycanium_z", "marshadium_z",
                        "mewnium_z", "mimikium_z", "pikanium_z", "pikashunium_z", "primarium_z",
                        "snorlium_z", "solganium_z", "tapunium_z", "ultranecrozium_z", "waterium_z",
                        "steelium_z", "rockium_z", "psychium_z", "poisonium_z", "normalium_z",
                        "icium_z", "groundium_z", "grassium_z", "ghostium_z", "flyinium_z",
                        "firium_z", "electrium_z", "dragonium_z", "darkinium_z", "buginium_z"
                };

                for (String crystal : zCrystals) {
                    addItemWithCount(poolBuilder, "mega_showdown:" + crystal, 1, 1);
                }

                // Mega Stones (peso 50 = más fácil de obtener)
                String[] megaStones = {
                        "absolite", "aerodactylite", "abomasite", "alakazite", "altarianite",
                        "ampharosite", "audinite", "banettite", "beedrillite", "blastoisinite",
                        "blazikenite", "cameruptite", "charizardite_y", "charizardite_x", "diancite",
                        "galladite", "garchompite", "gardevoirite", "gengarite", "glalitite",
                        "gyaradosite", "heracronite", "houndoominite", "kangaskhanite", "latiasite",
                        "latiosite", "lopunnite", "lucarionite", "manectite", "mawilite",
                        "medichamite", "metagrossite", "mewtwonite_y", "mewtwonite_x", "pidgeotite",
                        "pinsirite", "sablenite", "salamencite", "scizorite", "sceptilite",
                        "sharpedonite", "slowbronite", "steelixite", "swampertite", "tyranitarite",
                        "venusaurite"
                };

                for (String megaStone : megaStones) {
                    addItemWithCount(poolBuilder, "mega_showdown:" + megaStone, 1, 1);
                }

                addItemWithCount(poolBuilder, "mega_showdown:prison_bottle", 1, 1);
                break;

            default:
                return; // No modificar otros tipos
        }
    }

    /**
     * Método helper para agregar items con cantidad
     *
     * @param poolBuilder El pool builder
     * @param itemId ID del item (ejemplo: "mega_showdown:mega_stone")
     * @param minCount Cantidad mínima
     * @param maxCount Cantidad máxima
     */
    private static void addItemWithCount(LootPool.Builder poolBuilder, String itemId,
                                         int minCount, int maxCount) {
        ItemEntry.Builder<?> itemBuilder = ItemEntry.builder(
                Registries.ITEM.get(Identifier.of(itemId))
        );

        // Si min y max son iguales, usar ConstantLootNumberProvider
        // Si son diferentes, usar UniformLootNumberProvider para cantidad aleatoria
        if (minCount == maxCount) {
            if (minCount != 1) { // Solo agregar función si no es 1
                itemBuilder.apply(
                        SetCountLootFunction.builder(
                                ConstantLootNumberProvider.create(minCount)
                        )
                );
            }
        } else {
            itemBuilder.apply(
                    SetCountLootFunction.builder(
                            UniformLootNumberProvider.create(minCount, maxCount)
                    )
            );
        }

        poolBuilder.with(itemBuilder);
    }

    public static void register() {
        CobbleGachaMachine.LOGGER.info("Registering custom loot tables");
        CAPSULE = registerLootContext(Identifier.of(CobbleGachaMachine.MOD_ID, "capsule"),
                builder -> builder.require(LootContextParameters.ORIGIN)
                        .allow(LootContextParameters.THIS_ENTITY));
        registerModifiers();
    }
}