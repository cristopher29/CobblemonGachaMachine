package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayRewardNormalSoundPayload implements CustomPayload {
    public static final Id<PlayRewardNormalSoundPayload> ID = new Id<>(Identifier.of(CobbleGachaMachine.MOD_ID, "play_reward_normal_sound"));

    public static final PlayRewardNormalSoundPayload INSTANCE = new PlayRewardNormalSoundPayload();

    private PlayRewardNormalSoundPayload() {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final PacketCodec<ByteBuf, PlayRewardNormalSoundPayload> CODEC = PacketCodec.unit(INSTANCE);
}