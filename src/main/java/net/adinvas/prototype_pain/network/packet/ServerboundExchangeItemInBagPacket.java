package net.adinvas.prototype_pain.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ServerboundExchangeItemInBagPacket(ItemStack bag, int slot, ItemStack target, boolean offhand) {

    public ServerboundExchangeItemInBagPacket(FriendlyByteBuf buf) {
        this(buf.readItem(), buf.readVarInt(), buf.readItem(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeItem(bag);
        buf.writeVarInt(slot);
        buf.writeItem(target);
        buf.writeBoolean(offhand);
    }
}
