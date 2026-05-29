package net.zaharenko424.casualties_cubed.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue DISINFECTION_SCALE;
    public static final ForgeConfigSpec.DoubleValue WOUND_ANTIBLEED_RATE;
    public static final ForgeConfigSpec.DoubleValue INFECTION_CHANCE;
    public static final ForgeConfigSpec.DoubleValue INFECTION_MUSCLE_DRAIN;
    public static final ForgeConfigSpec.DoubleValue HEMOTHORAX_HEAL_RATE;

    // Oxygen changes
    public static final ForgeConfigSpec.DoubleValue OXYGEN_REPLENISH;
    public static final ForgeConfigSpec.DoubleValue OXYGEN_DRAIN;

    // Blood regen & bleeding
    public static final ForgeConfigSpec.DoubleValue BLOOD_REGEN_RATE;   // L per second
    public static final ForgeConfigSpec.DoubleValue MAX_BLEED_RATE;// L per second
    public static final ForgeConfigSpec.DoubleValue BLOOD_VISCOSITY_REGEN;

    // Damage scaling
    public static final ForgeConfigSpec.DoubleValue DAMAGE_SCALE;
    public static final ForgeConfigSpec.DoubleValue PAIN_PER_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue OPIATE_PAIN_REDUCTION;
    public static final ForgeConfigSpec.DoubleValue FRAC_DISL_FROM_MUSCLE_DAMAGE_CHANCE;
    public static final ForgeConfigSpec.DoubleValue FRACTURE_HEAL_RATE;
    public static final ForgeConfigSpec.DoubleValue DISLOCATION_HEAL_RATE;

    // Limb healing rates
    public static final ForgeConfigSpec.DoubleValue NORMAL_LIMB_HEAL_RATE;  // % per second
    public static final ForgeConfigSpec.DoubleValue BOOSTED_LIMB_HEAL_RATE;   // % per second
    public static final ForgeConfigSpec.DoubleValue MAGICAL_HEAL_RATE;

    // Tourniquet behavior
    public static final ForgeConfigSpec.DoubleValue TOURNIQUET_PAIN_PER_TICK;
    public static final ForgeConfigSpec.IntValue TOURNIQUET_SAFE_TICKS;             // 60s before muscle damage starts
    public static final ForgeConfigSpec.DoubleValue TOURNIQUET_MUSCLE_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue CONS_PENALTY_PER_OPIOID;
    public static final ForgeConfigSpec.DoubleValue CONSCIOUSNESS_REGEN;

    //Heavy Shit

    public static final ForgeConfigSpec.DoubleValue HELMET_ARMOR_SCALE;
    public static final ForgeConfigSpec.DoubleValue CHESTPLATE_ARMOR_SCALE;
    public static final ForgeConfigSpec.DoubleValue LEG_ARMOR_SCALE;
    public static final ForgeConfigSpec.DoubleValue BOOTS_ARMOR_SCALE;
    public static final ForgeConfigSpec.DoubleValue BRAIN_DRAIN;
    public static final ForgeConfigSpec.DoubleValue BRAIN_HEALTH_REGEN;
    public static final ForgeConfigSpec.DoubleValue IMMUNITY_SCALE;
    public static final ForgeConfigSpec.BooleanValue DO_TEMP_CHANGE;

    public static final ForgeConfigSpec.BooleanValue PERMANENT_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue LIMB_REGROWTH;
    public static final ForgeConfigSpec.IntValue LIMB_REGROWTH_MIN_REGEN;
    public static final ForgeConfigSpec.IntValue LIMB_REGROWTH_DURATION;

    public static final ForgeConfigSpec.BooleanValue PHYS_INTEGRATION;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("Prototype Pain Server Config");

        DISINFECTION_SCALE = BUILDER
                .comment("The scale of strength of Disinfectants")
                        .defineInRange("disinfectionScale",1d,0,100);

        WOUND_ANTIBLEED_RATE = BUILDER
                .comment("The rate at which internal Bleeding heals (L/min)")
                        .defineInRange("wundAntiBleed",0.000025,0,10);

        INFECTION_CHANCE=BUILDER
                .comment("Chance of an infection at 0% skin health (%/s)")
                        .defineInRange("infectionChance",0.001,0,1);
        INFECTION_MUSCLE_DRAIN = BUILDER
                .comment("The rate at which Infection Drains Muscle health(only above 80% infection) (%/s)")
                        .defineInRange("infectionMuscleDrain",0.4,0,100);
        HEMOTHORAX_HEAL_RATE = BUILDER
                .comment("The rate at which Hemothorax is healed (pts/s)")
                        .defineInRange("hemothoraxHealRate",0.05,0,100);
        OXYGEN_REPLENISH = BUILDER
                .comment("The rate at which Oxygen Replenishes (%/s)")
                        .defineInRange("oxygenReplenish",8d,0,100);
        OXYGEN_DRAIN = BUILDER
                .comment("The rate at which Oxygen Drains when not breathing (%/s)")
                        .defineInRange("oxygenDrain",5d,0,100);
        BLOOD_REGEN_RATE = BUILDER
                .comment("The rate at which Blood balances itself around 5L at full hunger (L/s)")
                        .defineInRange("bloodRegen",0.001,0,10);
        MAX_BLEED_RATE = BUILDER
                .comment("The Maximum Rate of Bleeding from one Limb (L/s)")
                        .defineInRange("maxBleedRate",0.016,0,10);
        BLOOD_VISCOSITY_REGEN = BUILDER
                .comment("The rate at which Blood viscosity returns to 0 (pts/s)")
                        .defineInRange("bloodViscosityRegen",0.2,0,100);
        DAMAGE_SCALE = BUILDER
                .comment("% Damage Per point of normal damage to the Limb muscle and skin health")
                        .defineInRange("damageScale",5d,0.1,100);
        PAIN_PER_DAMAGE = BUILDER
                .comment("How much pain gives one point of normal damage")
                        .defineInRange("painPerDamage",5,0.1,100);
        OPIATE_PAIN_REDUCTION = BUILDER
                .comment("How much pain relief gives each point of Opiates")
                        .defineInRange("OpiatePainReduction",0.4,0.1,100);
        FRAC_DISL_FROM_MUSCLE_DAMAGE_CHANCE = BUILDER
                .comment("Chance for Discocation/Fracture at 0% Muscle Health")
                        .defineInRange("fractDislcFromMuslceDamageChance",0.33,0,1);
        NORMAL_LIMB_HEAL_RATE = BUILDER
                .comment("The normal rate at which Limbs Heal their Health passively (%/s)")
                        .defineInRange("normalLimbHealRate",0.04,0,100);
        BOOSTED_LIMB_HEAL_RATE = BUILDER
                .comment("The Boosted rate at which Limbs Heal their Health passively (%/s)")
                        .defineInRange("boostedLimbHealRate",0.125,0,100);
        TOURNIQUET_PAIN_PER_TICK = BUILDER
                .comment("Pain per tick for limb with applied tourniquet")
                        .defineInRange("tourniquetPainPerTick",0.05,0,100);
        TOURNIQUET_SAFE_TICKS=BUILDER
                .comment("Ticks for how long a tourniquet can be on a limb before causing muscle damage (ticks)")
                        .defineInRange("tourniquetSafeTicks",20*60*5,0, Integer.MAX_VALUE);
        TOURNIQUET_MUSCLE_DAMAGE=BUILDER
                .comment("Damage to the muscle health of the limb with a tourniquet after safe ticks have passed (s)")
                        .defineInRange("tourniquetMuscleDamage",1.5,0,100);
        MAGICAL_HEAL_RATE = BUILDER
                .comment("the Heal Scalar of magical healing(potions, regeneration)")
                .defineInRange("magicalHealRate",0.5,0,Double.MAX_VALUE);


        CONS_PENALTY_PER_OPIOID = BUILDER
                .comment("How much consciousness is penalized by one point of Opioids (opioids max is 100)")
                        .defineInRange("consPenaltyPerOpiod",0.2,0,Double.MAX_VALUE);
        CONSCIOUSNESS_REGEN = BUILDER
                .comment("How fast consciousness restores itself(keep in mind that consciousness is also capped by things such as:")
                        .comment("Oxygen,Opioids,High Pain,Head Health ...")
                                .comment("(%/s)")
                                        .defineInRange("consciousnessDelta",4,0,Double.MAX_VALUE);

        HELMET_ARMOR_SCALE = BUILDER
                .comment("Scaling of Armor values")
                        .defineInRange("helmetArmorScale",3,0,Double.MAX_VALUE);
        CHESTPLATE_ARMOR_SCALE = BUILDER.defineInRange("chestplateArmorScale",1.1,0,Double.MAX_VALUE);
        LEG_ARMOR_SCALE = BUILDER.defineInRange("leggingsArmorScale",1.5,0,Double.MAX_VALUE);
        BOOTS_ARMOR_SCALE = BUILDER.defineInRange("bootsArmorScale",2.5,0,Double.MAX_VALUE);
/*
        INSTANT_DEATH_MIN_DAMAGE = BUILDER
                .comment("At or above what damage will the player die instantly")
                        .defineInRange("instantDeathMinDamage",20,0,Double.MAX_VALUE);




 */
        PERMANENT_DAMAGE = BUILDER
                .comment("True/False Permanent Damage(until death)")
                .define("permanentDamage",true);

        BUILDER.push("LimbRegrowth");
        LIMB_REGROWTH = BUILDER
                .comment("Lets players regrow missing limbs with long enough regeneration 2+ effect")
                .define("enabled", false);

        LIMB_REGROWTH_MIN_REGEN = BUILDER
                .comment("Minimum amplifier of regeneration effect to regrow limbs (lvl 1 = amplifier 0). Keep in mind that the next option is for regeneration level specified here. Higher regeneration will take less time")
                .defineInRange("minRegen", 1, 0, 255);

        LIMB_REGROWTH_DURATION = BUILDER
                .comment("Duration in ticks for a limb to regrow")
                .defineInRange("duration", 60 * 20, 0, Integer.MAX_VALUE);
        BUILDER.pop();


        BRAIN_DRAIN = BUILDER
                .comment("How much the Brain health is drain per s when dying")
                        .defineInRange("deathBrainDrain",1.5,0.01,100);

        BRAIN_HEALTH_REGEN = BUILDER
                .comment("Brain health Regeneration per min (only works in Permanent Damage mode")
                        .defineInRange("brainRegen",0.3,0,Double.MAX_VALUE);

        IMMUNITY_SCALE = BUILDER
                .comment("Scalar of Immunity value")
                        .defineInRange("immunityScale",1,0,Double.MAX_VALUE);

        FRACTURE_HEAL_RATE = BUILDER
                .comment("Fracture Heal rate (%/s)")
                        .defineInRange("fractureRegen",0.05,0,Double.MAX_VALUE);
        DISLOCATION_HEAL_RATE = BUILDER
                .comment("Dislocation Heal rate (%/s)")
                .defineInRange("dislocationRegen",0.05,0,Double.MAX_VALUE);


        DO_TEMP_CHANGE = BUILDER
                .comment("on/off temperature")
                        .define("doTempChange",true);





        BUILDER.push("Integrations");
        PHYS_INTEGRATION = BUILDER
                .define("prototype_physics",false);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
