package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.zaharenko424.casualties_cubed.limbs.Limb;

public record ServerboundUseSyringePacket(int targetId, Limb limb, InteractionHand usedHand, byte bagSlot, float[] amounts) {

    public ServerboundUseSyringePacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readEnum(InteractionHand.class), buf.readByte(), readFloatAr(buf));
    }

    private static float[] readFloatAr(FriendlyByteBuf buf) {
        int amountCount = buf.readVarInt();
        float[] amounts = new float[amountCount];
        for (int i = 0; i < amountCount; i++) {
            amounts[i] = buf.readFloat();
        }
        return amounts;
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeEnum(usedHand);
        buf.writeByte(bagSlot);

        buf.writeVarInt(this.amounts.length);
        for (Float amount : this.amounts){
            buf.writeFloat(amount);
        }
    }
}
