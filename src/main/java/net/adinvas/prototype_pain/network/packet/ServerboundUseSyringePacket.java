package net.adinvas.prototype_pain.network.packet;

import net.adinvas.prototype_pain.limbs.Limb;
import net.minecraft.network.FriendlyByteBuf;

///FIXME DONT LISTEN TO SOME RANDOM CLIENT DATA, TAKE DATA FROM ITEM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public record ServerboundUseSyringePacket(int targetId, Limb limb, String[] ids, float[] amounts) {

    public ServerboundUseSyringePacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readEnum(Limb.class), readStrAr(buf), readFloatAr(buf));
    }

    private static String[] readStrAr(FriendlyByteBuf buf) {
        int idCount = buf.readVarInt();
        String[] ids = new String[idCount];
        for (int i = 0; i < idCount; i++) {
            ids[i] = buf.readUtf();
        }
        return ids;
    }

    private static float[] readFloatAr(FriendlyByteBuf buf) {
        int amountCount = buf.readVarInt();
        float[] amounts = new float[amountCount];
        for (int i = 0; i < amountCount; i++) {
            amounts[i] = buf.readFloat();
        }
        return amounts;
    }

    public void write(FriendlyByteBuf buf){
        buf.writeVarInt(targetId);
        buf.writeEnum(limb);

        buf.writeVarInt(this.ids.length);
        for (String id : this.ids){
            buf.writeUtf(id);
        }

        buf.writeVarInt(this.amounts.length);
        for (Float amount : this.amounts){
            buf.writeFloat(amount);
        }
    }
}
