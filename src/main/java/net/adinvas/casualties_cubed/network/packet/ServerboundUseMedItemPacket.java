package net.adinvas.casualties_cubed.network.packet;

import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;

public record ServerboundUseMedItemPacket(int targetId, Limb limb, InteractionHand usedHand, byte bagSlot) {

    public ServerboundUseMedItemPacket(int targetId, Limb limb, InteractionHand usedHand) {
        this(targetId, limb, usedHand, (byte) -1);
    }

    public ServerboundUseMedItemPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readEnum(InteractionHand.class), buf.readByte());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeEnum(usedHand);
        buf.writeByte(bagSlot);
    }
}
