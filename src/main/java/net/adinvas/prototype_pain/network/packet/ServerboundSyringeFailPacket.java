package net.adinvas.prototype_pain.network.packet;

import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;

public record ServerboundSyringeFailPacket(int targetId, Limb limb) {

    public ServerboundSyringeFailPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
    }
}
