package net.adinvas.prototype_pain.network.packet;

import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ServerboundUseBandagePacket(int targetId, Limb limb, ItemStack bandage, float durability) {

    public ServerboundUseBandagePacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readItem(), buf.readFloat());
    }

    public void write(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeItem(bandage);
        buf.writeFloat(durability);
    }
}
