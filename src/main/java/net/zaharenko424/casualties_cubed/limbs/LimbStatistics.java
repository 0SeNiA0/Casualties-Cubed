package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.zaharenko424.casualties_cubed.config.ServerConfig;

public class LimbStatistics {

    public static final float MAX_REGROWTH_PROGRESS = 20 * 60;

    float skinHealth = 100f;//
    float muscleHealth = 100f;//
    float pain = 0f;//
    float infection = 0f;//
    float fracture = 0f;//
    float dislocation = 0f;
    int shrapnel = 0;//
    boolean hasSplint = false;//
    float bleedRate = 0f;//
    float desinfectionTimer = 0f;//
    float minPain = 0f;//
    float finalPain = 0f;
    boolean skinHeal = false;
    boolean muscleHeal = false;
    boolean tourniquet = false;
    int tourniquetTimer = 0;
    boolean amputated = false;
    float regrowthProgress = 0;

    LimbStatistics(){}

    public float getSkinHealth() {
        return skinHealth;
    }

    public void setSkinHealth(float skinHealth) {
        if (amputated) return;
        this.skinHealth = skinHealth;
    }

    public float getMuscleHealth() {
        return muscleHealth;
    }

    public void setMuscleHealth(float muscleHealth) {
        if (amputated) return;
        this.muscleHealth = muscleHealth;
    }

    public float getPain() {
        return pain;
    }

    public void setPain(float pain) {
        if (amputated) return;
        this.pain = pain;
    }

    public float getInfection() {
        return infection;
    }

    public void setInfection(float infection) {
        if (amputated) return;
        this.infection = infection;
    }

    public float getFracture() {
        return fracture;
    }

    public void setFracture(float fracture) {
        if (amputated) return;
        this.fracture = fracture;
    }

    public float getDislocation() {
        return dislocation;
    }

    public void setDislocation(float dislocation) {
        if (amputated) return;
        this.dislocation = dislocation;
    }

    public int getShrapnel() {
        return shrapnel;
    }

    public void setShrapnel(int shrapnel) {
        if (amputated) return;
        this.shrapnel = shrapnel;
    }

    public float getBleedRate() {
        return bleedRate;
    }

    public void setBleedRate(float bleedRate) {
        if (amputated) return;
        this.bleedRate = bleedRate;
    }

    public boolean isHasSplint() {
        return hasSplint;
    }

    public void setHasSplint(boolean hasSplint) {
        if (amputated) return;
        this.hasSplint = hasSplint;
    }

    public float getDisinfectionTimer() {
        return desinfectionTimer;
    }

    public void setDisinfectionTimer(float disinfectionTimer) {
        if (amputated) return;
        this.desinfectionTimer = disinfectionTimer;
    }

    public float getMinPain() {
        return minPain;
    }

    public void setMinPain(float minPain) {
        if (amputated) return;
        this.minPain = minPain;
    }

    public float getFinalPain() {
        return finalPain;
    }

    public void setFinalPain(float finalPain) {
        if (amputated) return;
        this.finalPain = finalPain;
    }

    public boolean isSkinHeal() {
        return skinHeal;
    }

    public void setSkinHeal(boolean skinHeal) {
        if (amputated) return;
        this.skinHeal = skinHeal;
    }

    public boolean isMuscleHeal() {
        return muscleHeal;
    }

    public void setMuscleHeal(boolean muscleHeal) {
        if (amputated) return;
        this.muscleHeal = muscleHeal;
    }

    public boolean isTourniquet() {
        return tourniquet;
    }

    public void setTourniquet(boolean tourniquet) {
        if (amputated) return;
        this.tourniquet = tourniquet;
    }

    public int getTourniquetTimer() {
        return tourniquetTimer;
    }

    public void setTourniquetTimer(int tourniquetTimer) {
        if (amputated) return;
        this.tourniquetTimer = tourniquetTimer;
    }

    public boolean isAmputated() {
        return amputated;
    }

    public void setAmputated(boolean amputated) {
        if (this.amputated == amputated) return;
        this.amputated = amputated;

        if (!amputated) return;

        skinHealth = 0;
        muscleHealth = 0;
        pain = 0;
        infection = 0;
        fracture = 0;
        dislocation = 0;
        shrapnel = 0;
        hasSplint = false;
        bleedRate = 0;
        desinfectionTimer = 0;
        minPain = 0;
        finalPain = 0;
        skinHeal = false;
        muscleHeal = false;
        tourniquet = false;
        tourniquetTimer = 0;
    }

    public float getRegrowthProgress() {
        return regrowthProgress;
    }

    public void progressRegrowth(float amount) {
        if (!ServerConfig.LIMB_REGROWTH.get() || !amputated) return;

        regrowthProgress += amount;
        if (regrowthProgress >= MAX_REGROWTH_PROGRESS) {
            amputated = false;
            regrowthProgress = 0;
        }
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
        desinfectionTimer = other.desinfectionTimer;
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
            if (regrowthProgress > 0)tag.putFloat("RegrowthProgress", regrowthProgress);
            return;
        }

        tag.putFloat("SkinHealth", skinHealth);
        tag.putFloat("MuscleHealth", muscleHealth);
        tag.putFloat("Pain", pain);
        tag.putFloat("Infection", infection);
        tag.putFloat("FractureTimer", fracture);
        tag.putFloat("Dislocated", dislocation);
        tag.putInt("Shrapnell", shrapnel);
        tag.putBoolean("HasSplint", hasSplint);
        tag.putFloat("BleedRate", bleedRate);
        tag.putFloat("DesinfectionTimer", desinfectionTimer);
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
        shrapnel = tag.getInt("Shrapnell");
        hasSplint = tag.getBoolean("HasSplint");
        bleedRate = tag.getFloat("BleedRate");
        desinfectionTimer = tag.getFloat("DesinfectionTimer");
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
                ", desinfectionTimer=" + desinfectionTimer +
                ", minPain=" + minPain +
                ", finalPain=" + finalPain +
                ", skinHeal=" + skinHeal +
                ", muscleHeal=" + muscleHeal +
                ", tourniquet=" + tourniquet +
                ", tourniquetTimer=" + tourniquetTimer +
                '}';
    }
}
