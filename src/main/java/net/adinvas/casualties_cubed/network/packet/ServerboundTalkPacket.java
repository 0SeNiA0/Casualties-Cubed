package net.adinvas.casualties_cubed.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public class ServerboundTalkPacket {

    public ServerboundTalkPacket() {}

    // Decoder (from bytes)
    public ServerboundTalkPacket(FriendlyByteBuf buf) {
        // No payload to read
    }

    // Encoder (to bytes)
    public void encode(FriendlyByteBuf buf) {
        // No payload to write
    }
}
