package net.adinvas.prototype_pain.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ServerboundFluidTransferPacket(ItemStack source, ItemStack target, int helperSlot, float amount) {

    public ServerboundFluidTransferPacket(FriendlyByteBuf buf) {
        this(buf.readItem(), buf.readItem(), buf.readVarInt(), buf.readFloat());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeItem(source);
        buf.writeItem(target);
        buf.writeVarInt(helperSlot);
        buf.writeFloat(amount);
    }
}
