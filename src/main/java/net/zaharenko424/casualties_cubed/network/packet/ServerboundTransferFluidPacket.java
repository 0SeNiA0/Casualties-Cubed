package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public record ServerboundTransferFluidPacket(int toSlot, int amount) {

    public ServerboundTransferFluidPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(toSlot);
        buf.writeVarInt(amount);
    }
}
