package net.adinvas.casualties_cubed.registry;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.item.misc.BrownCapMushItem;
import net.adinvas.casualties_cubed.item.misc.ExperimentalTreatmentItem;
import net.adinvas.casualties_cubed.item.multi_tank.*;
import net.adinvas.casualties_cubed.item.special.bag.LargeMedibagItem;
import net.adinvas.casualties_cubed.item.special.bag.MediumMedibagItem;
import net.adinvas.casualties_cubed.item.special.bag.SmallMedibagItem;
import net.adinvas.casualties_cubed.item.bandages.*;
import net.adinvas.casualties_cubed.item.misc.BrownCapItem;
import net.adinvas.casualties_cubed.item.reusable.SplintItem;
import net.adinvas.casualties_cubed.item.reusable.TourniquetItem;
import net.adinvas.casualties_cubed.item.reusable.TweezersItem;
import net.adinvas.casualties_cubed.item.usable.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<Item> OLD_RAG = ITEMS.register("old_rag", OldRagItem::new);
    public static final RegistryObject<Item> RIPPED_DRESSING = ITEMS.register("ripped_dressing", RippedDressingItem::new);
    public static final RegistryObject<Item> BAND_AIDS = ITEMS.register("band_aids", BandAidItem::new);
    public static final RegistryObject<Item> DRESSING = ITEMS.register("dressing", DressingItem::new);
    public static final RegistryObject<Item> BRUISE_KIT = ITEMS.register("bruise_kit", BruiseKitItem::new);
    public static final RegistryObject<Item> ALGANATE_DRESSING = ITEMS.register("alganate_dressing", AlganiteDressingItem::new);
    public static final RegistryObject<Item> MEDICAL_GAUZE = ITEMS.register("medical_gauze", MedicalGauzeItem::new);
    public static final RegistryObject<Item> PLASTIC_DRESSING = ITEMS.register("plastic_dressing", PlasticDressingItem::new);
    public static final RegistryObject<Item> STERILIZED_DRESSING = ITEMS.register("sterilized_dressing", SterilizedDressingItem::new);

    public static final RegistryObject<Item> BONE_WELDER = ITEMS.register("bone_welder", BoneWeldingItem::new);//bone_welding -> bone_welder
    public static final RegistryObject<Item> LRD = ITEMS.register("lrd", LRDItem::new);
    public static final RegistryObject<Item> MAKESHIFT_LRD = ITEMS.register("makeshift_lrd", MakeshiftLRDItem::new);
    public static final RegistryObject<Item> MEDICAL_SUTURE = ITEMS.register("medical_suture", MedicalSutureItem::new);
    public static final RegistryObject<Item> ICE_PACK = ITEMS.register("ice_pack", IcePackItem::new);
    public static final RegistryObject<Item> HEAT_PACK = ITEMS.register("heat_pack", HeatPackItem::new);
    public static final RegistryObject<Item> GLOW_FRUIT = ITEMS.register("glow_fruit", () -> new GlowFruitItem(ModBlocks.GLOW_FRUIT_BUSH.get()));

    public static final RegistryObject<Item> SmallMedibag = ITEMS.register("small_medibag", SmallMedibagItem::new);
    public static final RegistryObject<Item> MediumMedibag = ITEMS.register("medium_medibag", MediumMedibagItem::new);
    public static final RegistryObject<Item> LargeMedibag = ITEMS.register("large_medibag", LargeMedibagItem::new);

    public static final RegistryObject<Item> SPLINT = ITEMS.register("splint", SplintItem::new);
    public static final RegistryObject<Item> TWEEZERS = ITEMS.register("tweezers", TweezersItem::new);
    public static final RegistryObject<Item> TOURNIQUET = ITEMS.register("tourniquet", TourniquetItem::new);

    public static final RegistryObject<Item> BrownCap = ITEMS.register("brown_cap", () -> new BrownCapItem(ModBlocks.BROWN_CAP.get()));
    public static final RegistryObject<Item> BROWN_CAP_MUSH = ITEMS.register("brown_cap_mush", BrownCapMushItem::new);
    public static final RegistryObject<Item> EXPERIMENTAL_TREATMENT = ITEMS.register("experimental_treatment", ExperimentalTreatmentItem::new);
    public static final RegistryObject<Item> AUTO_PUMP = ITEMS.register("auto_pump", AutoPumpItem::new);

    public static final RegistryObject<Item> THERMOMETER = ITEMS.register("thermometer", ThermometerItem::new);
    public static final RegistryObject<Item> SimpleEarProtection = ITEMS.register("simple_ear_protection", net.adinvas.casualties_cubed.item.special.SimpleEarProtection::new);

    //public static final RegistryObject<Item> MultiTank = ITEMS.register("testtank2", MultiTankFluidItem::new);
    public static final RegistryObject<Item> MEDICINE_VIAL = ITEMS.register("medicine_vial", MedicineVialItem::new);
    public static final RegistryObject<Item> BOTTLE = ITEMS.register("bottle", BottleItem::new);
    public static final RegistryObject<Item> SYRINGE = ITEMS.register("syringe", SyringeItem::new);
    public static final RegistryObject<Item> AUTO_INJECTOR = ITEMS.register("auto_injector",AutoInjectorItem::new);//autoinjector -> auto_injector
    public static final RegistryObject<Item> PILL_BOTTLE = ITEMS.register("pill_bottle",PillContainerItem::new);

    public static final RegistryObject<Item> ALCOHOL_BOTTLE = ITEMS.register("alcohol", AlcoholBottleItem::new);
    public static final RegistryObject<Item> ANTISERUM_INJECTOR = ITEMS.register("antiserum",AntiserumInjectorItem::new);
    public static final RegistryObject<Item> ANTIBIOTICS_PILLS = ITEMS.register("antibiotics",AntibioticsItem::new);
    public static final RegistryObject<Item> ANTISEPTIC_SPRAY = ITEMS.register("antiseptic",AntisepticSprayItem::new);
    public static final RegistryObject<Item> PROCOAGULANT_INJECTOR = ITEMS.register("blood_clotting",ProcoagulantInjectorItem::new);
    public static final RegistryObject<Item> STREPTOKINASE_INJECTOR = ITEMS.register("blood_thinner",StreptokinaseInjectorItem::new);
    public static final RegistryObject<Item> BRAIN_GROW_PILLS = ITEMS.register("brain_grow",BrainGrowPillItem::new);
    public static final RegistryObject<Item> CEFTRIAXONE_VIAL = ITEMS.register("ceftriaxone", CeftriaxoneVialItem::new);
    public static final RegistryObject<Item> FENTANYL_VIAL = ITEMS.register("fentanyl_vial",FentanylVialItem::new);
    public static final RegistryObject<Item> HEROIN_SYRINGE = ITEMS.register("heroin_vial",HeroinSyringeItem::new);
    public static final RegistryObject<Item> MORPHINE_VIAL = ITEMS.register("morphine_vial",MorphineVialItem::new);
    public static final RegistryObject<Item> NALOXONE_VIAL = ITEMS.register("naloxone_vial",NaloxoneVialItem::new);
    public static final RegistryObject<Item> OPIUM_VIAL = ITEMS.register("opium_vial",OpiumVialItem::new);
    public static final RegistryObject<Item> PainkillersPills = ITEMS.register("painkillers",PainkillersPillItem::new);
    public static final RegistryObject<Item> REACTION_LIQUID_VIAL = ITEMS.register("reaction_vial",ReactionLiquidVial::new);
    public static final RegistryObject<Item> SALINE_SYRINGE = ITEMS.register("saline",SalineSyringeItem::new);
    public static final RegistryObject<Item> RELIEF_CREAM_BOTTLE = ITEMS.register("relief_cream",ReliefCreamBottle::new);

    public static final RegistryObject<Item> ScavPlush = ITEMS.register("scav_plushie",()->
            new BlockItem(ModBlocks.SCAV_BLOCK.get(),new Item.Properties()));

    public static final RegistryObject<Item> MedicalMixer = ITEMS.register("medical_mixer",()->
            new BlockItem(ModBlocks.MEDICAL_MIXER.get(),new Item.Properties()));
}
