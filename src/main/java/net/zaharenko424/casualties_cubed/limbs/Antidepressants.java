package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.util.Util;

public class Antidepressants {

    private final PlayerHealthData data;

    private float amount;
    private float currentAmount;

    public Antidepressants(PlayerHealthData data) {
        this.data = data;
    }

    public void add(ServerPlayer player, float amount) {
        this.amount += amount;
        if (player.getRandom().nextFloat() < 0.3f) {
            data.addSickness(amount * 0.3f);
        }
    }

    public void update() {
        currentAmount = Util.moveTowards(Util.TICK_TO_SEC * 5, currentAmount, amount);

        if (amount > 0f) {
            amount -= Util.TICK_TO_SEC * 0.185f;
            data.antidepressantHappiness = -data.happiness() * 0.6f * Mth.clamp(currentAmount * 0.0166f, 0, 1);
            data.addHappiness(currentAmount * 3E-05f * Util.TICK_TO_SEC);
            if (currentAmount >= 250f) {
                data.addBloodPressure(-Util.TICK_TO_SEC * 3);
                data.overdoseIndex(2);
            }
            return;
        }

        data.antidepressantHappiness = 0f;
        reset();
    }

    public void reset() {
        amount = 0;
        currentAmount = 0;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putFloat("amount", amount);
        tag.putFloat("currentAmount", currentAmount);
        return tag;
    }

    public void load(CompoundTag tag) {
        amount = tag.getFloat("amount");
        currentAmount = tag.getFloat("currentAmount");
    }
}
