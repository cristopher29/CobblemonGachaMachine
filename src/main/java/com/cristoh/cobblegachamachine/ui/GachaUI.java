package com.cristoh.cobblegachamachine.ui;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class GachaUI {

    public static void openGachaUI(ServerPlayerEntity player, BlockPos pos) {
        CustomSimpleGUI gui = new CustomSimpleGUI(ScreenHandlerType.GENERIC_9X3, player, false, pos);
        gui.open();
    }
}
