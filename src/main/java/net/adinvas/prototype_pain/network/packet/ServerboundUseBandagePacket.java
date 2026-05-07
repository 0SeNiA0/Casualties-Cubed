package net.adinvas.prototype_pain.network.packet;

import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;

public record ServerboundUseBandagePacket(int targetId, Limb limb, InteractionHand usedHand, byte bagSlot, float durability) {

    public ServerboundUseBandagePacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readEnum(InteractionHand.class), buf.readByte(), buf.readFloat());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeEnum(usedHand);
        buf.writeByte(bagSlot);
        buf.writeFloat(durability);
    }
}
