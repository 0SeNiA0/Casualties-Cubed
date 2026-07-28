package net.zaharenko424.casualties_cubed.network.packet;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;

public record ServerboundUseBandagePacket(int targetId, Limb limb, InteractionHand usedHand, byte bagSlot) {

    public ServerboundUseBandagePacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readEnum(InteractionHand.class), buf.readByte());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeEnum(usedHand);
        buf.writeByte(bagSlot);
    }
}
