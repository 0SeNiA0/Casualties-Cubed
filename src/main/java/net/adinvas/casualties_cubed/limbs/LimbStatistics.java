package net.adinvas.casualties_cubed.limbs;

public class LimbStatistics {

    public float skinHealth = 100f;//
    public float muscleHealth = 100f;//
    public float pain = 0f;//
    public float infection = 0f;//
    public float fracture = 0f;//
    public float dislocation = 0f;
    public int shrapnell = 0;//
    public boolean hasSplint = false;//
    public float bleedRate = 0f;//
    public float desinfectionTimer = 0f;//
    public float MinPain = 0f;//
    public float finalPain = 0f;
    public boolean SkinHeal = false;
    public boolean MuscleHeal = false;
    public boolean Tourniquet = false;
    public int tourniquetTimer = 0;
    public boolean amputated = false;


    public LimbStatistics(){
    }

    public boolean isAmputated() {
        return amputated;
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

    public void setFromShrapnelType(){

    }
}
