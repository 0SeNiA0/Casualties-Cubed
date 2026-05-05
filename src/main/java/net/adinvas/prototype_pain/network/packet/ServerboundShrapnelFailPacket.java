package net.adinvas.prototype_pain.network.packet;

import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;

public record ServerboundShrapnelFailPacket(int targetId, Limb limb) {

    public ServerboundShrapnelFailPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
    }
}
