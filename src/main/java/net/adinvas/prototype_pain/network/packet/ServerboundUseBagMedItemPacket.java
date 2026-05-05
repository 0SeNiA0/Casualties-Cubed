package net.adinvas.prototype_pain.network.packet;

import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ServerboundUseBagMedItemPacket(int targetId, Limb limb, ItemStack bag, int slot, ItemStack item, boolean offhand) {

    public ServerboundUseBagMedItemPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readItem(), buf.readVarInt(), buf.readItem(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeItem(bag);
        buf.writeVarInt(slot);
        buf.writeItem(item);
        buf.writeBoolean(offhand);
    }
}
