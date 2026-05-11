package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record ServerboundGuiSyncTogglePacket(int targetId, boolean enable) {

    public ServerboundGuiSyncTogglePacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeBoolean(enable);
    }
}
