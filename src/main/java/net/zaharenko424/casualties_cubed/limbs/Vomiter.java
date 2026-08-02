package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.zaharenko424.casualties_cubed.util.Util;

public class Vomiter {

    private final PlayerHealthData data;

    private float vomitTime;
    private float bloodVomitTime;

    private float vomitProgress = -1;//C:U uses coroutine and therefore doesn't need this
    private float bloodVomitProgress = -1;

    private float vomitPower;

    public Vomiter(PlayerHealthData data) {
        this.data = data;
    }

    public void update(ServerPlayer player) {
        if (data.getSickness() > 50) {
            vomitTime += Util.TICK_TO_SEC * data.getSickness() * 0.01f;
        } else {
            vomitTime += Util.TICK_TO_SEC * data.getSickness() * 0.0025f;
        }

        bloodVomitTime += Util.TICK_TO_SEC * data.getInternalBleedingCapped() * 1.25f;

        if (bloodVomitTime > 15) {
            bloodVomitTime = 0;
            vomitBlood();
        }

        if (vomitTime > 60) {
            vomitTime = 0;
            vomit();
        }

        if (vomitProgress > -1) doVomit(player);
        if (bloodVomitProgress > -1) doBloodVomit(player);
    }

    public void vomit() {
        if (vomitProgress > -1) {
            vomitPower += 1 / (vomitPower + 1);
            return;
        }

        vomitProgress = 0;
    }

    private void doVomit(ServerPlayer player) {
        if (vomitProgress < 5 * 20) {// 5s
            data.temporarySlowdown = Math.max(data.temporarySlowdown, vomitProgress / 20 * 0.15f);
            vomitProgress++;
            data.setConsciousness(Math.min(data.getConsciousness(), 100 - vomitProgress * Util.TICK_TO_SEC * 13.5f));
            return;
        }

        //decrease hunger & thirst (a bit misleading as those are intended as points of not hunger and points of not thirst)
        FoodData food = player.getFoodData();
        food.setFoodLevel(food.getFoodLevel() - 2 - Math.round(0.5f * vomitPower));
        data.addSickness(-8 - vomitPower * 5);
        data.setBloodVolume(data.getBloodVolume() - Util.CU_BLOOD_POINT_AS_L);

        if (vomitPower > 0) {
            //extra particles
            data.setInternalBleeding(data.getInternalBleeding() + vomitPower * Util.CUBloodPointsToL(10));
        }

        data.temporarySlowdown = Math.max(data.temporarySlowdown, 0.95f);

        //if is sleeping without pills wake up

        vomitProgress = -1;
        vomitPower = 0;
    }

    public void vomitBlood() {
        bloodVomitProgress = 0;
    }

    private void doBloodVomit(ServerPlayer player) {
        if (bloodVomitProgress < 5 * 20) {
            data.temporarySlowdown = Math.max(data.temporarySlowdown, bloodVomitProgress / 20 * 0.15f);
            bloodVomitProgress++;
            data.setConsciousness(Math.min(data.getConsciousness(), 100 - bloodVomitProgress * Util.TICK_TO_SEC * 10));
            return;
        }

        //decrease thirst
        data.setBloodVolume(data.getBloodVolume() - Util.CU_BLOOD_POINT_AS_L);

        data.temporarySlowdown = Math.max(data.temporarySlowdown, 0.6f);

        //if is sleeping without pills wake up

        bloodVomitProgress = -1;
    }

    public void reset() {
        vomitTime = bloodVomitTime = 0;
        vomitProgress = bloodVomitProgress = -1;
        vomitPower = 0;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("VomitTime", vomitTime);
        tag.putFloat("BloodVomitTime", bloodVomitTime);
        tag.putFloat("VomitProgress", vomitProgress);
        tag.putFloat("VomitPower", vomitPower);
        tag.putFloat("BloodVomitProgress", bloodVomitProgress);
        return tag;
    }

    public void load(CompoundTag tag) {
        vomitTime = tag.getFloat("VomitTime");
        bloodVomitTime = tag.getFloat("BloodVomitTime");
        vomitProgress = tag.getFloat("VomitProgress");
        vomitPower = tag.getFloat("VomitPower");
        bloodVomitProgress = tag.getFloat("BloodVomitProgress");
    }
}
