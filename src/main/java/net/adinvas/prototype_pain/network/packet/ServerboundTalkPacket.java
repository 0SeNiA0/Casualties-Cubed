package net.adinvas.prototype_pain.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public class ServerboundTalkPacket {

    public ServerboundTalkPacket() {}

    // Decoder (from bytes)
    public ServerboundTalkPacket(FriendlyByteBuf buf) {
        // No payload to read
    }

    // Encoder (to bytes)
    public void toBytes(FriendlyByteBuf buf) {
        // No payload to write
    }
}
