package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
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
        if (data.sickness() > 50) {
            vomitTime += Util.TICK_TO_SEC * data.sickness() * 0.01f;
        } else {
            vomitTime += Util.TICK_TO_SEC * data.sickness() * 0.0025f;
        }

        Painkillers painkillers = data.painkillers;
        if (painkillers.currentOpiateReception() < 0) {
            vomitTime -= Mth.clamp(painkillers.currentOpiateReception(), -25, 0) * Util.TICK_TO_SEC * 0.02f;
        }

        bloodVomitTime += Util.TICK_TO_SEC * data.internalBleedingCapped() * 1.25f;

        if (bloodVomitTime > 15) {
            bloodVomitTime = 0;
            vomitBlood(player);
        }

        if (vomitTime > 60) {
            vomitTime = 0;
            vomit(player);
        }

        if (vomitProgress > -1) doVomit(player);
        if (bloodVomitProgress > -1) doBloodVomit(player);
    }

    public void vomit(ServerPlayer player) {
        if (vomitProgress > -1) {
            vomitPower += 1 / (vomitPower + 1);
            return;
        }

        vomitProgress = 0;
        player.playNotifySound(ModSounds.VOMIT_WARNING.get(), SoundSource.PLAYERS, 1, 1);
    }

    private void doVomit(ServerPlayer player) {
        if (vomitProgress < 5 * 20) {// 5s
            data.temporarySlowdown = Math.max(data.temporarySlowdown, vomitProgress / 20 * 0.15f);
            vomitProgress++;
            data.consciousness(Math.min(data.consciousness(), 100 - vomitProgress * Util.TICK_TO_SEC * 13.5f));
            return;
        }

        data.addHappiness(-2.5f);
        data.addHunger(-(17 + vomitPower * 5));
        data.drink(-(10 + vomitPower * 5));
        data.addSickness(-8 - vomitPower * 5);
        data.bloodVolume(data.bloodVolume() - Util.CU_BLOOD_POINT_AS_L);

        if (vomitPower > 0) {
            //extra particles
            data.internalBleeding(data.internalBleeding() + vomitPower * Util.CUBloodPointsToL(10));
        }

        data.temporarySlowdown = Math.max(data.temporarySlowdown, 0.95f);

        player.serverLevel().playSound(null, player,ModSounds.VOMIT.get(), SoundSource.PLAYERS, 1, 1);
        if (player.isSleeping()) {//if sleeping without pills wake up
            player.stopSleeping();
        }

        vomitProgress = -1;
        vomitPower = 0;
    }

    public void vomitBlood(ServerPlayer player) {
        if (bloodVomitProgress == -1) {
            player.playNotifySound(ModSounds.BLOOD_VOMIT_WARNING.get(), SoundSource.PLAYERS, 1, 1);
        }
        bloodVomitProgress = 0;
    }

    private void doBloodVomit(ServerPlayer player) {
        if (bloodVomitProgress < 5 * 20) {
            data.temporarySlowdown = Math.max(data.temporarySlowdown, bloodVomitProgress / 20 * 0.15f);
            bloodVomitProgress++;
            data.consciousness(Math.min(data.consciousness(), 100 - bloodVomitProgress * Util.TICK_TO_SEC * 10));
            return;
        }

        data.addHappiness(-0.8f);
        data.temporarySlowdown = Math.max(data.temporarySlowdown, 0.6f);
        data.drink(-1);
        data.bloodVolume(data.bloodVolume() - Util.CU_BLOOD_POINT_AS_L);

        player.serverLevel().playSound(null, player,ModSounds.VOMIT.get(), SoundSource.PLAYERS, 1, 1);
        if (player.isSleeping()) {//if sleeping without pills wake up
            player.stopSleeping();
        }

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
