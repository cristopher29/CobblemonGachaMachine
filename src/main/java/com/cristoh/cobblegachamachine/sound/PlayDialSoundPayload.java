package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayDialSoundPayload implements CustomPayload {
    public static final Id<PlayDialSoundPayload> ID = new Id<>(Identifier.of(CobbleGachaMachine.MOD_ID, "play_dial_sound"));

    public static final PlayDialSoundPayload INSTANCE = new PlayDialSoundPayload();

    private PlayDialSoundPayload() {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final PacketCodec<ByteBuf, PlayDialSoundPayload> CODEC = PacketCodec.unit(INSTANCE);
}