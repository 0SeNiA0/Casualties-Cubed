package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.casualties_cubed.util.Util;

public class SleepingPills {

    private final PlayerHealthData data;

    private float amount;
    private boolean wasSleeping;

    public SleepingPills(PlayerHealthData data) {
        this.data = data;
    }

    public void add(float amount) {
        this.amount += amount;
    }

    public boolean isActive() {
        return amount > 0;
    }

    public void update(ServerPlayer player) {
        if (amount <= 0) {
            reset();
            return;
        }

        amount -= Util.TICK_TO_SEC;

        if (data.isSleeping(player)) {
            wasSleeping = true;
        } else if (wasSleeping) {
            wasSleeping = false;
            amount *= 0.75f;
        }

        Painkillers painkillers = data.painkillers;
        if (amount > (painkillers.currentOpiateReception() > 15f ? 150f : 900f)) {
            data.overdoseIndex(2);
            data.addBloodPressure(-Util.TICK_TO_SEC * 2.5f);

            if (data.respiratoryRate() > 70f) {
                data.respiratoryRate(Util.moveTowards(Util.TICK_TO_SEC * 15, data.respiratoryRate(), 70));
            }

            if (!data.isConscious()) {
                data.addBloodPressure(-Util.TICK_TO_SEC * 2.5f);
                data.heartRate(data.heartRate() - Util.TICK_TO_SEC);

                if (data.respiratoryRate() > 45f) {
                    data.respiratoryRate(Util.moveTowards(Util.TICK_TO_SEC * 15, data.respiratoryRate(), 40));
                }
            }
        }
    }

    public void reset() {
        amount = 0;
        wasSleeping = false;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putFloat("amount", amount);
        return tag;
    }

    public void load(CompoundTag tag) {
        amount = tag.getFloat("amount");
    }
}
