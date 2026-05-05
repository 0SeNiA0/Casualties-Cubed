package net.adinvas.prototype_pain.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record ServerboundCPRPacket(int targetId, Success success) {

    public enum Success {
        LOW,
        MEDIUM,
        HIGH
    }

    public ServerboundCPRPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Success.class));
    }

    public void write(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(success);
    }
}
