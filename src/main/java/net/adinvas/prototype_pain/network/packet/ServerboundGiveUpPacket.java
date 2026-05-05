package net.adinvas.prototype_pain.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public class ServerboundGiveUpPacket {

    public ServerboundGiveUpPacket() {}

    // Decoder (from bytes)
    public ServerboundGiveUpPacket(FriendlyByteBuf buf) {
        // No payload to read
    }

    // Encoder (to bytes)
    public void write(FriendlyByteBuf buf) {
        // No payload to write
    }
}
