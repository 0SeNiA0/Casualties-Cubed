package net.zaharenko424.casualties_cubed.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ServerboundExchangeItemInHandPacket(ItemStack target, boolean offhand) {

    public ServerboundExchangeItemInHandPacket(FriendlyByteBuf buf) {
        this(buf.readItem(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeItem(target);
        buf.writeBoolean(offhand);
    }
}
