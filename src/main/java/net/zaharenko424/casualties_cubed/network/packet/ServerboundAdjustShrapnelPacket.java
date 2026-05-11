package net.zaharenko424.casualties_cubed.network.packet;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;

public record ServerboundAdjustShrapnelPacket(int targetId, Limb limb, int amount) {

    public ServerboundAdjustShrapnelPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readVarInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.targetId);
        buf.writeEnum(this.limb);
        buf.writeVarInt(this.amount);
    }
}
