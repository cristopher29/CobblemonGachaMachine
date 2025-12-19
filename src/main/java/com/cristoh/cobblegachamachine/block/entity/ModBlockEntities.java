package com.cristoh.cobblegachamachine.block.entity;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import com.cristoh.cobblegachamachine.block.ModBlocks;
import com.cristoh.cobblegachamachine.block.entity.custom.GachaBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static BlockEntityType<GachaBlockEntity> GACHA_BLOCK_ENTITY;

    public static void register() {
        GACHA_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(CobbleGachaMachine.MOD_ID, "gacha_block_entity"),
                FabricBlockEntityTypeBuilder.create(GachaBlockEntity::new, ModBlocks.GACHA_BLOCK).build(null));
    }
}
