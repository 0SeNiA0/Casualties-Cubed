package net.zaharenko424.casualties_cubed.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalEffects;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluidType;

public class ModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, CasualtiesCubed.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, CasualtiesCubed.MOD_ID);


    public static final RegistryObject<FluidType> CLEAN_WATER_TYPE = FLUID_TYPES.register("clean_water", () -> new MedicalFluidType(MedicalEffects.CLEAN_WATER, FastColor.ARGB32.color(255, 117, 209, 255)));
    public static final RegistryObject<MedicalFluid> CLEAN_WATER = FLUIDS.register("clean_water", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CLEAN_WATER_TYPE, ModFluids.CLEAN_WATER, ModFluids.CLEAN_WATER)));

    public static final RegistryObject<FluidType> CARBONATED_WATER_TYPE = FLUID_TYPES.register("carbonated_water", () -> new MedicalFluidType(MedicalEffects.CARBONATED_WATER, FastColor.ARGB32.color(255, 102, 166, 255)));
    public static final RegistryObject<MedicalFluid> CARBONATED_WATER = FLUIDS.register("carbonated_water", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CARBONATED_WATER_TYPE, ModFluids.CARBONATED_WATER, ModFluids.CARBONATED_WATER)));

    public static final RegistryObject<FluidType> LRD_SERUM_TYPE = FLUID_TYPES.register("lrd_serum", () -> new MedicalFluidType(MedicalEffects.LRD_SERUM, 0xebb734));
    public static final RegistryObject<MedicalFluid> LRD_SERUM = FLUIDS.register("lrd_serum", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.LRD_SERUM_TYPE, ModFluids.LRD_SERUM, ModFluids.LRD_SERUM)));

    public static final RegistryObject<FluidType> MORPHINE_TYPE = FLUID_TYPES.register("morphine", () -> new MedicalFluidType(MedicalEffects.MORPHINE, 0x632329));
    public static final RegistryObject<MedicalFluid> MORPHINE = FLUIDS.register("morphine", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.MORPHINE_TYPE, ModFluids.MORPHINE, ModFluids.MORPHINE)));

    public static final RegistryObject<FluidType> BIO_CHEM_TYPE = FLUID_TYPES.register("bio_chem", () -> new MedicalFluidType(MedicalEffects.BIO_CHEM, -6364641));
    public static final RegistryObject<MedicalFluid> BIO_CHEM = FLUIDS.register("bio_chem", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.BIO_CHEM_TYPE, ModFluids.BIO_CHEM, ModFluids.BIO_CHEM)));

    public static final RegistryObject<FluidType> OPIUM_TYPE = FLUID_TYPES.register("opium", () -> new MedicalFluidType(MedicalEffects.OPIUM, 0xeb4034));
    public static final RegistryObject<MedicalFluid> OPIUM = FLUIDS.register("opium", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.OPIUM_TYPE, ModFluids.OPIUM, ModFluids.OPIUM)));

    public static final RegistryObject<FluidType> PAINKILLERS_TYPE = FLUID_TYPES.register("painkillers", () -> new MedicalFluidType(MedicalEffects.PAINKILLERS, 0x888888));
    public static final RegistryObject<MedicalFluid> PAINKILLERS = FLUIDS.register("painkillers", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.PAINKILLERS_TYPE, ModFluids.PAINKILLERS, ModFluids.PAINKILLERS)));

    public static final RegistryObject<FluidType> HEROIN_TYPE = FLUID_TYPES.register("heroin", () -> new MedicalFluidType(MedicalEffects.HEROIN, 0xedf8ff));
    public static final RegistryObject<MedicalFluid> HEROIN = FLUIDS.register("heroin", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.HEROIN_TYPE, ModFluids.HEROIN, ModFluids.HEROIN)));

    public static final RegistryObject<FluidType> NALOXONE_TYPE = FLUID_TYPES.register("naloxone", () -> new MedicalFluidType(MedicalEffects.NALOXONE, 0xf2abff));
    public static final RegistryObject<MedicalFluid> NALOXONE = FLUIDS.register("naloxone", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.NALOXONE_TYPE, ModFluids.NALOXONE, ModFluids.NALOXONE)));

    public static final RegistryObject<FluidType> NALTREXONE_TYPE = FLUID_TYPES.register("naltrexone", () -> new MedicalFluidType(MedicalEffects.NALTREXONE, -1));
    public static final RegistryObject<MedicalFluid> NALTREXONE = FLUIDS.register("naltrexone", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.NALTREXONE_TYPE, ModFluids.NALTREXONE, ModFluids.NALTREXONE)));

    public static final RegistryObject<FluidType> CEFTRIAXONE_TYPE = FLUID_TYPES.register("ceftriaxone", () -> new MedicalFluidType(MedicalEffects.CEFTRAIAXONE, 0x184a19));
    public static final RegistryObject<MedicalFluid> CEFTRIAXONE = FLUIDS.register("ceftriaxone", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CEFTRIAXONE_TYPE, ModFluids.CEFTRIAXONE, ModFluids.CEFTRIAXONE)));

    public static final RegistryObject<FluidType> FENTANYL_TYPE = FLUID_TYPES.register("fentanyl", () -> new MedicalFluidType(MedicalEffects.FENTANYL, 0xa1d9ff));
    public static final RegistryObject<MedicalFluid> FENTANYL = FLUIDS.register("fentanyl", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.FENTANYL_TYPE, ModFluids.FENTANYL, ModFluids.FENTANYL)));

    public static final RegistryObject<FluidType> KETCHUP_TYPE = FLUID_TYPES.register("ketchup", () -> new MedicalFluidType(MedicalEffects.KETCHUP, FastColor.ARGB32.color(255, 255, 43, 43)));
    public static final RegistryObject<MedicalFluid> KETCHUP = FLUIDS.register("ketchup", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.KETCHUP_TYPE, ModFluids.KETCHUP, ModFluids.KETCHUP)));

    public static final RegistryObject<FluidType> CHLOROFORM_TYPE = FLUID_TYPES.register("chloroform", () -> new MedicalFluidType(MedicalEffects.CHLOROFORM, FastColor.ARGB32.color(255, 186, 209, 167)));
    public static final RegistryObject<MedicalFluid> CHLOROFORM = FLUIDS.register("chloroform", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CHLOROFORM_TYPE, ModFluids.CHLOROFORM, ModFluids.CHLOROFORM)));

    public static final RegistryObject<FluidType> HIGH_GRADE_STIMULANT_TYPE = FLUID_TYPES.register("high_grade_stimulant", () -> new MedicalFluidType(MedicalEffects.HIGH_GRADE_STIMULANT, -1));
    public static final RegistryObject<MedicalFluid> HIGH_GRADE_STIMULANT = FLUIDS.register("high_grade_stimulant", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.HIGH_GRADE_STIMULANT_TYPE, ModFluids.HIGH_GRADE_STIMULANT, ModFluids.HIGH_GRADE_STIMULANT)));

    public static final RegistryObject<FluidType> MID_GRADE_STIMULANT_TYPE = FLUID_TYPES.register("mid_grade_stimulant", () -> new MedicalFluidType(MedicalEffects.MID_GRADE_STIMULANT, FastColor.ARGB32.color(255, 209, 209, 209)));
    public static final RegistryObject<MedicalFluid> MID_GRADE_STIMULANT = FLUIDS.register("mid_grade_stimulant", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.MID_GRADE_STIMULANT_TYPE, ModFluids.MID_GRADE_STIMULANT, ModFluids.MID_GRADE_STIMULANT)));

    public static final RegistryObject<FluidType> LOW_GRADE_STIMULANT_TYPE = FLUID_TYPES.register("low_grade_stimulant", () -> new MedicalFluidType(MedicalEffects.LOW_GRADE_STIMULANT, FastColor.ARGB32.color(255, 144, 144, 144)));
    public static final RegistryObject<MedicalFluid> LOW_GRADE_STIMULANT = FLUIDS.register("low_grade_stimulant", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.LOW_GRADE_STIMULANT_TYPE, ModFluids.LOW_GRADE_STIMULANT, ModFluids.LOW_GRADE_STIMULANT)));

    public static final RegistryObject<FluidType> APPLE_JUICE_TYPE = FLUID_TYPES.register("apple_juice", () -> new MedicalFluidType(MedicalEffects.APPLE_JUICE, FastColor.ARGB32.color(255, 197, 255, 97)));
    public static final RegistryObject<MedicalFluid> APPLE_JUICE = FLUIDS.register("apple_juice", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.APPLE_JUICE_TYPE, ModFluids.APPLE_JUICE, ModFluids.APPLE_JUICE)));

    public static final RegistryObject<FluidType> ORANGE_JUICE_TYPE = FLUID_TYPES.register("orange_juice", () -> new MedicalFluidType(MedicalEffects.ORANGE_JUICE, FastColor.ARGB32.color(255, 137, 41, 255)));
    public static final RegistryObject<MedicalFluid> ORANGE_JUICE = FLUIDS.register("orange_juice", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ORANGE_JUICE_TYPE, ModFluids.ORANGE_JUICE, ModFluids.ORANGE_JUICE)));

    public static final RegistryObject<FluidType> LEMONADE_TYPE = FLUID_TYPES.register("lemonade", () -> new MedicalFluidType(MedicalEffects.LEMONADE, FastColor.ARGB32.color(255, 247, 97, 255)));
    public static final RegistryObject<MedicalFluid> LEMONADE = FLUIDS.register("lemonade", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.LEMONADE_TYPE, ModFluids.LEMONADE, ModFluids.LEMONADE)));

    public static final RegistryObject<FluidType> ICE_TEA_TYPE = FLUID_TYPES.register("ice_tea", () -> new MedicalFluidType(MedicalEffects.ICE_TEA, FastColor.ARGB32.color(255, 250, 135, 52)));
    public static final RegistryObject<MedicalFluid> ICE_TEA = FLUIDS.register("ice_tea", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ICE_TEA_TYPE, ModFluids.ICE_TEA, ModFluids.ICE_TEA)));

    public static final RegistryObject<FluidType> SOUP_TYPE = FLUID_TYPES.register("soup", () -> new MedicalFluidType(MedicalEffects.SOUP, FastColor.ARGB32.color(255, 125, 81, 0)));
    public static final RegistryObject<MedicalFluid> SOUP = FLUIDS.register("soup", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SOUP_TYPE, ModFluids.SOUP, ModFluids.SOUP)));

    public static final RegistryObject<FluidType> CHOCOLATE_MILK_TYPE = FLUID_TYPES.register("chocolate_milk", () -> new MedicalFluidType(MedicalEffects.CHOCOLATE_MILK, FastColor.ARGB32.color(255, 143, 92, 55)));
    public static final RegistryObject<MedicalFluid> CHOCOLATE_MILK = FLUIDS.register("chocolate_milk", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CHOCOLATE_MILK_TYPE, ModFluids.CHOCOLATE_MILK, ModFluids.CHOCOLATE_MILK)));

    public static final RegistryObject<FluidType> CEREAL_TYPE = FLUID_TYPES.register("cereal", () -> new MedicalFluidType(MedicalEffects.CEREAL, FastColor.ARGB32.color(255, 255, 219, 156)));
    public static final RegistryObject<MedicalFluid> CEREAL = FLUIDS.register("cereal", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CEREAL_TYPE, ModFluids.CEREAL, ModFluids.CEREAL)));

    public static final RegistryObject<FluidType> COFFEE_TYPE = FLUID_TYPES.register("coffee", () -> new MedicalFluidType(MedicalEffects.COFFEE, FastColor.ARGB32.color(255, 80, 50, 30)));
    public static final RegistryObject<MedicalFluid> COFFEE = FLUIDS.register("coffee", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.COFFEE_TYPE, ModFluids.COFFEE, ModFluids.COFFEE)));

    public static final RegistryObject<FluidType> ENERGY_DRINK_TYPE = FLUID_TYPES.register("energy_drink", () -> new MedicalFluidType(MedicalEffects.ENERGY_DRINK, FastColor.ARGB32.color(255, 187, 0, 255)));
    public static final RegistryObject<MedicalFluid> ENERGY_DRINK = FLUIDS.register("energy_drink", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ENERGY_DRINK_TYPE, ModFluids.ENERGY_DRINK, ModFluids.ENERGY_DRINK)));

    public static final RegistryObject<FluidType> SPORTS_DRINK_TYPE = FLUID_TYPES.register("sports_drink", () -> new MedicalFluidType(MedicalEffects.SPORTS_DRINK, FastColor.ARGB32.color(255, 10, 59, 255)));
    public static final RegistryObject<MedicalFluid> SPORTS_DRINK = FLUIDS.register("sports_drink", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SPORTS_DRINK_TYPE, ModFluids.SPORTS_DRINK, ModFluids.SPORTS_DRINK)));

    public static final RegistryObject<FluidType> OLIVE_OIL_TYPE = FLUID_TYPES.register("olive_oil", () -> new MedicalFluidType(MedicalEffects.OLIVE_OIL, FastColor.ARGB32.color(200, 129, 135, 7)));
    public static final RegistryObject<MedicalFluid> OLIVE_OIL = FLUIDS.register("olive_oil", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.OLIVE_OIL_TYPE, ModFluids.OLIVE_OIL, ModFluids.OLIVE_OIL)));

    public static final RegistryObject<FluidType> HOT_SAUCE_TYPE = FLUID_TYPES.register("hot_sauce", () -> new MedicalFluidType(MedicalEffects.HOT_SAUCE, FastColor.ARGB32.color(200, 255, 0, 0)));
    public static final RegistryObject<MedicalFluid> HOT_SAUCE = FLUIDS.register("hot_sauce", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.HOT_SAUCE_TYPE, ModFluids.HOT_SAUCE, ModFluids.HOT_SAUCE)));

    public static final RegistryObject<FluidType> ICE_CREAM_TYPE = FLUID_TYPES.register("ice_cream", () -> new MedicalFluidType(MedicalEffects.ICE_CREAM, FastColor.ARGB32.color(255, 237, 255, 189)));
    public static final RegistryObject<MedicalFluid> ICE_CREAM = FLUIDS.register("ice_cream", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ICE_CREAM_TYPE, ModFluids.ICE_CREAM, ModFluids.ICE_CREAM)));

    public static final RegistryObject<FluidType> YOGURT_TYPE = FLUID_TYPES.register("yogurt", () -> new MedicalFluidType(MedicalEffects.YOGURT, FastColor.ARGB32.color(255, 213, 235, 240)));
    public static final RegistryObject<MedicalFluid> YOGURT = FLUIDS.register("yogurt", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.YOGURT_TYPE, ModFluids.YOGURT, ModFluids.YOGURT)));

    public static final RegistryObject<FluidType> MOLD_TYPE = FLUID_TYPES.register("mold", () -> new MedicalFluidType(MedicalEffects.MOLD, FastColor.ARGB32.color(255, 63, 79, 50)));
    public static final RegistryObject<MedicalFluid> MOLD = FLUIDS.register("mold", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.MOLD_TYPE, ModFluids.MOLD, ModFluids.MOLD)));

    public static final RegistryObject<FluidType> POWDERED_MILK_TYPE = FLUID_TYPES.register("powdered_milk", () -> new MedicalFluidType(MedicalEffects.POWDERED_MILK, FastColor.ARGB32.color(255, 242, 242, 242)));
    public static final RegistryObject<MedicalFluid> POWDERED_MILK = FLUIDS.register("powdered_milk", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.POWDERED_MILK_TYPE, ModFluids.POWDERED_MILK, ModFluids.POWDERED_MILK)));

    public static final RegistryObject<FluidType> RAD_WATER_TYPE = FLUID_TYPES.register("rad_water", () -> new MedicalFluidType(MedicalEffects.RAD_WATER, FastColor.ARGB32.color(255, 121, 224, 221)));
    public static final RegistryObject<MedicalFluid> RAD_WATER = FLUIDS.register("rad_water", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.RAD_WATER_TYPE, ModFluids.RAD_WATER, ModFluids.RAD_WATER)));

    public static final RegistryObject<FluidType> MERCURY_TYPE = FLUID_TYPES.register("mercury", () -> new MedicalFluidType(MedicalEffects.MERCURY, FastColor.ARGB32.color(255, 77, 77, 77)));
    public static final RegistryObject<MedicalFluid> MERCURY = FLUIDS.register("mercury", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.MERCURY_TYPE, ModFluids.MERCURY, ModFluids.MERCURY)));

    public static final RegistryObject<FluidType> SODA_TYPE = FLUID_TYPES.register("soda", () -> new MedicalFluidType(MedicalEffects.SODA, FastColor.ARGB32.color(255, 112, 94, 73)));
    public static final RegistryObject<MedicalFluid> SODA = FLUIDS.register("soda", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SODA_TYPE, ModFluids.SODA, ModFluids.SODA)));

    public static final RegistryObject<FluidType> ALCOHOL_TYPE = FLUID_TYPES.register("alcohol", () -> new MedicalFluidType(MedicalEffects.ALCOHOL, 0x828282));
    public static final RegistryObject<MedicalFluid> ALCOHOL = FLUIDS.register("alcohol", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ALCOHOL_TYPE, ModFluids.ALCOHOL, ModFluids.ALCOHOL)));

    public static final RegistryObject<FluidType> BLEACH_TYPE = FLUID_TYPES.register("bleach", () -> new MedicalFluidType(MedicalEffects.BLEACH, 2146230734));
    public static final RegistryObject<MedicalFluid> BLEACH = FLUIDS.register("bleach", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.BLEACH_TYPE, ModFluids.BLEACH, ModFluids.BLEACH)));

    public static final RegistryObject<FluidType> RELIEF_CREAM_TYPE = FLUID_TYPES.register("relief_cream", () -> new MedicalFluidType(MedicalEffects.RELIEF_CREAM, 0x75644d));
    public static final RegistryObject<MedicalFluid> RELIEF_CREAM = FLUIDS.register("relief_cream", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.RELIEF_CREAM_TYPE, ModFluids.RELIEF_CREAM, ModFluids.RELIEF_CREAM)));

    public static final RegistryObject<FluidType> WOUND_GLUE_TYPE = FLUID_TYPES.register("wound_glue", () -> new MedicalFluidType(MedicalEffects.WOUND_GLUE, -1130047));
    public static final RegistryObject<MedicalFluid> WOUND_GLUE = FLUIDS.register("wound_glue", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.WOUND_GLUE_TYPE, ModFluids.WOUND_GLUE, ModFluids.WOUND_GLUE)));

    public static final RegistryObject<FluidType> BRAINGROW_TYPE = FLUID_TYPES.register("braingrow", () -> new MedicalFluidType(MedicalEffects.BRAINGROW, 0x915946));
    public static final RegistryObject<MedicalFluid> BRAINGROW = FLUIDS.register("braingrow", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.BRAINGROW_TYPE, ModFluids.BRAINGROW, ModFluids.BRAINGROW)));

    public static final RegistryObject<FluidType> ANTIBIOTICS_TYPE = FLUID_TYPES.register("antibiotics", () -> new MedicalFluidType(MedicalEffects.ANTIBIOTICS, 0x593f8a));
    public static final RegistryObject<MedicalFluid> ANTIBIOTICS = FLUIDS.register("antibiotics", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ANTIBIOTICS_TYPE, ModFluids.ANTIBIOTICS, ModFluids.ANTIBIOTICS)));

    public static final RegistryObject<FluidType> ANTIVENOM_TYPE = FLUID_TYPES.register("antivenom", () -> new MedicalFluidType(MedicalEffects.ANTIVENOM, -12792486));
    public static final RegistryObject<MedicalFluid> ANTIVENOM = FLUIDS.register("antivenom", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ANTIVENOM_TYPE, ModFluids.ANTIVENOM, ModFluids.ANTIVENOM)));

    public static final RegistryObject<FluidType> ANTISERUM_TYPE = FLUID_TYPES.register("antiserum", () -> new MedicalFluidType(MedicalEffects.ANTISERUM, 0x6f3582));
    public static final RegistryObject<MedicalFluid> ANTISERUM = FLUIDS.register("antiserum", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ANTISERUM_TYPE, ModFluids.ANTISERUM, ModFluids.ANTISERUM)));

    public static final RegistryObject<FluidType> PROCOAGULANT_TYPE = FLUID_TYPES.register("procoagulant", () -> new MedicalFluidType(MedicalEffects.PROCOAGULANT, 0x57172b));
    public static final RegistryObject<MedicalFluid> PROCOAGULANT = FLUIDS.register("procoagulant", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.PROCOAGULANT_TYPE, ModFluids.PROCOAGULANT, ModFluids.PROCOAGULANT)));

    public static final RegistryObject<FluidType> EPINEPHRINE_TYPE = FLUID_TYPES.register("epinephrine", () -> new MedicalFluidType(MedicalEffects.EPINEPHRINE, FastColor.ARGB32.color(161, 255, 233, 255)));
    public static final RegistryObject<MedicalFluid> EPINEPHRINE = FLUIDS.register("epinephrine", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.EPINEPHRINE_TYPE, ModFluids.EPINEPHRINE, ModFluids.EPINEPHRINE)));

    public static final RegistryObject<FluidType> OXYLINE_TYPE = FLUID_TYPES.register("oxyline", () -> new MedicalFluidType(MedicalEffects.OXYLINE, FastColor.ARGB32.color(77, 255, 222, 255)));
    public static final RegistryObject<MedicalFluid> OXYLINE = FLUIDS.register("oxyline", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.OXYLINE_TYPE, ModFluids.OXYLINE, ModFluids.OXYLINE)));

    public static final RegistryObject<FluidType> SODIUM_NITROPRUSSIDE_TYPE = FLUID_TYPES.register("sodium_nitroprusside", () -> new MedicalFluidType(MedicalEffects.SODIUM_NITROPRUSSIDE, FastColor.ARGB32.color(255, 207, 88, 41)));
    public static final RegistryObject<MedicalFluid> SODIUM_NITROPRUSSIDE = FLUIDS.register("sodium_nitroprusside", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SODIUM_NITROPRUSSIDE_TYPE, ModFluids.SODIUM_NITROPRUSSIDE, ModFluids.SODIUM_NITROPRUSSIDE)));

    public static final RegistryObject<FluidType> VASOPRESSIN_TYPE = FLUID_TYPES.register("vasopressin", () -> new MedicalFluidType(MedicalEffects.VASOPRESSIN, FastColor.ARGB32.color(160, 255, 255, 255)));
    public static final RegistryObject<MedicalFluid> VASOPRESSIN = FLUIDS.register("vasopressin", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.VASOPRESSIN_TYPE, ModFluids.VASOPRESSIN, ModFluids.VASOPRESSIN)));

    public static final RegistryObject<FluidType> AMIODARONE_TYPE = FLUID_TYPES.register("amiodarone", () -> new MedicalFluidType(MedicalEffects.AMIODARONE, FastColor.ARGB32.color(255, 220, 212, 160)));
    public static final RegistryObject<MedicalFluid> AMIODARONE = FLUIDS.register("amiodarone", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.AMIODARONE_TYPE, ModFluids.AMIODARONE, ModFluids.AMIODARONE)));

    public static final RegistryObject<FluidType> STREPTOKINASE_TYPE = FLUID_TYPES.register("streptokinase", () -> new MedicalFluidType(MedicalEffects.STREPTOKINASE, 0x0aecfc));
    public static final RegistryObject<MedicalFluid> STREPTOKINASE = FLUIDS.register("streptokinase", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.STREPTOKINASE_TYPE, ModFluids.STREPTOKINASE, ModFluids.STREPTOKINASE)));

    public static final RegistryObject<FluidType> SALINE_TYPE = FLUID_TYPES.register("saline", () -> new MedicalFluidType(MedicalEffects.SALINE, 0xc9c8c5));
    public static final RegistryObject<MedicalFluid> SALINE = FLUIDS.register("saline", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SALINE_TYPE, ModFluids.SALINE, ModFluids.SALINE)));

    public static final RegistryObject<FluidType> YELLOW_BLOOD_TYPE = FLUID_TYPES.register("yellow_blood", () -> new MedicalFluidType(MedicalEffects.YELLOW_BLOOD, FastColor.ARGB32.color(255, 255, 201, 0)));
    public static final RegistryObject<MedicalFluid> YELLOW_BLOOD = FLUIDS.register("yellow_blood", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.YELLOW_BLOOD_TYPE, ModFluids.YELLOW_BLOOD, ModFluids.YELLOW_BLOOD)));

    public static final RegistryObject<FluidType> RED_BLOOD_TYPE = FLUID_TYPES.register("red_blood", () -> new MedicalFluidType(MedicalEffects.RED_BLOOD, FastColor.ARGB32.color(255, 199, 10, 10)));
    public static final RegistryObject<MedicalFluid> RED_BLOOD = FLUIDS.register("red_blood", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.RED_BLOOD_TYPE, ModFluids.RED_BLOOD, ModFluids.RED_BLOOD)));

    public static final RegistryObject<FluidType> ALIEN_BLOOD_TYPE = FLUID_TYPES.register("alien_blood", () -> new MedicalFluidType(MedicalEffects.ALIEN_BLOOD, FastColor.ARGB32.color(255, 255, 235, 18)));
    public static final RegistryObject<MedicalFluid> ALIEN_BLOOD = FLUIDS.register("alien_blood", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ALIEN_BLOOD_TYPE, ModFluids.ALIEN_BLOOD, ModFluids.ALIEN_BLOOD)));

    public static final RegistryObject<FluidType> ANTISEPTIC_TYPE = FLUID_TYPES.register("antiseptic", () -> new MedicalFluidType(MedicalEffects.ANTISEPTIC, 0x5a6b45));
    public static final RegistryObject<MedicalFluid> ANTISEPTIC = FLUIDS.register("antiseptic", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ANTISEPTIC_TYPE, ModFluids.ANTISEPTIC, ModFluids.ANTISEPTIC)));

    public static final RegistryObject<FluidType> GROUNDWATER_TYPE = FLUID_TYPES.register("groundwater", () -> new MedicalFluidType(MedicalEffects.GROUNDWATER, FastColor.ARGB32.color(255, 89, 138, 212)));
    public static final RegistryObject<MedicalFluid> GROUNDWATER = FLUIDS.register("groundwater", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.GROUNDWATER_TYPE, ModFluids.GROUNDWATER, ModFluids.GROUNDWATER)));

    public static final RegistryObject<FluidType> LUMALGAE_TYPE = FLUID_TYPES.register("lumalgae", () -> new MedicalFluidType(MedicalEffects.LUMALGAE, FastColor.ARGB32.color(255, 33, 153, 0)));
    public static final RegistryObject<MedicalFluid> LUMALGAE = FLUIDS.register("lumalgae", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.LUMALGAE_TYPE, ModFluids.LUMALGAE, ModFluids.LUMALGAE)));

    public static final RegistryObject<FluidType> OIL_TYPE = FLUID_TYPES.register("oil", () -> new MedicalFluidType(MedicalEffects.OIL, FastColor.ARGB32.color(255, 71, 50, 21)));
    public static final RegistryObject<MedicalFluid> OIL = FLUIDS.register("oil", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.OIL_TYPE, ModFluids.OIL, ModFluids.OIL)));

    public static final RegistryObject<FluidType> SAP_TYPE = FLUID_TYPES.register("sap", () -> new MedicalFluidType(MedicalEffects.SAP, FastColor.ARGB32.color(255, 209, 190, 63)));
    public static final RegistryObject<MedicalFluid> SAP = FLUIDS.register("sap", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SAP_TYPE, ModFluids.SAP, ModFluids.SAP)));

    public static final RegistryObject<FluidType> DIRTY_WATER_TYPE = FLUID_TYPES.register("dirty_water", () -> new MedicalFluidType(MedicalEffects.DIRTY_WATER, FastColor.ARGB32.color(255, 153, 126, 67)));
    public static final RegistryObject<MedicalFluid> DIRTY_WATER = FLUIDS.register("dirty_water", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.DIRTY_WATER_TYPE, ModFluids.DIRTY_WATER, ModFluids.DIRTY_WATER)));

    public static final RegistryObject<FluidType> FAT_TYPE = FLUID_TYPES.register("fat", () -> new MedicalFluidType(MedicalEffects.FAT, FastColor.ARGB32.color(255, 209, 190, 63)));
    public static final RegistryObject<MedicalFluid> FAT = FLUIDS.register("fat", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.FAT_TYPE, ModFluids.FAT, ModFluids.FAT)));

    public static final RegistryObject<FluidType> SOAP_TYPE = FLUID_TYPES.register("soap", () -> new MedicalFluidType(MedicalEffects.SOAP, FastColor.ARGB32.color(255, 161, 255, 186)));
    public static final RegistryObject<MedicalFluid> SOAP = FLUIDS.register("soap", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SOAP_TYPE, ModFluids.SOAP, ModFluids.SOAP)));

    public static final RegistryObject<FluidType> PRODUCE_JUICE_TYPE = FLUID_TYPES.register("produce_juice", () -> new MedicalFluidType(MedicalEffects.PRODUCE_JUICE, FastColor.ARGB32.color(255, 255, 254, 181)));
    public static final RegistryObject<MedicalFluid> PRODUCE_JUICE = FLUIDS.register("produce_juice", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.PRODUCE_JUICE_TYPE, ModFluids.PRODUCE_JUICE, ModFluids.PRODUCE_JUICE)));

    public static final RegistryObject<FluidType> REFINED_JUICE_TYPE = FLUID_TYPES.register("refined_juice", () -> new MedicalFluidType(MedicalEffects.REFINED_JUICE, FastColor.ARGB32.color(255, 255, 225, 115)));
    public static final RegistryObject<MedicalFluid> REFINED_JUICE = FLUIDS.register("refined_juice", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.REFINED_JUICE_TYPE, ModFluids.REFINED_JUICE, ModFluids.REFINED_JUICE)));

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }
}
