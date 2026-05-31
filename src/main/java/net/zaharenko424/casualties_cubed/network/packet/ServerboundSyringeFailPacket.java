package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.world.InteractionHand;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;

public record ServerboundSyringeFailPacket(int targetId, Limb limb, InteractionHand usedHand) {

    public ServerboundSyringeFailPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readEnum(InteractionHand.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeEnum(usedHand);
    }
}
