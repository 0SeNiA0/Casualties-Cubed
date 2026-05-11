package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public class ServerboundGiveUpPacket {

    public ServerboundGiveUpPacket() {}

    // Decoder (from bytes)
    public ServerboundGiveUpPacket(FriendlyByteBuf buf) {
        // No payload to read
    }

    // Encoder (to bytes)
    public void encode(FriendlyByteBuf buf) {
        // No payload to write
    }
}
