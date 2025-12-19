package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayDispenseSoundPayload implements CustomPayload {
    public static final Id<PlayDispenseSoundPayload> ID = new Id<>(Identifier.of(CobbleGachaMachine.MOD_ID, "play_dispense_sound"));

    public static final PlayDispenseSoundPayload INSTANCE = new PlayDispenseSoundPayload();

    private PlayDispenseSoundPayload() {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final PacketCodec<ByteBuf, PlayDispenseSoundPayload> CODEC = PacketCodec.unit(INSTANCE);
}