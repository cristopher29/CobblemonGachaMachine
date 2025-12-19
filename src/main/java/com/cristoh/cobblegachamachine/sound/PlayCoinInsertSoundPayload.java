package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayCoinInsertSoundPayload implements CustomPayload {
    public static final Id<PlayCoinInsertSoundPayload> ID = new Id<>(Identifier.of(CobbleGachaMachine.MOD_ID, "play_coin_insert_sound"));

    public static final PlayCoinInsertSoundPayload INSTANCE = new PlayCoinInsertSoundPayload();

    private PlayCoinInsertSoundPayload() {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final PacketCodec<ByteBuf, PlayCoinInsertSoundPayload> CODEC = PacketCodec.unit(INSTANCE);
}