package net.adinvas.prototype_pain.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public class ServerboundLegUsePacket {

    public ServerboundLegUsePacket() {}

    // Decoder (from bytes)
    public ServerboundLegUsePacket(FriendlyByteBuf buf) {
        // No payload to read
    }

    // Encoder (to bytes)
    public void encode(FriendlyByteBuf buf) {
        // No payload to write
    }
}
