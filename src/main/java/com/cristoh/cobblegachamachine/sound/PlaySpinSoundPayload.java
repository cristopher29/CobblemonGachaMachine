package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlaySpinSoundPayload implements CustomPayload {
    public static final CustomPayload.Id<PlaySpinSoundPayload> ID = new CustomPayload.Id<>(Identifier.of(CobbleGachaMachine.MOD_ID, "play_spin_sound"));

    public static final PlaySpinSoundPayload INSTANCE = new PlaySpinSoundPayload();

    private PlaySpinSoundPayload() {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final PacketCodec<ByteBuf, PlaySpinSoundPayload> CODEC = PacketCodec.unit(INSTANCE);
}