package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayRewardRareSoundPayload implements CustomPayload {
    public static final Id<PlayRewardRareSoundPayload> ID = new Id<>(Identifier.of(CobbleGachaMachine.MOD_ID, "play_reward_rare_sound"));

    public static final PlayRewardRareSoundPayload INSTANCE = new PlayRewardRareSoundPayload();

    private PlayRewardRareSoundPayload() {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final PacketCodec<ByteBuf, PlayRewardRareSoundPayload> CODEC = PacketCodec.unit(INSTANCE);
}