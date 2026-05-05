package net.adinvas.prototype_pain.network.packet;

import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ServerboundUseMedItemPacket(int targetId, Limb limb, ItemStack item, boolean offhand) {

    public ServerboundUseMedItemPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readItem(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeItem(item);
        buf.writeBoolean(offhand);
    }
}
