package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record ClientboundSyncHealthPacket(int targetId, CompoundTag data) {

    public ClientboundSyncHealthPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeNbt(data);
    }
}
