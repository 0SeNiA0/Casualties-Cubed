package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.HumanoidArm;

public record ServerboundSwapItemsPacket(HumanoidArm armFrom, byte bagSlotFrom, HumanoidArm armTo, byte bagSlotTo) {

    public ServerboundSwapItemsPacket(FriendlyByteBuf buf) {
        this(buf.readEnum(HumanoidArm.class), buf.readByte(), buf.readEnum(HumanoidArm.class), buf.readByte());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(armFrom);
        buf.writeByte(bagSlotFrom);
        buf.writeEnum(armTo);
        buf.writeByte(bagSlotTo);
    }
}
