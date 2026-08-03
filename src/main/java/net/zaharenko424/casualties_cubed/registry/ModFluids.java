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


    public static final RegistryObject<FluidType> CLEAN_WATER_TYPE = FLUID_TYPES.register("clean_water", () -> new MedicalFluidType(FluidType.Properties.create(), FastColor.ARGB32.color(255, 117, 209, 255)));
    public static final RegistryObject<MedicalFluid> CLEAN_WATER = FLUIDS.register("clean_water", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CLEAN_WATER_TYPE, ModFluids.CLEAN_WATER, ModFluids.CLEAN_WATER), MedicalEffects.CLEAN_WATER));

    public static final RegistryObject<FluidType> LRD_SERUM_TYPE = FLUID_TYPES.register("lrd_serum", () -> new MedicalFluidType(FluidType.Properties.create(), 0xebb734));
    public static final RegistryObject<MedicalFluid> LRD_SERUM = FLUIDS.register("lrd_serum", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.LRD_SERUM_TYPE, ModFluids.LRD_SERUM, ModFluids.LRD_SERUM), MedicalEffects.LRD_SERUM));

    public static final RegistryObject<FluidType> MORPHINE_TYPE = FLUID_TYPES.register("morphine", () -> new MedicalFluidType(FluidType.Properties.create(), 0x632329));
    public static final RegistryObject<MedicalFluid> MORPHINE = FLUIDS.register("morphine", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.MORPHINE_TYPE, ModFluids.MORPHINE, ModFluids.MORPHINE), MedicalEffects.MORPHINE));

    public static final RegistryObject<FluidType> BIO_CHEM_TYPE = FLUID_TYPES.register("bio_chem", () -> new MedicalFluidType(FluidType.Properties.create(), -6364641));
    public static final RegistryObject<MedicalFluid> BIO_CHEM = FLUIDS.register("bio_chem", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.BIO_CHEM_TYPE, ModFluids.BIO_CHEM, ModFluids.BIO_CHEM), MedicalEffects.BIO_CHEM));

    public static final RegistryObject<FluidType> OPIUM_TYPE = FLUID_TYPES.register("opium", () -> new MedicalFluidType(FluidType.Properties.create(), 0xeb4034));
    public static final RegistryObject<MedicalFluid> OPIUM = FLUIDS.register("opium", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.OPIUM_TYPE, ModFluids.OPIUM, ModFluids.OPIUM), MedicalEffects.OPIUM));

    public static final RegistryObject<FluidType> PAINKILLERS_TYPE = FLUID_TYPES.register("painkillers", () -> new MedicalFluidType(FluidType.Properties.create(), 0x888888));
    public static final RegistryObject<MedicalFluid> PAINKILLERS = FLUIDS.register("painkillers", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.PAINKILLERS_TYPE, ModFluids.PAINKILLERS, ModFluids.PAINKILLERS), MedicalEffects.PAINKILLERS));

    public static final RegistryObject<FluidType> HEROIN_TYPE = FLUID_TYPES.register("heroin", () -> new MedicalFluidType(FluidType.Properties.create(), 0xedf8ff));
    public static final RegistryObject<MedicalFluid> HEROIN = FLUIDS.register("heroin", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.HEROIN_TYPE, ModFluids.HEROIN, ModFluids.HEROIN), MedicalEffects.HEROIN));

    public static final RegistryObject<FluidType> NALOXONE_TYPE = FLUID_TYPES.register("naloxone", () -> new MedicalFluidType(FluidType.Properties.create(), 0xf2abff));
    public static final RegistryObject<MedicalFluid> NALOXONE = FLUIDS.register("naloxone", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.NALOXONE_TYPE, ModFluids.NALOXONE, ModFluids.NALOXONE), MedicalEffects.NALOXONE));

    public static final RegistryObject<FluidType> NALTREXONE_TYPE = FLUID_TYPES.register("naltrexone", () -> new MedicalFluidType(FluidType.Properties.create(), -1));
    public static final RegistryObject<MedicalFluid> NALTREXONE = FLUIDS.register("naltrexone", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.NALTREXONE_TYPE, ModFluids.NALTREXONE, ModFluids.NALTREXONE), MedicalEffects.NALTREXONE));

    public static final RegistryObject<FluidType> CEFTRIAXONE_TYPE = FLUID_TYPES.register("ceftriaxone", () -> new MedicalFluidType(FluidType.Properties.create(), 0x184a19));
    public static final RegistryObject<MedicalFluid> CEFTRIAXONE = FLUIDS.register("ceftriaxone", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CEFTRIAXONE_TYPE, ModFluids.CEFTRIAXONE, ModFluids.CEFTRIAXONE), MedicalEffects.CEFTRAIAXONE));

    public static final RegistryObject<FluidType> FENTANYL_TYPE = FLUID_TYPES.register("fentanyl", () -> new MedicalFluidType(FluidType.Properties.create(), 0xa1d9ff));
    public static final RegistryObject<MedicalFluid> FENTANYL = FLUIDS.register("fentanyl", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.FENTANYL_TYPE, ModFluids.FENTANYL, ModFluids.FENTANYL), MedicalEffects.FENTANYL));

    public static final RegistryObject<FluidType> CHOCOLATE_MILK_TYPE = FLUID_TYPES.register("chocolate_milk", () -> new MedicalFluidType(FluidType.Properties.create(), 0x2e251c));
    public static final RegistryObject<MedicalFluid> CHOCOLATE_MILK = FLUIDS.register("chocolate_milk", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.CHOCOLATE_MILK_TYPE, ModFluids.CHOCOLATE_MILK, ModFluids.CHOCOLATE_MILK), MedicalEffects.CHOCOLATE_MILK));

    public static final RegistryObject<FluidType> MOLD_TYPE = FLUID_TYPES.register("mold", () -> new MedicalFluidType(FluidType.Properties.create(), FastColor.ARGB32.color(255, 63, 79, 50)));
    public static final RegistryObject<MedicalFluid> MOLD = FLUIDS.register("mold", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.MOLD_TYPE, ModFluids.MOLD, ModFluids.MOLD), MedicalEffects.MOLD));

    public static final RegistryObject<FluidType> MERCURY_TYPE = FLUID_TYPES.register("mercury", () -> new MedicalFluidType(FluidType.Properties.create(), FastColor.ARGB32.color(255, 77, 77, 77)));
    public static final RegistryObject<MedicalFluid> MERCURY = FLUIDS.register("mercury", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.MERCURY_TYPE, ModFluids.MERCURY, ModFluids.MERCURY), MedicalEffects.MERCURY));

    public static final RegistryObject<FluidType> ALCOHOL_TYPE = FLUID_TYPES.register("alcohol", () -> new MedicalFluidType(FluidType.Properties.create(), 0x828282));
    public static final RegistryObject<MedicalFluid> ALCOHOL = FLUIDS.register("alcohol", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ALCOHOL_TYPE, ModFluids.ALCOHOL, ModFluids.ALCOHOL), MedicalEffects.ALCOHOL));

    public static final RegistryObject<FluidType> BLEACH_TYPE = FLUID_TYPES.register("bleach", () -> new MedicalFluidType(FluidType.Properties.create(), 2146230734));
    public static final RegistryObject<MedicalFluid> BLEACH = FLUIDS.register("bleach", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.BLEACH_TYPE, ModFluids.BLEACH, ModFluids.BLEACH), MedicalEffects.BLEACH));

    public static final RegistryObject<FluidType> RELIEF_CREAM_TYPE = FLUID_TYPES.register("relief_cream", () -> new MedicalFluidType(FluidType.Properties.create(), 0x75644d));
    public static final RegistryObject<MedicalFluid> RELIEF_CREAM = FLUIDS.register("relief_cream", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.RELIEF_CREAM_TYPE, ModFluids.RELIEF_CREAM, ModFluids.RELIEF_CREAM), MedicalEffects.RELIEF_CREAM));

    public static final RegistryObject<FluidType> WOUND_GLUE_TYPE = FLUID_TYPES.register("wound_glue", () -> new MedicalFluidType(FluidType.Properties.create(), -1130047));
    public static final RegistryObject<MedicalFluid> WOUND_GLUE = FLUIDS.register("wound_glue", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.WOUND_GLUE_TYPE, ModFluids.WOUND_GLUE, ModFluids.WOUND_GLUE), MedicalEffects.WOUND_GLUE));

    public static final RegistryObject<FluidType> BRAINGROW_TYPE = FLUID_TYPES.register("braingrow", () -> new MedicalFluidType(FluidType.Properties.create(), 0x915946));
    public static final RegistryObject<MedicalFluid> BRAINGROW = FLUIDS.register("braingrow", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.BRAINGROW_TYPE, ModFluids.BRAINGROW, ModFluids.BRAINGROW), MedicalEffects.BRAINGROW));

    public static final RegistryObject<FluidType> ANTIBIOTICS_TYPE = FLUID_TYPES.register("antibiotics", () -> new MedicalFluidType(FluidType.Properties.create(), 0x593f8a));
    public static final RegistryObject<MedicalFluid> ANTIBIOTICS = FLUIDS.register("antibiotics", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ANTIBIOTICS_TYPE, ModFluids.ANTIBIOTICS, ModFluids.ANTIBIOTICS), MedicalEffects.ANTIBIOTICS));

    public static final RegistryObject<FluidType> ANTIVENOM_TYPE = FLUID_TYPES.register("antivenom", () -> new MedicalFluidType(FluidType.Properties.create(), -12792486));
    public static final RegistryObject<MedicalFluid> ANTIVENOM = FLUIDS.register("antivenom", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ANTIVENOM_TYPE, ModFluids.ANTIVENOM, ModFluids.ANTIVENOM), MedicalEffects.ANTIVENOM));

    public static final RegistryObject<FluidType> ANTISERUM_TYPE = FLUID_TYPES.register("antiserum", () -> new MedicalFluidType(FluidType.Properties.create(), 0x6f3582));
    public static final RegistryObject<MedicalFluid> ANTISERUM = FLUIDS.register("antiserum", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ANTISERUM_TYPE, ModFluids.ANTISERUM, ModFluids.ANTISERUM), MedicalEffects.ANTISERUM));

    public static final RegistryObject<FluidType> PROCOAGULANT_TYPE = FLUID_TYPES.register("procoagulant", () -> new MedicalFluidType(FluidType.Properties.create(), 0x57172b));
    public static final RegistryObject<MedicalFluid> PROCOAGULANT = FLUIDS.register("procoagulant", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.PROCOAGULANT_TYPE, ModFluids.PROCOAGULANT, ModFluids.PROCOAGULANT), MedicalEffects.PROCOAGULANT));

    public static final RegistryObject<FluidType> SODIUM_NITROPRUSSIDE_TYPE = FLUID_TYPES.register("sodium_nitroprusside", () -> new MedicalFluidType(FluidType.Properties.create(), FastColor.ARGB32.color(255, 207, 88, 41)));
    public static final RegistryObject<MedicalFluid> SODIUM_NITROPRUSSIDE = FLUIDS.register("sodium_nitroprusside", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SODIUM_NITROPRUSSIDE_TYPE, ModFluids.SODIUM_NITROPRUSSIDE, ModFluids.SODIUM_NITROPRUSSIDE), MedicalEffects.SODIUM_NITROPRUSSIDE));

    public static final RegistryObject<FluidType> VASOPRESSIN_TYPE = FLUID_TYPES.register("vasopressin", () -> new MedicalFluidType(FluidType.Properties.create(), FastColor.ARGB32.color(160, 255, 255, 255)));
    public static final RegistryObject<MedicalFluid> VASOPRESSIN = FLUIDS.register("vasopressin", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.VASOPRESSIN_TYPE, ModFluids.VASOPRESSIN, ModFluids.VASOPRESSIN), MedicalEffects.VASOPRESSIN));

    public static final RegistryObject<FluidType> STREPTOKINASE_TYPE = FLUID_TYPES.register("streptokinase", () -> new MedicalFluidType(FluidType.Properties.create(), 0x0aecfc));
    public static final RegistryObject<MedicalFluid> STREPTOKINASE = FLUIDS.register("streptokinase", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.STREPTOKINASE_TYPE, ModFluids.STREPTOKINASE, ModFluids.STREPTOKINASE), MedicalEffects.STREPTOKINASE));

    public static final RegistryObject<FluidType> SALINE_TYPE = FLUID_TYPES.register("saline", () -> new MedicalFluidType(FluidType.Properties.create(), 0xc9c8c5));
    public static final RegistryObject<MedicalFluid> SALINE = FLUIDS.register("saline", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SALINE_TYPE, ModFluids.SALINE, ModFluids.SALINE), MedicalEffects.SALINE));

    public static final RegistryObject<FluidType> BLOOD_TYPE = FLUID_TYPES.register("blood", () -> new MedicalFluidType(FluidType.Properties.create(), -7862264));
    public static final RegistryObject<MedicalFluid> BLOOD = FLUIDS.register("blood", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.BLOOD_TYPE, ModFluids.BLOOD, ModFluids.BLOOD), MedicalEffects.BLOOD));

    public static final RegistryObject<FluidType> ANTISEPTIC_TYPE = FLUID_TYPES.register("antiseptic", () -> new MedicalFluidType(FluidType.Properties.create(), 0x5a6b45));
    public static final RegistryObject<MedicalFluid> ANTISEPTIC = FLUIDS.register("antiseptic", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.ANTISEPTIC_TYPE, ModFluids.ANTISEPTIC, ModFluids.ANTISEPTIC), MedicalEffects.ANTISEPTIC));

    public static final RegistryObject<FluidType> GROUNDWATER_TYPE = FLUID_TYPES.register("groundwater", () -> new MedicalFluidType(FluidType.Properties.create(), FastColor.ARGB32.color(255, 89, 138, 212)));
    public static final RegistryObject<MedicalFluid> GROUNDWATER = FLUIDS.register("groundwater", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.GROUNDWATER_TYPE, ModFluids.GROUNDWATER, ModFluids.GROUNDWATER), MedicalEffects.GROUNDWATER));


    public static final RegistryObject<FluidType> SOAP_TYPE = FLUID_TYPES.register("soap", () -> new MedicalFluidType(FluidType.Properties.create(), FastColor.ARGB32.color(255, 161, 255, 186)));
    public static final RegistryObject<MedicalFluid> SOAP = FLUIDS.register("soap", () -> new MedicalFluid(new ForgeFlowingFluid.Properties(ModFluids.SOAP_TYPE, ModFluids.SOAP, ModFluids.SOAP), MedicalEffects.SOAP));

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }
}
