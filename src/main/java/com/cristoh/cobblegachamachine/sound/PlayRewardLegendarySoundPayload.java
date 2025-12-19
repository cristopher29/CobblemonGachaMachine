package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayRewardLegendarySoundPayload implements CustomPayload {
    public static final Id<PlayRewardLegendarySoundPayload> ID = new Id<>(Identifier.of(CobbleGachaMachine.MOD_ID, "play_reward_legendary_sound"));

    public static final PlayRewardLegendarySoundPayload INSTANCE = new PlayRewardLegendarySoundPayload();

    private PlayRewardLegendarySoundPayload() {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final PacketCodec<ByteBuf, PlayRewardLegendarySoundPayload> CODEC = PacketCodec.unit(INSTANCE);
}