package com.cristoh.cobblegachamachine.block;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import com.cristoh.cobblegachamachine.ModTags;
import com.cristoh.cobblegachamachine.block.custom.GachaBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static Block GACHA_BLOCK;

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        Registry.register(Registries.BLOCK, Identifier.of(CobbleGachaMachine.MOD_ID, name), block);
        return block;
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(CobbleGachaMachine.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void register() {
        CobbleGachaMachine.LOGGER.info("Registering custom blocks for " + CobbleGachaMachine.MOD_ID);

        GACHA_BLOCK = registerBlock("gacha_block",
                new GachaBlock(AbstractBlock.Settings.create().nonOpaque().strength(1.5f).instrument(NoteBlockInstrument.BASEDRUM))
        );
    }
}
