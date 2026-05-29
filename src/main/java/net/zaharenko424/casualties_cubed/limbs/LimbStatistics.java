package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.config.ServerConfig;

public class LimbStatistics {

    private float skinHealth = 100f;//
    private float muscleHealth = 100f;//
    private float infection = 0f;//
    private float fracture = 0f;//
    private float dislocation = 0f;
    private int shrapnel = 0;//
    private float bleedRate = 0f;//
    private float disinfectionTimer = 0f;//
    private float minPain = 0f;//
    private float pain = 0f;//
    private float finalPain = 0f;
    private boolean skinHeal = false;
    private boolean muscleHeal = false;
    private boolean hasSplint = false;//
    private boolean tourniquet = false;
    private int tourniquetTimer = 0;
    private boolean amputated = false;
    private float regrowthProgress = 0;

    private boolean syncNeeded = true;
    private boolean softSyncNeeded = true;

    LimbStatistics(){}

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

    public boolean isSkinHeal() {
        return skinHeal;
    }

    public void setSkinHeal(boolean skinHeal) {
        if (amputated || this.skinHeal == skinHeal) return;

        this.skinHeal = skinHeal;
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
        if (!ServerConfig.LIMB_REGROWTH.get() || !amputated) return;

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

    void copyFrom(LimbStatistics other) {
        skinHealth = other.skinHealth;
        muscleHealth = other.muscleHealth;
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
