package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.util.Util;

public class LimbStatistics {

    private static final float dislocationHealSpeed = 0.07f;
    private static final float boneHealSpeed = 0.043f;
    private static final float muscleDeathThreshold = 5;

    private final PlayerHealthData data;

    private float skinHealth = 100f;
    private float skinHealAmount;
    private float muscleHealth = 100f;
    private float burn = 0;
    private float disinfectionTime = 0f;
    private int infectionCheck;
    private float infection = 0f;//
    private float boneHealTimer = 0f;//
    private float dislocationTimer = 0f;
    private int shrapnel = 0;//
    /// THIS IS PER TICK for whateve reason
    private float bleedRate = 0f;
    private float bandageSlowAmount;
    private float pain = 0f;//
    private boolean hasSplint = false;//
    private boolean tourniquet = false;
    private int tourniquetTimer = 0;

    private boolean amputated = false;
    private float regrowthProgress = 0;

    private boolean syncNeeded = true;
    private boolean softSyncNeeded = true;

    LimbStatistics(PlayerHealthData data){
        this.data = data;
    }

    public float getSkinHealth() {
        return skinHealth;
    }

    public void addSkinHealth(float skinHealth) {
        setSkinHealth(this.skinHealth + skinHealth);
    }

    public void setSkinHealth(float skinHealth) {
        if (amputated) return;

        skinHealth = Mth.clamp(skinHealth, 0, 100);
        if (this.skinHealth == skinHealth) return;

        this.skinHealth = skinHealth;
        syncNeeded = true;
    }

    public float skinHealAmount() {
        return skinHealAmount;
    }

    public void addSkinHealAmount(float skinHealAmount) {
        if (amputated) return;

        this.skinHealAmount += skinHealAmount;
        syncNeeded = true;
    }

    public void setSkinHeal(boolean skinHeal) {
    }

    public float getMuscleHealth() {
        return muscleHealth;
    }

    public void addMuscleHealth(float muscleHealth) {
        setMuscleHealth(this.muscleHealth + muscleHealth);
    }

    public void setMuscleHealth(float muscleHealth) {
        if (amputated) return;

        muscleHealth = Mth.clamp(muscleHealth, 0, 100);
        if (this.muscleHealth == muscleHealth) return;

        this.muscleHealth = muscleHealth;
        syncNeeded = true;
    }

    public void setMuscleHeal(boolean muscleHeal) {
    }

    public float getBurn() {
        return burn;
    }

    public void addBurn(float burn) {
        setBurn(this.burn + burn);
    }

    public void setBurn(float burn) {
        if (amputated) return;

        burn = Mth.clamp(burn, 0, 100);
        if (this.burn == burn) return;

        this.burn = burn;
        syncNeeded = true;
    }

    public float getPain() {
        return pain;
    }

    public void addPain(float pain) {
        setPain(this.pain + pain);
    }

    public void setPain(float pain) {
        if (amputated) return;

        if (this.pain == pain) return;

        this.pain = pain;
        syncNeeded = true;
    }

    public float getInfection() {
        return infection;
    }

    public void addInfection(float infection) {
        setInfection(this.infection + infection);
    }

    public void setInfection(float infection) {
        if (amputated) return;

        infection = Mth.clamp(infection, 0, 100);
        if (this.infection == infection) return;

        this.infection = infection;
        syncNeeded = true;
    }

    public float getBoneHealTimer() {
        return boneHealTimer;
    }

    public void addBoneHealTimer(float fracture) {
        setBoneHealTimer(this.boneHealTimer + fracture);
    }

    public void setBoneHealTimer(float boneHealTimer) {
        if (amputated) return;

        boneHealTimer = Mth.clamp(boneHealTimer, 0, 100);
        if (this.boneHealTimer == boneHealTimer) return;

        this.boneHealTimer = boneHealTimer;
        syncNeeded = true;
    }

    public float getDislocationTimer() {
        return dislocationTimer;
    }

    public void addDislocationTimer(float dislocation) {
        setDislocationTimer(this.dislocationTimer + dislocation);
    }

    public void setDislocationTimer(float dislocationTimer) {
        if (amputated) return;

        dislocationTimer = Mth.clamp(dislocationTimer, 0, 100);
        if (this.dislocationTimer == dislocationTimer) return;

        this.dislocationTimer = dislocationTimer;
        syncNeeded = true;
    }

    public int getShrapnel() {
        return shrapnel;
    }

    public void addShrapnel(int shrapnel) {
        setShrapnel(this.shrapnel + shrapnel);
    }

    public void setShrapnel(int shrapnel) {
        if (amputated) return;

        shrapnel = Mth.clamp(shrapnel, 0, 5);
        if (this.shrapnel == shrapnel) return;

        this.shrapnel = shrapnel;
        syncNeeded = true;
    }

    public float getBleedRate() {
        return bleedRate;
    }

    public void addBleedRate(float bleedRate) {
        setBleedRate(this.bleedRate + bleedRate);
    }

    public void setBleedRate(float bleedRate) {
        if (amputated) return;

        bleedRate = Mth.clamp(bleedRate, 0, data.getMAX_BLEED_RATE() *  (1 - getSkinHealth() / 100));
        if (this.bleedRate == bleedRate) return;

        this.bleedRate = bleedRate;
        syncNeeded = true;
    }

    public float bandageSlowAmount() {
        return bandageSlowAmount;
    }

    public void addBandageSlowAmount(float bandageSlowAmount) {
        if (amputated) return;

        this.bandageSlowAmount = bandageSlowAmount;
        syncNeeded = true;
    }

    public boolean hasSplint() {
        return hasSplint;
    }

    public void setHasSplint(boolean hasSplint) {
        if (amputated || this.hasSplint == hasSplint) return;

        this.hasSplint = hasSplint;
        syncNeeded = true;
    }

    public float getDisinfectionTime() {
        return disinfectionTime;
    }

    public void addDisinfectionTimer(float disinfectionTimer) {
        setDisinfectionTime(this.disinfectionTime + disinfectionTimer);
    }

    public void setDisinfectionTimerAtLeast(float disinfectionTimer) {
        setDisinfectionTime(Math.max(this.disinfectionTime, disinfectionTimer));
    }

    public void setDisinfectionTime(float disinfectionTime) {
        if (amputated || this.disinfectionTime == disinfectionTime) return;

        this.disinfectionTime = disinfectionTime;
        syncNeeded = true;
    }

    public boolean isTourniquet() {
        return tourniquet;
    }

    public void setTourniquet(boolean tourniquet) {
        if (amputated || this.tourniquet == tourniquet) return;

        this.tourniquet = tourniquet;
        if (!tourniquet) tourniquetTimer = 0;
        syncNeeded = true;
    }

    public int getTourniquetTimer() {
        return tourniquetTimer;
    }

    public void setTourniquetTimer(int tourniquetTimer) {//TODO TourniquetTimer
        if (amputated || !tourniquet || this.tourniquetTimer == tourniquetTimer) return;

        this.tourniquetTimer = tourniquetTimer;
        syncNeeded = true;
    }

    public float bleedSpeedMult() {
        return data.bleedingSpeedMultiplier();
    }

    public boolean isAmputated() {
        return amputated;
    }

    public void setAmputated(boolean amputated) {
        if (this.amputated == amputated) return;

        this.amputated = amputated;
        syncNeeded = true;
        softSyncNeeded = true;

        if (!amputated) return;

        skinHealth = 0;
        muscleHealth = 0;
        burn = 0;
        pain = 0;
        infection = 0;
        boneHealTimer = 0;
        dislocationTimer = 0;
        shrapnel = 0;
        bleedRate = 0;
        disinfectionTime = 0;

        hasSplint = false;//TODO drop splint / tourniquet if any present - need player context
        tourniquet = false;
        tourniquetTimer = 0;

        bandageSlowAmount = 0;
        skinHealAmount = 0;
        infectionCheck = 0;
    }

    public float getRegrowthProgress() {
        return regrowthProgress;
    }

    public void progressRegrowth(float amount) {
        if (!amputated || !ServerConfig.LIMB_REGROWTH.get()) return;

        regrowthProgress += amount;
        if (regrowthProgress >= ServerConfig.LIMB_REGROWTH_DURATION.get()) {
            setAmputated(false);
            regrowthProgress = 0;
        }
    }

    boolean sync() {
        if (!syncNeeded) return false;

        syncNeeded = false;
        return true;
    }

    boolean softSync() {
        if (!softSyncNeeded) return false;

        softSyncNeeded = false;
        return true;
    }

    void update(ServerPlayer player, Limb limb) {
        if (isAmputated()) return;

        if (burn >= 100 && ServerConfig.PERMANENT_DAMAGE.get()) {
            data.dismember(limb);
            return;
        }

        if (burn > 0) burn -= Math.min(burn, 0.1f * Util.TICK_TO_SEC);// 0.1/s -> 100 to 0 in ~16min

        data.setBloodVolume(data.getBloodVolume() - bleedRate);

        float newPain = 15 - skinHealth * 0.15f + infection * 0.1f;
        pain = Mth.clamp(Util.moveTowards(pain > newPain ? Util.TICK_TO_SEC : Util.TICK_TO_SEC * 0.6f, pain, newPain),0 ,100);
        if (data.getTemperature() < 32) {
            pain -= Util.TICK_TO_SEC * 5;
        }

        dislocationTimer -= Util.TICK_TO_SEC * dislocationHealSpeed * ServerConfig.HEALING_RATE.get().floatValue();
        boneHealTimer -= Util.TICK_TO_SEC * boneHealSpeed * ServerConfig.HEALING_RATE.get().floatValue();

        //CU uses dislocated/broken booleans which is effectively respective healTimer > 0

        if (shrapnel == 0 && bleedRate > 0 && data.getVenom() < 20) {
            /*mul twice as bleed is per tick TODO make per second or smth*/
            addBleedRate(-Util.TICK_TO_SEC * Util.TICK_TO_SEC * Util.CUBloodPointsToL(data.bleedClottingSpeed() * ServerConfig.HEALING_RATE.get().floatValue() * bleedSpeedMult()));
            if (bandageSlowAmount > 0) addBleedRate(-Util.TICK_TO_SEC * Util.TICK_TO_SEC * Util.CUBloodPointsToL(1.25f * bleedSpeedMult()));
        }

        bandageSlowAmount = Math.max(bandageSlowAmount - 1.25f * Util.TICK_TO_SEC, 0);
        skinHealAmount = Math.max(skinHealAmount - 0.5f * Util.TICK_TO_SEC, 0);
        disinfectionTime = Math.max(disinfectionTime - ServerConfig.INFECTION_RATE.get().floatValue() * Util.TICK_TO_SEC, 0);

        if (infection <= 0 && skinHealth < 80) {
            infectionCheck--;
            if (infectionCheck <= 0) {//40 seconds
                infectionCheck = 40 * 20;
                float f = Mth.lerp(0.77f, skinHealth * 0.01f / 0.8f, 1) - (bleedRate * 20 / bleedSpeedMult()) * 0.007f;//convert L/t to CU points
                if (player.getRandom().nextFloat() * ServerConfig.INFECTION_CHANCE.get().floatValue() < 1 - f) infection = 0.1f;
            }
        }

        if (infection > 0) {
            float infectionSpeed = infectionSpeed();
            infection += infectionSpeed * Util.TICK_TO_SEC / 60 * ServerConfig.INFECTION_RATE.get().floatValue();

            if (data.getTemperature() < 40.5f) {
                data.addTemperature(0.02f * Util.TICK_TO_SEC);
            }

            infection = Mth.clamp(infection, 0, 100);
        }

        if (infection > 90) {
            LimbStatistics stats;
            for (Limb connected : limb.getConnectedLimbs()) {
                stats = data.getLimb(connected);
                if (stats.getInfection() > 0) continue;

                stats.setInfection(0.1f);
            }
        }

        if (infection > 75) {
            addMuscleHealth(-0.2f * Util.TICK_TO_SEC);
        } else if ((infection <= 0 || limb == Limb.HEAD) && data.getCUHunger(player) > 0) {
            addMuscleHealth(muscleHealRate(player, limb));

            if (infection <= 0) {
                addSkinHealth(skinHealRate(player));
            }
        }

        if (muscleHealth <= muscleDeathThreshold) {
            if (limb == Limb.CHEST) {
                data.respiratoryRate(0);
                data.setInternalBleeding(data.getInternalBleeding() + 0.2f * Util.TICK_TO_SEC);
            }
        }

        if ((dislocationTimer > 0 || boneHealTimer > 0) && muscleHealth > 50) {
            muscleHealth = 50;
        }

        if (skinHealAmount > 0) {
            addSkinHealth(0.5f * Util.TICK_TO_SEC);
            addBurn(-0.5f * Util.TICK_TO_SEC);
        }

        if (data.getBloodOxygen() <= 5 || data.bloodPressure() < 20) {
            addMuscleHealth(-0.6f * Util.TICK_TO_SEC);
        }
    }

    float infectionSpeed() {
        float infectionSpeed = data.currentImmunityMult();
        if (disinfectionTime > 0) {
            infectionSpeed -= 1.7f;
        }
        return 7.2f * infectionSpeed;
    }

    float muscleHealRate(ServerPlayer player, Limb limb) {
        if (burn > 25) return 0;

        return Util.TICK_TO_SEC * 0.08f * (player.isSleeping() ? 1.4f : 1) * ((muscleHealth > 10 || limb == Limb.HEAD) ? 1 : 0.25f)
                * (shrapnel > 0 ? (limb != Limb.HEAD || muscleHealth > 14.28571f) ? 0 : 1 : 1) * data.hungerLimbHealCurrent() * ServerConfig.HEALING_RATE.get().floatValue();
    }

    float skinHealRate(ServerPlayer player) {
        if (burn > 25) return 0;

        if (shrapnel > 0) return 0;
        return Util.TICK_TO_SEC * 0.055f * (player.isSleeping() ? 1.4f : 1) * (skinHealth > 10 ? 1 : 0.25f)
                * (bleedRate * 20 > Util.CUBloodPointsToL(bleedSpeedMult()) ? 0.2f : 1) * data.hungerLimbHealCurrent() * ServerConfig.HEALING_RATE.get().floatValue();
    }

    void save(CompoundTag tag) {
        tag.putBoolean("Amputated", amputated);
        if (amputated) {
            if (regrowthProgress > 0) tag.putFloat("RegrowthProgress", regrowthProgress);
            return;
        }

        tag.putFloat("SkinHealth", skinHealth);
        tag.putFloat("MuscleHealth", muscleHealth);
        tag.putFloat("Burn", burn);
        tag.putFloat("Pain", pain);
        tag.putFloat("Infection", infection);
        tag.putFloat("FractureTimer", boneHealTimer);
        tag.putFloat("Dislocated", dislocationTimer);
        tag.putInt("Shrapnel", shrapnel);
        tag.putBoolean("HasSplint", hasSplint);
        tag.putFloat("BleedRate", bleedRate);
        tag.putFloat("DisinfectionTimer", disinfectionTime);
        tag.putBoolean("Tourniquet", tourniquet);
        tag.putInt("TourniquetTime", tourniquetTimer);

        tag.putFloat("bandageSlowAmount", bandageSlowAmount);
        tag.putFloat("skinHealAmount", skinHealAmount);
        tag.putInt("infectionCheck", infectionCheck);
    }

    void load(CompoundTag tag) {
        setAmputated(tag.getBoolean("Amputated"));
        if (amputated) {
            regrowthProgress = tag.getFloat("RegrowthProgress");
            return;
        }

        skinHealth = tag.contains("SkinHealth") ? tag.getFloat("SkinHealth") : 100;
        muscleHealth = tag.contains("MuscleHealth") ? tag.getFloat("MuscleHealth") : 100;
        burn = tag.getFloat("Burn");
        pain = tag.getFloat("Pain");
        infection = tag.getFloat("Infection");
        boneHealTimer = tag.getFloat("FractureTimer");
        dislocationTimer = tag.getFloat("Dislocated");
        shrapnel = tag.getInt("Shrapnel");
        hasSplint = tag.getBoolean("HasSplint");
        bleedRate = tag.getFloat("BleedRate");
        disinfectionTime = tag.getFloat("DisinfectionTimer");
        tourniquet = tag.getBoolean("Tourniquet");
        tourniquetTimer = tag.getInt("TourniquetTime");

        bandageSlowAmount = tag.getFloat("bandageSlowAmount");
        skinHealAmount = tag.getFloat("skinHealAmount");
        infectionCheck = tag.getInt("infectionCheck");
    }

    @Override
    public String toString() {
        return "LimbStatistics{" +
                "skinHealth=" + skinHealth +
                ", muscleHealth=" + muscleHealth +
                ", burn=" + burn +
                ", pain=" + pain +
                ", infection=" + infection +
                ", fractureTimer=" + boneHealTimer +
                ", dislocatedTimer=" + dislocationTimer +
                ", shrapnell=" + shrapnel +
                ", hasSplint=" + hasSplint +
                ", bleedRate=" + bleedRate +
                ", disinfectionTimer=" + disinfectionTime +
                ", skinHealAmount=" + skinHealAmount +
                ", tourniquet=" + tourniquet +
                ", tourniquetTimer=" + tourniquetTimer +
                '}';
    }
}
