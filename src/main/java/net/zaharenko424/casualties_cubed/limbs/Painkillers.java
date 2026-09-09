package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.casualties_cubed.util.Util;

public class Painkillers {

    private final PlayerHealthData data;

    private float antagonistAmount;
    private float opiateAmount;

    private float opiateReception;
    private float currentOpiateReception;

    private float opiateTolerance;

    public Painkillers(PlayerHealthData data) {
        this.data = data;
    }

    public void addAntagonist(float amount) {
        if (opiateAmount != 0 || opiateTolerance != 0) antagonistAmount += amount;
    }

    public void addOpiates(float amount) {
        opiateAmount += amount;
    }

    public float currentOpiateReception() {
        return currentOpiateReception;
    }

    public void addTolerance(float amount) {
        if (opiateAmount != 0 || opiateTolerance != 0) opiateTolerance += amount;
    }

    public void update(ServerPlayer player) {
        antagonistAmount = Math.max(antagonistAmount - Util.TICK_TO_SEC, 0);

        opiateReception = opiateAmount - opiateTolerance;
        currentOpiateReception = Util.moveTowards(5 * Util.TICK_TO_SEC, currentOpiateReception, opiateReception);

        opiateTolerance = Util.moveTowards(opiateTolerance < opiateAmount ? 0.08f : 0.04f * Util.TICK_TO_SEC, opiateTolerance, opiateAmount);
        if (opiateReception > 45) {
            opiateTolerance = Util.moveTowards(0.022f * Util.TICK_TO_SEC, opiateTolerance, opiateAmount);
        }
        if (opiateReception < -30) {
            opiateTolerance = Util.moveTowards(0.025f * Util.TICK_TO_SEC, opiateTolerance, opiateAmount);
        }
        if (opiateTolerance < 0) opiateTolerance = 0;

        opiateAmount = Util.moveTowards(0.059f * Util.TICK_TO_SEC, opiateAmount, 0);
        if (opiateAmount > 100) {
            opiateAmount = Util.moveTowards(0.05f * Util.TICK_TO_SEC, opiateAmount, 0);
        }
        if (opiateAmount > 130) {
            opiateAmount = Util.moveTowards(0.07f * Util.TICK_TO_SEC, opiateAmount, 0);
        }
        if (opiateAmount > 160) {
            opiateAmount = Util.moveTowards(0.09f * Util.TICK_TO_SEC, opiateAmount, 0);
        }
        if (opiateAmount > 195) {
            opiateAmount = Util.moveTowards(0.11f * Util.TICK_TO_SEC, opiateAmount, 0);
        }
        if (opiateAmount > 235) {
            opiateAmount = Util.moveTowards(0.13f * Util.TICK_TO_SEC, opiateAmount, 0);
        }
        if (opiateAmount > 260) {
            opiateAmount = Util.moveTowards(0.15f * Util.TICK_TO_SEC, opiateAmount, 0);
        }
        if (opiateAmount > 300) {
            opiateAmount = Util.moveTowards(0.18f * Util.TICK_TO_SEC, opiateAmount, 0);
        }

        if (antagonistAmount > 0) {
            opiateAmount = Util.moveTowards(8 * Util.TICK_TO_SEC, opiateAmount, 0);
        }

        if (currentOpiateReception > 0) {
            LimbStatistics stats;
            for (Limb limb : Limb.values()) {
                stats = data.getLimb(limb);
                if (stats.isAmputated()) continue;

                stats.addPain(-currentOpiateReception * 0.3f * Util.TICK_TO_SEC);
            }
            data.opiateHappiness = currentOpiateReception;
        } else {
            data.opiateHappiness = currentOpiateReception * 1.66f;
            if (data.opiateHappiness < -80f) {
                data.opiateHappiness = -80f;
            }
        }

        if (currentOpiateReception > 30f) {
            data.addEnergy(-currentOpiateReception * 0.0025f * Util.TICK_TO_SEC);
        }
        if (currentOpiateReception > 5f && data.happiness() > -60f) {
            data.addHappiness(currentOpiateReception * 3E-05f * Util.TICK_TO_SEC);
        }

        if (currentOpiateReception < -16) {
            data.addSickness(0.09f * Util.TICK_TO_SEC);
        }
        if (currentOpiateReception < -15 && data.isSleeping(player) && data.energy() > 45) {// && no sleeping pills
            data.wakeUp(player);
        }
        if (currentOpiateReception < -34) {
            data.tryStartFibrillation(true);
        }

        if (opiateAmount == 0 && opiateTolerance == 0) {
            data.opiateHappiness = 0;
        }
    }

    public void reset() {
        antagonistAmount = 0;
        opiateAmount = 0;
        opiateReception = 0;
        currentOpiateReception = 0;
        opiateTolerance = 0;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putFloat("antagonistAmount", antagonistAmount);
        tag.putFloat("opiateAmount", opiateAmount);
        tag.putFloat("opiateReception", opiateReception);
        tag.putFloat("currentOpiateReception", currentOpiateReception);
        tag.putFloat("opiateTolerance", opiateTolerance);
        return tag;
    }

    public void load(CompoundTag tag) {
        antagonistAmount = tag.getFloat("antagonistAmount");
        opiateAmount = tag.getFloat("opiateAmount");
        opiateReception = tag.getFloat("opiateReception");
        currentOpiateReception = tag.getFloat("currentOpiateReception");
        opiateTolerance = tag.getFloat("opiateTolerance");
    }
}
