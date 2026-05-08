package net.adinvas.casualties_cubed.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;

public record ClientboundFluidSyncPacket(BlockPos pos, int tankId, FluidStack fluid) {

    public ClientboundFluidSyncPacket(FriendlyByteBuf buf){
        this(buf.readBlockPos(), buf.readVarInt(), buf.readFluidStack());
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeBlockPos(pos);
        buf.writeVarInt(tankId);
        buf.writeFluidStack(fluid);
    }
}
