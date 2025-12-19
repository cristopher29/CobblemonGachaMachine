package com.cristoh.cobblegachamachine.item;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import com.cristoh.cobblegachamachine.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {


    public static ItemGroup GACHA_ITEMS_GROUP;

    public static ItemGroup GACHA_BLOCKS_GROUP;


    public static void register() {
        CobbleGachaMachine.LOGGER.info("Registering item groups for " + CobbleGachaMachine.MOD_ID);

        GACHA_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
                Identifier.of(CobbleGachaMachine.MOD_ID, "gacha_items"),
                FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.GACHA_BLOCK))
                        .displayName(Text.translatable("itemgroup.cobble-gacha-machine.gacha_items"))
                        .entries((displayContext, entries) -> {
                            entries.add(ModBlocks.GACHA_BLOCK);
                            entries.add(ModItems.NORMAL_CAPSULE);
                            entries.add(ModItems.RARE_CAPSULE);
                            entries.add(ModItems.ULTRA_RARE_CAPSULE);
                            entries.add(ModItems.LEGENDARY_CAPSULE);

                        }).build());

        /*GACHA_BLOCKS_GROUP = Registry.register(Registries.ITEM_GROUP,
                Identifier.of(CobbleGachaMachine.MOD_ID, "pink_garnet_blocks"),
                FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.GACHA_BLOCK))
                        .displayName(Text.translatable("itemgroup.cobble-gacha-machine.gacha_blocks"))
                        .entries((displayContext, entries) -> {
                        }).build());*/
    }
}