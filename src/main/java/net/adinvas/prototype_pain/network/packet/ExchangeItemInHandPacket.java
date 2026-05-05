package net.adinvas.prototype_pain.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ExchangeItemInHandPacket(ItemStack target, boolean offhand) {

    public ExchangeItemInHandPacket(FriendlyByteBuf buf) {
        this(buf.readItem(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf){
        buf.writeItem(target);
        buf.writeBoolean(offhand);
    }
}
