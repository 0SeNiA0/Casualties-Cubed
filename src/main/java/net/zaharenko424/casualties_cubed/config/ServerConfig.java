package net.zaharenko424.casualties_cubed.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue EXPIE_MODE;

    // Damage scaling
    public static final ForgeConfigSpec.DoubleValue DAMAGE_SCALE;
    public static final ForgeConfigSpec.DoubleValue PAIN_PER_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue FRAC_DISL_FROM_MUSCLE_DAMAGE_CHANCE;

    // Limb healing rates
    public static final ForgeConfigSpec.DoubleValue MAGICAL_HEAL_RATE;

    //Armor
    public static final ForgeConfigSpec.DoubleValue HELMET_ARMOR_SCALE;
    public static final ForgeConfigSpec.DoubleValue CHESTPLATE_ARMOR_SCALE;
    public static final ForgeConfigSpec.DoubleValue LEG_ARMOR_SCALE;
    public static final ForgeConfigSpec.DoubleValue BOOTS_ARMOR_SCALE;

    public static final ForgeConfigSpec.BooleanValue DO_TEMP_CHANGE;

    public static final ForgeConfigSpec.BooleanValue PERMANENT_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue LIMB_REGROWTH;
    public static final ForgeConfigSpec.IntValue LIMB_REGROWTH_MIN_REGEN;
    public static final ForgeConfigSpec.IntValue LIMB_REGROWTH_DURATION;

    public static final ForgeConfigSpec.DoubleValue METABOLISM_RATE;
    public static final ForgeConfigSpec.DoubleValue HEALING_RATE;
    public static final ForgeConfigSpec.DoubleValue INFECTION_CHANCE;
    public static final ForgeConfigSpec.DoubleValue INFECTION_RATE;
    public static final ForgeConfigSpec.DoubleValue BLEED_RATE;
    public static final ForgeConfigSpec.DoubleValue FIB_RATE;
    public static final ForgeConfigSpec.BooleanValue STROKES;
    public static final ForgeConfigSpec.DoubleValue EXP_GAIN;
    public static final ForgeConfigSpec.DoubleValue SLEEP_CYCLE_SPEED;
    public static final ForgeConfigSpec.DoubleValue MOOD_NORMALIZATION_RATE;
    public static final ForgeConfigSpec.BooleanValue FORCE_SLEEP;
    public static final ForgeConfigSpec.BooleanValue NO_SLEEP_RESTRICTIONS;
    public static final ForgeConfigSpec.DoubleValue STAMINA_REGEN;
    public static final ForgeConfigSpec.BooleanValue BRAIN_DAMAGE_FX;

    public static final ForgeConfigSpec.BooleanValue INFINITE_LAST_STAND;
    public static final ForgeConfigSpec.BooleanValue TOTEM_OF_UNDYING_LAST_STAND;

    public static final ForgeConfigSpec.BooleanValue PHYS_INTEGRATION;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("Prototype Pain Server Config");

        EXPIE_MODE = BUILDER
                .comment("Defines whether some aspects of the mod should assume that player is an expie (coffee being toxic, etc).")
                .define("expieMode", true);


        DAMAGE_SCALE = BUILDER
                .comment("% Damage Per point of normal damage to the Limb muscle and skin health")
                        .defineInRange("damageScale",5d,0.1,100);
        PAIN_PER_DAMAGE = BUILDER
                .comment("How much pain gives one point of normal damage")
                        .defineInRange("painPerDamage",5,0.1,100);
        FRAC_DISL_FROM_MUSCLE_DAMAGE_CHANCE = BUILDER
                .comment("Chance for Discocation/Fracture at 0% Muscle Health")
                        .defineInRange("fractDislcFromMuslceDamageChance",0.33,0,1);
        MAGICAL_HEAL_RATE = BUILDER
                .comment("the Heal Scalar of magical healing(potions, regeneration)")
                .defineInRange("magicalHealRate",0.5,0,Double.MAX_VALUE);

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


        DO_TEMP_CHANGE = BUILDER
                .comment("on/off temperature")
                        .define("doTempChange",true);


        METABOLISM_RATE = BUILDER
                .comment("Multiplier of sickness decrease and hunger buildup.")
                .defineInRange("metabolismRate", 1f, 0, 1000);

        HEALING_RATE = BUILDER
                .comment("Healing multiplier.")
                .defineInRange("healingRate", 1f, 0, 1000);

        INFECTION_CHANCE = BUILDER
                .comment("Infection chance multiplier.")
                .defineInRange("infectionChance",1f,0,1000);

        INFECTION_RATE = BUILDER
                .comment("Infection rate multiplier.")
                .defineInRange("infectionRate", 1f, 0, 1000);

        BLEED_RATE = BUILDER
                .comment("Bleed rate multiplier.")
                .defineInRange("bleedRate", 1f, 0, 1000);

        FIB_RATE = BUILDER
                .comment("Multiplier of fibrillation buildup.")
                .defineInRange("fibRate", 1f, 0, 1000);

        STROKES = BUILDER
                .comment("Enables strokes.")
                .define("strokes", true);

        EXP_GAIN = BUILDER
                .comment("Skill exp gain multiplier.")
                .defineInRange("expGain", 1, 0, 1000f);

        SLEEP_CYCLE_SPEED = BUILDER
                .comment("Energy gain/loss multiplier.")
                .defineInRange("sleepCycleSpeed", 1, 0, 1000f);

        MOOD_NORMALIZATION_RATE = BUILDER
                .comment("Mood normalization speed multiplier.")
                .defineInRange("moodNormalizationRate", 1, 0, 1000f);

        FORCE_SLEEP = BUILDER
                .comment("Force sleep when energy too low.")
                .define("forceSleep", true);

        NO_SLEEP_RESTRICTIONS = BUILDER
                .comment("Wont wake up from usual factors.")
                .define("noSleepRestrictions", false);

        STAMINA_REGEN = BUILDER
                .comment("Stamina regen multiplier.")
                .defineInRange("staminaRegen", 1, 0, 1000f);

        BRAIN_DAMAGE_FX = BUILDER
                .comment("Brain damage effects such as dropping held items")
                .define("brainDamageFx", true);


        INFINITE_LAST_STAND = BUILDER
                .comment("Allow triggering last stand unlimited amount of times")
                .define("infiniteLastStand", false);

        TOTEM_OF_UNDYING_LAST_STAND = BUILDER
                .comment("Use totem of undying to guarantee last stand (doing so will prevent natural last stand roll unless infiniteLastStand is true)")
                .define("totemLastStand", true);



        BUILDER.push("Integrations");
        PHYS_INTEGRATION = BUILDER
                .define("prototype_physics",false);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
