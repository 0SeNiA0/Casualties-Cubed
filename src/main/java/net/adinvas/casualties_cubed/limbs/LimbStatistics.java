package net.adinvas.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;

public class LimbStatistics {

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
        this.skinHealth = skinHealth;
    }

    public float getMuscleHealth() {
        return muscleHealth;
    }

    public void setMuscleHealth(float muscleHealth) {
        this.muscleHealth = muscleHealth;
    }

    public float getPain() {
        return pain;
    }

    public void setPain(float pain) {
        this.pain = pain;
    }

    public float getInfection() {
        return infection;
    }

    public void setInfection(float infection) {
        this.infection = infection;
    }

    public float getFracture() {
        return fracture;
    }

    public void setFracture(float fracture) {
        this.fracture = fracture;
    }

    public float getDislocation() {
        return dislocation;
    }

    public void setDislocation(float dislocation) {
        this.dislocation = dislocation;
    }

    public int getShrapnel() {
        return shrapnel;
    }

    public void setShrapnel(int shrapnel) {
        this.shrapnel = shrapnel;
    }

    public float getBleedRate() {
        return bleedRate;
    }

    public void setBleedRate(float bleedRate) {
        this.bleedRate = bleedRate;
    }

    public boolean isHasSplint() {
        return hasSplint;
    }

    public void setHasSplint(boolean hasSplint) {
        this.hasSplint = hasSplint;
    }

    public float getDisinfectionTimer() {
        return desinfectionTimer;
    }

    public void setDisinfectionTimer(float disinfectionTimer) {
        this.desinfectionTimer = disinfectionTimer;
    }

    public float getMinPain() {
        return minPain;
    }

    public void setMinPain(float minPain) {
        this.minPain = minPain;
    }

    public float getFinalPain() {
        return finalPain;
    }

    public void setFinalPain(float finalPain) {
        this.finalPain = finalPain;
    }

    public boolean isSkinHeal() {
        return skinHeal;
    }

    public void setSkinHeal(boolean skinHeal) {
        this.skinHeal = skinHeal;
    }

    public boolean isMuscleHeal() {
        return muscleHeal;
    }

    public void setMuscleHeal(boolean muscleHeal) {
        this.muscleHeal = muscleHeal;
    }

    public boolean isTourniquet() {
        return tourniquet;
    }

    public void setTourniquet(boolean tourniquet) {
        this.tourniquet = tourniquet;
    }

    public int getTourniquetTimer() {
        return tourniquetTimer;
    }

    public void setTourniquetTimer(int tourniquetTimer) {
        this.tourniquetTimer = tourniquetTimer;
    }

    public boolean isAmputated() {
        return amputated;
    }

    public void setAmputated(boolean amputated) {
        this.amputated = amputated;
    }

    public float getRegrowthProgress() {
        return regrowthProgress;
    }

    public void setRegrowthProgress(float regrowthProgress) {
        this.regrowthProgress = regrowthProgress;
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
        tag.putBoolean("Amputated", amputated);
        tag.putFloat("RegrowthProgress", regrowthProgress);
    }

    void load(CompoundTag tag) {
        if (tag.contains("SkinHealth")) skinHealth = tag.getFloat("SkinHealth");
        if (tag.contains("MuscleHealth")) muscleHealth = tag.getFloat("MuscleHealth");
        if (tag.contains("Pain")) pain = tag.getFloat("Pain");
        if (tag.contains("Infection")) infection = tag.getFloat("Infection");
        if (tag.contains("FractureTimer")) fracture = tag.getFloat("FractureTimer");
        if (tag.contains("Dislocated")) dislocation = tag.getFloat("Dislocated");
        if (tag.contains("Shrapnell")) shrapnel = tag.getInt("Shrapnell");
        if (tag.contains("HasSplint")) hasSplint = tag.getBoolean("HasSplint");
        if (tag.contains("BleedRate")) bleedRate = tag.getFloat("BleedRate");
        if (tag.contains("DesinfectionTimer")) desinfectionTimer = tag.getFloat("DesinfectionTimer");
        if (tag.contains("MinPain")) minPain = tag.getFloat("MinPain");
        if (tag.contains("FinalPain")) finalPain = tag.getFloat("FinalPain");
        if (tag.contains("SkinHeal")) skinHeal = tag.getBoolean("SkinHeal");
        if (tag.contains("MuscleHeal")) muscleHeal = tag.getBoolean("MuscleHeal");
        if (tag.contains("Tourniquet")) tourniquet = tag.getBoolean("Tourniquet");
        if (tag.contains("TourniquetTime")) tourniquetTimer = tag.getInt("TourniquetTime");
        if (tag.contains("Amputated")) amputated = tag.getBoolean("Amputated");
        if (tag.contains("RegrowthProgress")) regrowthProgress = tag.getFloat("RegrowthProgress");

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
