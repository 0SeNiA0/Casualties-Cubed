package net.adinvas.prototype_pain.network.packet;

import net.adinvas.prototype_pain.limbs.Limb;
import net.adinvas.prototype_pain.network.MedicalAction;
import net.minecraft.network.FriendlyByteBuf;

///Only supports {@link MedicalAction#REMOVE_SPLINT} and {@link MedicalAction#REMOVE_TOURNIQUET}
public record ServerboundMedicalActionPacket(int targetId, Limb limb, MedicalAction action) {

    public ServerboundMedicalActionPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), buf.readEnum(MedicalAction.class));
    }

    public void encode(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);
        buf.writeEnum(action);
    }
}
