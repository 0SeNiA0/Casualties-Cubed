package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import org.apache.commons.lang3.BooleanUtils;

public class LimbStatistics {

    private final PlayerHealthData data;

    private float skinHealth = 100f;//
    private boolean skinHeal = false;
    private float muscleHealth = 100f;//
    private boolean muscleHeal = false;
    private float burn = 0;
    private float infection = 0f;//
    private float fracture = 0f;//
    private float dislocation = 0f;
    private int shrapnel = 0;//
    private float bleedRate = 0f;//
    private float disinfectionTimer = 0f;//
    private float minPain = 0f;//
    private float pain = 0f;//
    private float finalPain = 0f;
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

    public boolean isSkinHeal() {
        return skinHeal;
    }

    public void setSkinHeal(boolean skinHeal) {
        if (amputated || this.skinHeal == skinHeal) return;

        this.skinHeal = skinHeal;
        syncNeeded = true;
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

    public boolean isMuscleHeal() {
        return muscleHeal;
    }

    public void setMuscleHeal(boolean muscleHeal) {
        if (amputated || this.muscleHeal == muscleHeal) return;

        this.muscleHeal = muscleHeal;
        syncNeeded = true;
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

        pain = Math.max(minPain, pain);
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

    public float getFracture() {
        return fracture;
    }

    public void addFracture(float fracture) {
        setFracture(this.fracture + fracture);
    }

    public void setFracture(float fracture) {
        if (amputated) return;

        fracture = Mth.clamp(fracture, 0, 100);
        if (this.fracture == fracture) return;

        this.fracture = fracture;
        syncNeeded = true;
    }

    public float getDislocation() {
        return dislocation;
    }

    public void addDislocation(float dislocation) {
        setDislocation(this.dislocation + dislocation);
    }

    public void setDislocation(float dislocation) {
        if (amputated) return;

        dislocation = Mth.clamp(dislocation, 0, 100);
        if (this.dislocation == dislocation) return;

        this.dislocation = dislocation;
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
        if (amputated || this.bleedRate == bleedRate) return;

        this.bleedRate = bleedRate;
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

    public float getDisinfectionTimer() {
        return disinfectionTimer;
    }

    public void addDisinfectionTimer(float disinfectionTimer) {
        setDisinfectionTimer(this.disinfectionTimer + disinfectionTimer);
    }

    public void setDisinfectionTimerAtLeast(float disinfectionTimer) {
        setDisinfectionTimer(Math.max(this.disinfectionTimer, disinfectionTimer));
    }

    public void setDisinfectionTimer(float disinfectionTimer) {
        if (amputated || this.disinfectionTimer == disinfectionTimer) return;

        this.disinfectionTimer = disinfectionTimer;
        syncNeeded = true;
    }

    public float getMinPain() {
        return minPain;
    }

    public void addMinPain(float minPain) {
        setMinPain(this.minPain + minPain);
    }

    public void setMinPain(float minPain) {
        if (amputated || this.minPain == minPain) return;

        this.minPain = minPain;
        syncNeeded = true;
    }

    public float getFinalPain() {
        return finalPain;
    }

    public void addFinalPain(float finalPain) {
        setFinalPain(this.finalPain + finalPain);
    }

    public void setFinalPain(float finalPain) {
        if (amputated || this.finalPain == finalPain) return;

        this.finalPain = finalPain;
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

    public void addTourniquetTimer(int tourniquetTimer) {
        setTourniquetTimer(this.tourniquetTimer + tourniquetTimer);
    }

    public void setTourniquetTimer(int tourniquetTimer) {
        if (amputated || !tourniquet || this.tourniquetTimer == tourniquetTimer) return;

        this.tourniquetTimer = tourniquetTimer;
        syncNeeded = true;
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
        pain = 0;
        infection = 0;
        fracture = 0;
        dislocation = 0;
        shrapnel = 0;
        bleedRate = 0;
        disinfectionTimer = 0;
        minPain = 0;
        finalPain = 0;
        skinHeal = false;
        muscleHeal = false;

        hasSplint = false;//TODO drop splint / tourniquet if any present - need player context
        tourniquet = false;
        tourniquetTimer = 0;
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

    void tick(Limb limb) {
        if (isAmputated()) return;

        //MinpainCalculation

        setMinPain(((getInfection() / 100) * 10) + (((getSkinHealth() - 100) / -100) * 15));

        //Healing
        if (burn < 25) {
            if (isSkinHeal() && getShrapnel() <= 0) {
                addSkinHealth((ServerConfig.BOOSTED_LIMB_HEAL_RATE.get().floatValue() / 20f));
            } else {
                addSkinHealth((ServerConfig.NORMAL_LIMB_HEAL_RATE.get().floatValue() / 20f));
            }

            if (isMuscleHeal() && getShrapnel() <= 0 && getInfection() <= 0) {
                addMuscleHealth((ServerConfig.BOOSTED_LIMB_HEAL_RATE.get().floatValue() / 20f));
            } else if (getShrapnel() <= 0 && getInfection() <= 0) {
                addMuscleHealth((ServerConfig.NORMAL_LIMB_HEAL_RATE.get().floatValue() / 20f));
            }
        }

        if (burn >= 100 && ServerConfig.PERMANENT_DAMAGE.get()) {
            data.dismember(limb);
            return;
        }

        burn -= .05f;// 1/s

        // Pain Adjustment
        float x = getPain() / 100f;
        float decay = 0.05f + 0.1f * (float) Math.pow(x, 1.2f);
        if (isTourniquet()) {
            if (getPain() > 60) {
                addPain(-decay * (1 + (data.getNetOpioids() > 0 ? (data.getNetOpioids() / 40) : 0)));
            }
        } else {
            addPain(-decay * (1 + (data.getNetOpioids() > 0 ? (data.getNetOpioids() / 40) : 0)));
        }

        // Infection Adjustment
        calculateInfectionAndSpread(limb);
        if (getSkinHealth() < 100 && getInfection() <= 0) {
            float chance = ((100 - getSkinHealth()) / 100f) * (ServerConfig.INFECTION_CHANCE.get().floatValue() / 20f);
            if (Math.random() < chance) {
                addInfection(1);
            }
        }

        // Bleed Adjustment
        setBleedRate(Math.max(0, Math.min(getBleedRate(), data.getMAX_BLEED_RATE() * (Math.abs((getSkinHealth() - 100) / 100)))));

        //Fract/Disl calculation
        if (getFracture() > 0 || getDislocation() > 0) {
            setMuscleHealth(Math.min(getMuscleHealth(), 50));
        }

        if (getFracture() > 0) {
            float reduction = (ServerConfig.FRACTURE_HEAL_RATE.get().floatValue() / 20) * (1 + BooleanUtils.toInteger(hasSplint()));
            setFracture(Mth.clamp(getFracture() - reduction, 0, 100));
        }
        if (getDislocation() > 0) {
            float reduction = (ServerConfig.DISLOCATION_HEAL_RATE.get().floatValue() / 20) * (1 + BooleanUtils.toInteger(hasSplint()));
            setDislocation(Mth.clamp(getDislocation() - reduction, 0, 100));
        }


        if (getInfection() >= 75) {
            addMuscleHealth(-(ServerConfig.INFECTION_MUSCLE_DRAIN.get().floatValue() / 20f));
        }


        if (getInfection() <= 0 && limb == Limb.HEAD && getMuscleHealth() < 15) {
            addMuscleHealth((ServerConfig.BOOSTED_LIMB_HEAL_RATE.get().floatValue() / 20f) * 3);
        }

        if (isTourniquet()) {
            // Pain ramps up towards 40
            if (getPain() < 60) {
                addPain(ServerConfig.TOURNIQUET_PAIN_PER_TICK.get().floatValue());
            }

            // Timer ticks up
            addTourniquetTimer(1);
            if (getTourniquetTimer() > ServerConfig.TOURNIQUET_SAFE_TICKS.get()) {
                float tourniquetMuscleDamage = (ServerConfig.TOURNIQUET_MUSCLE_DAMAGE.get().floatValue() / 20f);
                addMuscleHealth(-tourniquetMuscleDamage);
                switch (limb) {
                    case LEFT_ARM ->
                            data.getLimb(Limb.LEFT_HAND).addMuscleHealth(-tourniquetMuscleDamage);
                    case RIGHT_ARM ->
                            data.getLimb(Limb.RIGHT_HAND).addMuscleHealth(-tourniquetMuscleDamage);
                    case LEFT_LEG ->
                            data.getLimb(Limb.LEFT_FOOT).addMuscleHealth(-tourniquetMuscleDamage);
                    case RIGHT_LEG ->
                            data.getLimb(Limb.RIGHT_FOOT).addMuscleHealth(-tourniquetMuscleDamage);
                }
            }
        }

        setFinalPain(getPain());
    }

    private void calculateInfectionAndSpread(Limb limb) {
        float infection_progress = (float) ((data.getImmunity() * ServerConfig.IMMUNITY_SCALE.get()) * -0.001188f + 0.18f);
        if (getDisinfectionTimer() > 0) {
            infection_progress -= (0.125f * 20) * (ServerConfig.DISINFECTION_SCALE.get().floatValue() / 20f);
            addDisinfectionTimer(-1);
        }

        if (getInfection() <= 0) return;

        addInfection(infection_progress / 20);
        if (getInfection() < 75) return;

        float chance = (getInfection() - 75);
        if (Math.random() > chance) {
            LimbStatistics connectedLimb = data.getLimb(limb.randomFromConectedLimb());
            if (connectedLimb.getInfection() <= 0) {
                connectedLimb.addInfection(1);
            }
        }
    }

    void copyFrom(LimbStatistics other) {
        skinHealth = other.skinHealth;
        muscleHealth = other.muscleHealth;
        burn = other.burn;
        pain = other.pain;
        infection = other.infection;
        fracture = other.fracture;
        dislocation = other.dislocation;
        shrapnel = other.shrapnel;
        hasSplint = other.hasSplint;
        bleedRate = other.bleedRate;
        disinfectionTimer = other.disinfectionTimer;
        minPain = other.minPain;
        finalPain = other.finalPain;
        skinHeal = other.skinHeal;
        muscleHeal = other.muscleHeal;
        tourniquet = other.tourniquet;
        tourniquetTimer = other.tourniquetTimer;
        amputated = other.amputated;
        regrowthProgress = other.regrowthProgress;
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
        tag.putFloat("FractureTimer", fracture);
        tag.putFloat("Dislocated", dislocation);
        tag.putInt("Shrapnel", shrapnel);
        tag.putBoolean("HasSplint", hasSplint);
        tag.putFloat("BleedRate", bleedRate);
        tag.putFloat("DisinfectionTimer", disinfectionTimer);
        tag.putFloat("MinPain", minPain);
        tag.putFloat("FinalPain", finalPain);
        tag.putBoolean("SkinHeal", skinHeal);
        tag.putBoolean("MuscleHeal", muscleHeal);
        tag.putBoolean("Tourniquet", tourniquet);
        tag.putInt("TourniquetTime", tourniquetTimer);
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
        fracture = tag.getFloat("FractureTimer");
        dislocation = tag.getFloat("Dislocated");
        shrapnel = tag.getInt("Shrapnel");
        hasSplint = tag.getBoolean("HasSplint");
        bleedRate = tag.getFloat("BleedRate");
        disinfectionTimer = tag.getFloat("DisinfectionTimer");
        minPain = tag.getFloat("MinPain");
        finalPain = tag.getFloat("FinalPain");
        skinHeal = tag.getBoolean("SkinHeal");
        muscleHeal = tag.getBoolean("MuscleHeal");
        tourniquet = tag.getBoolean("Tourniquet");
        tourniquetTimer = tag.getInt("TourniquetTime");

        if (Float.isNaN(bleedRate)) bleedRate = 0;
    }

    @Override
    public String toString() {
        return "LimbStatistics{" +
                "skinHealth=" + skinHealth +
                ", muscleHealth=" + muscleHealth +
                ", burn=" + burn +
                ", pain=" + pain +
                ", infection=" + infection +
                ", fractureTimer=" + fracture +
                ", dislocatedTimer=" + dislocation +
                ", shrapnell=" + shrapnel +
                ", hasSplint=" + hasSplint +
                ", bleedRate=" + bleedRate +
                ", disinfectionTimer=" + disinfectionTimer +
                ", minPain=" + minPain +
                ", finalPain=" + finalPain +
                ", skinHeal=" + skinHeal +
                ", muscleHeal=" + muscleHeal +
                ", tourniquet=" + tourniquet +
                ", tourniquetTimer=" + tourniquetTimer +
                '}';
    }
}
