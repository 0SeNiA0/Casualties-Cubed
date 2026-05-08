package net.adinvas.casualties_cubed.network.packet;

import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;

public record ServerboundCauterizeActionPacket(int targetId, Limb limb) {

    public ServerboundCauterizeActionPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class));
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
    }
}
