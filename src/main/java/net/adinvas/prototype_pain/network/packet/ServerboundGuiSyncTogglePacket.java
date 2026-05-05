package net.adinvas.prototype_pain.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record ServerboundGuiSyncTogglePacket(int targetId, boolean enable) {

    public ServerboundGuiSyncTogglePacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeBoolean(enable);
    }
}
