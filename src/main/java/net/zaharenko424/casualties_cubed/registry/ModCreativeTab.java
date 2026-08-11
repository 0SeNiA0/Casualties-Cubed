package net.zaharenko424.casualties_cubed.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;

import static net.zaharenko424.casualties_cubed.registry.ModItems.*;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<CreativeModeTab> YOUR_TAB = CREATIVE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.casualties_cubed_tab")) // lang key
                    .icon(() -> new ItemStack(DRESSING.get())) // icon for the tab
                    .displayItems((parameters, output) -> {

                        output.accept(GLOW_FRUIT.get());
                        output.accept(BROWN_CAP.get());
                        output.accept(BROWN_CAP_MUSH.get());
                        output.accept(EXPERIMENTAL_TREATMENT.get());

                        output.accept(SMALL_MEDIBAG.get());
                        output.accept(MEDIUM_MEDIBAG.get());
                        output.accept(LARGE_MEDIBAG.get());

                        output.accept(OLD_RAG.get());
                        output.accept(RIPPED_DRESSING.get());
                        output.accept(ADHESIVE_BANDAGE.get());
                        output.accept(DRESSING.get());
                        output.accept(PLASTIC_DRESSING.get());
                        output.accept(STERILIZED_DRESSING.get());
                        output.accept(ALGINATE_DRESSING.get());
                        output.accept(MEDICAL_GAUZE.get());
                        output.accept(MEDICAL_SUTURE.get());

                        output.accept(BRUISE_KIT.get());
                        output.accept(HEAT_PACK.get());
                        output.accept(ICE_PACK.get());
                        output.accept(BONE_WELDER.get());
                        output.accept(MAKESHIFT_LRD.get());
                        output.accept(LRD.get());
                        output.accept(MAKESHIFT_LRD.get().withFluid(ModFluids.LRD_SERUM));
                        output.accept(LRD.get().withFluid(ModFluids.LRD_SERUM));
                        output.accept(AUTO_PUMP.get());
                        output.accept(CHEST_DRAIN.get());

                        output.accept(SPLINT.get());
                        output.accept(TWEEZERS.get());
                        output.accept(TOURNIQUET.get());
                        output.accept(THERMOMETER.get());

                        //Empty
                        output.accept(CANTEEN.get());
                        output.accept(WATER_BOTTLE.get());
                        output.accept(WATER_JUG.get());
                        output.accept(ALCOHOL_BOTTLE.get());
                        output.accept(RELIEF_CREAM_BOTTLE.get());
                        output.accept(ANTISEPTIC_SPRAY.get());
                        output.accept(WOUND_GLUE_SPRAY.get());

                        output.accept(BRAIN_GROW_PILLS.get());
                        output.accept(ANTIBIOTICS_PILLS.get());
                        output.accept(PAINKILLERS_PILLS.get());
                        output.accept(PILL_BOTTLE.get());

                        output.accept(CEFTRIAXONE_VIAL.get());
                        output.accept(OPIUM_VIAL.get());
                        output.accept(MORPHINE_VIAL.get());
                        output.accept(FENTANYL_VIAL.get());
                        output.accept(NALOXONE_VIAL.get());
                        output.accept(MEDICINE_VIAL.get());

                        output.accept(AUTO_INJECTOR.get());
                        output.accept(PROCOAGULANT_INJECTOR.get());
                        output.accept(STREPTOKINASE_INJECTOR.get());
                        output.accept(ANTISERUM_INJECTOR.get());

                        output.accept(SYRINGE.get());
                        output.accept(HEROIN_SYRINGE.get());
                        output.accept(IV_BAG.get());
                        output.accept(BLOOD_BAG.get());

                        output.accept(BLEACH_JUG.get());

                        output.accept(COMBAT_PEN.get());

                        //Filled
                        output.accept(MEDICAL_MIXER.get());

                        output.accept(ALCOHOL_BOTTLE.get().withDefFluid());
                        output.accept(RELIEF_CREAM_BOTTLE.get().withDefFluid());
                        output.accept(ANTISEPTIC_SPRAY.get().withDefFluid());
                        output.accept(WOUND_GLUE_SPRAY.get().withDefFluid());

                        output.accept(BRAIN_GROW_PILLS.get().withDefFluid());
                        output.accept(ANTIBIOTICS_PILLS.get().withDefFluid());
                        output.accept(PAINKILLERS_PILLS.get().withDefFluid());

                        output.accept(CEFTRIAXONE_VIAL.get().withDefFluid());
                        output.accept(OPIUM_VIAL.get().withDefFluid());
                        output.accept(MORPHINE_VIAL.get().withDefFluid());
                        output.accept(FENTANYL_VIAL.get().withDefFluid());
                        output.accept(NALOXONE_VIAL.get().withDefFluid());
                        output.accept(MEDICINE_VIAL.get().withFluid(ModFluids.BIO_CHEM));

                        output.accept(PROCOAGULANT_INJECTOR.get().withDefFluid());
                        output.accept(STREPTOKINASE_INJECTOR.get().withDefFluid());
                        output.accept(ANTISERUM_INJECTOR.get().withDefFluid());

                        output.accept(HEROIN_SYRINGE.get().withDefFluid());
                        output.accept(IV_BAG.get().withDefFluid());
                        output.accept(BLOOD_BAG.get().withDefFluid());

                        output.accept(BLEACH_JUG.get().withDefFluid());

                        output.accept(COMBAT_PEN.get().withDefFluid());


                        output.accept(EXPIE_PLUSHY.get());
                        output.accept(SimpleEarProtection.get());
                    })
                    .build()
    );
}
