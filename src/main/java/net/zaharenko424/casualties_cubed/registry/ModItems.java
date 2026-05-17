package net.zaharenko424.casualties_cubed.registry;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.item.bandages.*;
import net.zaharenko424.casualties_cubed.item.misc.BrownCapMushItem;
import net.zaharenko424.casualties_cubed.item.misc.ExperimentalTreatmentItem;
import net.zaharenko424.casualties_cubed.item.multi_tank.*;
import net.zaharenko424.casualties_cubed.item.special.bag.LargeMedibagItem;
import net.zaharenko424.casualties_cubed.item.special.bag.MediumMedibagItem;
import net.zaharenko424.casualties_cubed.item.special.bag.SmallMedibagItem;
import net.zaharenko424.casualties_cubed.item.misc.BrownCapItem;
import net.zaharenko424.casualties_cubed.item.reusable.SplintItem;
import net.zaharenko424.casualties_cubed.item.reusable.TourniquetItem;
import net.zaharenko424.casualties_cubed.item.reusable.TweezersItem;
import net.zaharenko424.casualties_cubed.item.usable.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<Item> OLD_RAG = ITEMS.register("old_rag", OldRagItem::new);
    public static final RegistryObject<Item> RIPPED_DRESSING = ITEMS.register("ripped_dressing", RippedDressingItem::new);
    public static final RegistryObject<Item> ADHESIVE_BANDAGE = ITEMS.register("adhesive_bandage", AdhesiveBandage::new);
    public static final RegistryObject<Item> DRESSING = ITEMS.register("dressing", DressingItem::new);
    public static final RegistryObject<Item> BRUISE_KIT = ITEMS.register("bruise_kit", BruiseKitItem::new);
    public static final RegistryObject<Item> ALGINATE_DRESSING = ITEMS.register("alginate_dressing", AlginateDressingItem::new);
    public static final RegistryObject<Item> MEDICAL_GAUZE = ITEMS.register("medical_gauze", MedicalGauzeItem::new);
    public static final RegistryObject<Item> PLASTIC_DRESSING = ITEMS.register("plastic_dressing", PlasticDressingItem::new);
    public static final RegistryObject<Item> STERILIZED_DRESSING = ITEMS.register("sterilized_dressing", SterilizedDressingItem::new);

    public static final RegistryObject<Item> BONE_WELDER = ITEMS.register("bone_welder", BoneWeldingItem::new);//bone_welding -> bone_welder
    public static final RegistryObject<LRDItem> LRD = ITEMS.register("lrd", LRDItem::new);
    public static final RegistryObject<MakeshiftLRDItem> MAKESHIFT_LRD = ITEMS.register("makeshift_lrd", MakeshiftLRDItem::new);
    public static final RegistryObject<Item> MEDICAL_SUTURE = ITEMS.register("medical_suture", MedicalSutureItem::new);
    public static final RegistryObject<Item> ICE_PACK = ITEMS.register("ice_pack", IcePackItem::new);
    public static final RegistryObject<Item> HEAT_PACK = ITEMS.register("heat_pack", HeatPackItem::new);
    public static final RegistryObject<Item> GLOW_FRUIT = ITEMS.register("glow_fruit", () -> new GlowFruitItem(ModBlocks.GLOW_FRUIT_BUSH.get()));

    public static final RegistryObject<Item> SMALL_MEDIBAG = ITEMS.register("small_medibag", SmallMedibagItem::new);
    public static final RegistryObject<Item> MEDIUM_MEDIBAG = ITEMS.register("medium_medibag", MediumMedibagItem::new);
    public static final RegistryObject<Item> LARGE_MEDIBAG = ITEMS.register("large_medibag", LargeMedibagItem::new);

    public static final RegistryObject<Item> SPLINT = ITEMS.register("splint", SplintItem::new);
    public static final RegistryObject<Item> TWEEZERS = ITEMS.register("tweezers", TweezersItem::new);
    public static final RegistryObject<Item> TOURNIQUET = ITEMS.register("tourniquet", TourniquetItem::new);

    public static final RegistryObject<Item> BROWN_CAP = ITEMS.register("brown_cap", () -> new BrownCapItem(ModBlocks.BROWN_CAP.get()));
    public static final RegistryObject<Item> BROWN_CAP_MUSH = ITEMS.register("brown_cap_mush", BrownCapMushItem::new);
    public static final RegistryObject<Item> EXPERIMENTAL_TREATMENT = ITEMS.register("experimental_treatment", ExperimentalTreatmentItem::new);
    public static final RegistryObject<Item> AUTO_PUMP = ITEMS.register("auto_pump", AutoPumpItem::new);

    public static final RegistryObject<Item> THERMOMETER = ITEMS.register("thermometer", ThermometerItem::new);
    public static final RegistryObject<Item> SimpleEarProtection = ITEMS.register("simple_ear_protection", net.zaharenko424.casualties_cubed.item.special.SimpleEarProtection::new);

    //public static final RegistryObject<Item> MultiTank = ITEMS.register("testtank2", MultiTankFluidItem::new);
    public static final RegistryObject<MedicineVialItem> MEDICINE_VIAL = ITEMS.register("medicine_vial", MedicineVialItem::new);
    public static final RegistryObject<BottleItem> BOTTLE = ITEMS.register("bottle", BottleItem::new);
    public static final RegistryObject<SyringeItem> SYRINGE = ITEMS.register("syringe", SyringeItem::new);
    public static final RegistryObject<AutoInjectorItem> AUTO_INJECTOR = ITEMS.register("auto_injector", AutoInjectorItem::new);//autoinjector -> auto_injector
    public static final RegistryObject<PillContainerItem> PILL_BOTTLE = ITEMS.register("pill_bottle", PillContainerItem::new);

    public static final RegistryObject<AlcoholBottleItem> ALCOHOL_BOTTLE = ITEMS.register("alcohol_bottle", AlcoholBottleItem::new);
    public static final RegistryObject<AntiserumInjectorItem> ANTISERUM_INJECTOR = ITEMS.register("antiserum_injector", AntiserumInjectorItem::new);
    public static final RegistryObject<AntibioticsItem> ANTIBIOTICS_PILLS = ITEMS.register("antibiotics_pill_bottle", AntibioticsItem::new);
    public static final RegistryObject<AntisepticSprayItem> ANTISEPTIC_SPRAY = ITEMS.register("antiseptic_spray_bottle", AntisepticSprayItem::new);
    public static final RegistryObject<ProcoagulantInjectorItem> PROCOAGULANT_INJECTOR = ITEMS.register("procoagulant_injector", ProcoagulantInjectorItem::new);
    public static final RegistryObject<StreptokinaseInjectorItem> STREPTOKINASE_INJECTOR = ITEMS.register("streptokinase_injector", StreptokinaseInjectorItem::new);
    public static final RegistryObject<BrainGrowPillItem> BRAIN_GROW_PILLS = ITEMS.register("brain_grow_pill_bottle", BrainGrowPillItem::new);
    public static final RegistryObject<CeftriaxoneVialItem> CEFTRIAXONE_VIAL = ITEMS.register("ceftriaxone_vial", CeftriaxoneVialItem::new);
    public static final RegistryObject<FentanylVialItem> FENTANYL_VIAL = ITEMS.register("fentanyl_vial", FentanylVialItem::new);
    public static final RegistryObject<HeroinSyringeItem> HEROIN_SYRINGE = ITEMS.register("heroin_syringe", HeroinSyringeItem::new);
    public static final RegistryObject<MorphineVialItem> MORPHINE_VIAL = ITEMS.register("morphine_vial", MorphineVialItem::new);
    public static final RegistryObject<NaloxoneVialItem> NALOXONE_VIAL = ITEMS.register("naloxone_vial", NaloxoneVialItem::new);
    public static final RegistryObject<OpiumVialItem> OPIUM_VIAL = ITEMS.register("opium_vial", OpiumVialItem::new);
    public static final RegistryObject<PainkillersPillItem> PAINKILLERS_PILLS = ITEMS.register("painkillers_pill_bottle", PainkillersPillItem::new);
    public static final RegistryObject<SalineSyringeItem> SALINE_SYRINGE = ITEMS.register("saline_bag", SalineSyringeItem::new);
    public static final RegistryObject<ReliefCreamBottle> RELIEF_CREAM_BOTTLE = ITEMS.register("relief_cream_bottle", ReliefCreamBottle::new);

    public static final RegistryObject<Item> EXPIE_PLUSHY = ITEMS.register("expie_plushy", () ->
            new BlockItem(ModBlocks.EXPIE_PLUSHY.get(), new Item.Properties()));

    public static final RegistryObject<Item> MEDICAL_MIXER = ITEMS.register("medical_mixer", () ->
            new BlockItem(ModBlocks.MEDICAL_MIXER.get(), new Item.Properties()));
}
