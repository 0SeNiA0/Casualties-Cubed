package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record ServerboundRagdollPacket(boolean ragdoll) {

    public ServerboundRagdollPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(ragdoll);
    }
}
