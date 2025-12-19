package com.cristoh.cobblegachamachine.block.entity.custom;

import com.cristoh.cobblegachamachine.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class GachaBlockEntity extends BlockEntity {

    public GachaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GACHA_BLOCK_ENTITY, pos, state);
    }
}
