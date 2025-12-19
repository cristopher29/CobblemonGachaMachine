package com.cristoh.cobblegachamachine.sound;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class PlayRewardUltraRareSoundPayload implements CustomPayload {
    public static final Id<PlayRewardUltraRareSoundPayload> ID = new Id<>(Identifier.of(CobbleGachaMachine.MOD_ID, "play_reward_ultra_rare_sound"));

    public static final PlayRewardUltraRareSoundPayload INSTANCE = new PlayRewardUltraRareSoundPayload();

    private PlayRewardUltraRareSoundPayload() {}

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static final PacketCodec<ByteBuf, PlayRewardUltraRareSoundPayload> CODEC = PacketCodec.unit(INSTANCE);
}