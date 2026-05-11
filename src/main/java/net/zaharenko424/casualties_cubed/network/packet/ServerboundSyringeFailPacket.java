package net.zaharenko424.casualties_cubed.network.packet;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;

public record ServerboundSyringeFailPacket(int targetId, Limb limb) {

    public ServerboundSyringeFailPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
    }
}
