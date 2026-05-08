package net.adinvas.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;

public class LimbStatistics {

    float skinHealth = 100f;//
    float muscleHealth = 100f;//
    float pain = 0f;//
    float infection = 0f;//
    float fracture = 0f;//
    float dislocation = 0f;
    int shrapnell = 0;//
    boolean hasSplint = false;//
    float bleedRate = 0f;//
    float desinfectionTimer = 0f;//
    float MinPain = 0f;//
    float finalPain = 0f;
    boolean SkinHeal = false;
    boolean MuscleHeal = false;
    boolean Tourniquet = false;
    int tourniquetTimer = 0;
    boolean amputated = false;
    float regrowthProgress = 0;

    LimbStatistics(){}

    public boolean isAmputated() {
        return amputated;
    }

    void copyFrom(LimbStatistics other) {
        skinHealth = other.skinHealth;
        muscleHealth = other.muscleHealth;
        pain = other.pain;
        infection = other.infection;
        fracture = other.fracture;
        dislocation = other.dislocation;
        shrapnell = other.shrapnell;
        hasSplint = other.hasSplint;
        bleedRate = other.bleedRate;
        desinfectionTimer = other.desinfectionTimer;
        MinPain = other.MinPain;
        finalPain = other.finalPain;
        SkinHeal = other.SkinHeal;
        MuscleHeal = other.MuscleHeal;
        Tourniquet = other.Tourniquet;
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
        tag.putInt("Shrapnell", shrapnell);
        tag.putBoolean("HasSplint", hasSplint);
        tag.putFloat("BleedRate", bleedRate);
        tag.putFloat("DesinfectionTimer", desinfectionTimer);
        tag.putFloat("MinPain", MinPain);
        tag.putFloat("FinalPain", finalPain);
        tag.putBoolean("SkinHeal", SkinHeal);
        tag.putBoolean("MuscleHeal", MuscleHeal);
        tag.putBoolean("Tourniquet", Tourniquet);
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
        if (tag.contains("Shrapnell")) shrapnell = tag.getInt("Shrapnell");
        if (tag.contains("HasSplint")) hasSplint = tag.getBoolean("HasSplint");
        if (tag.contains("BleedRate")) bleedRate = tag.getFloat("BleedRate");
        if (tag.contains("DesinfectionTimer")) desinfectionTimer = tag.getFloat("DesinfectionTimer");
        if (tag.contains("MinPain")) MinPain = tag.getFloat("MinPain");
        if (tag.contains("FinalPain")) finalPain = tag.getFloat("FinalPain");
        if (tag.contains("SkinHeal")) SkinHeal = tag.getBoolean("SkinHeal");
        if (tag.contains("MuscleHeal")) MuscleHeal = tag.getBoolean("MuscleHeal");
        if (tag.contains("Tourniquet")) Tourniquet = tag.getBoolean("Tourniquet");
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
                ", shrapnell=" + shrapnell +
                ", hasSplint=" + hasSplint +
                ", bleedRate=" + bleedRate +
                ", desinfectionTimer=" + desinfectionTimer +
                ", minPain=" + MinPain +
                ", finalPain=" + finalPain +
                ", skinHeal=" + SkinHeal +
                ", muscleHeal=" + MuscleHeal +
                ", tourniquet=" + Tourniquet +
                ", tourniquetTimer=" + tourniquetTimer +
                '}';
    }
}
