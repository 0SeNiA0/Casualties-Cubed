package net.adinvas.casualties_cubed.network.packet;

import net.adinvas.casualties_cubed.visual.ClientGamerules;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientboundBlindnessViewSyncPacket(int value) {

    public static ClientboundBlindnessViewSyncPacket decode(FriendlyByteBuf buf) {
        return new ClientboundBlindnessViewSyncPacket(buf.readVarInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(value);
    }

    public static void handle(ClientboundBlindnessViewSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientGamerules.blindnessViewDistance = msg.value;
        });
        ctx.get().setPacketHandled(true);
    }
}