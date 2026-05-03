package net.adinvas.prototype_pain.network.packet;
import net.adinvas.prototype_pain.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TriggerLastStandPacket {

    public TriggerLastStandPacket(){}

    public TriggerLastStandPacket(FriendlyByteBuf buf){
    }

    public void write(FriendlyByteBuf buf){
    }
    public static void handle(TriggerLastStandPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context c = ctx.get();
        // This packet is registered PLAY_TO_CLIENT, but still guard & offload client code:
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler::handleLastStand);
        c.setPacketHandled(true);
    }

}
