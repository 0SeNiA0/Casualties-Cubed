package net.zaharenko424.casualties_cubed.datagen;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.loot.AddFilledToChestsModifier;
import net.zaharenko424.casualties_cubed.loot.AddRandomFillToChestsModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobaLootModifiersProvider extends GlobalLootModifierProvider {

    public ModGlobaLootModifiersProvider(PackOutput output) {
        super(output, CasualtiesCubed.MOD_ID);
    }

    public float sanityScale = 0.9f;
    @Override
    protected void start() {
        add("add_dr", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.12f*sanityScale).build()
                },       // no extra conditions
                ModItems.DRESSING.get()
        ));
        add("add_oclussive_dressing", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.08f*sanityScale).build()
                },       // no extra conditions
                ModItems.PLASTIC_DRESSING.get()
        ));
        add("add_sdressing", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.05f*sanityScale).build()
                },       // no extra conditions
                ModItems.STERILIZED_DRESSING.get()
        ));
        add("add_bandaid", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.225f*sanityScale).build()
                },       // no extra conditions
                ModItems.BAND_AIDS.get()
        ));
        add("add_ice", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.14f*sanityScale).build()
                },       // no extra conditions
                ModItems.ICE_PACK.get()
        ));
        add("add_splint", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.05f*sanityScale).build()
                },       // no extra conditions
                ModItems.SPLINT.get()
        ));
        add("add_tweezers", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.05f*sanityScale).build()
                },       // no extra conditions
                ModItems.TWEEZERS.get()
        ));
        add("add_tourniquet", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.05f*sanityScale).build()
                },       // no extra conditions
                ModItems.TOURNIQUET.get()
        ));
        add("add_old_rag", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.30f*sanityScale).build()
                },       // no extra conditions
                ModItems.OLD_RAG.get()
        ));
        add("add_rippeddressing", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.30f*sanityScale).build()
                },       // no extra conditions
                ModItems.RIPPED_DRESSING.get()
        ));
        add("add_bruisekit", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.10f*sanityScale).build()
                },       // no extra conditions
                ModItems.BRUISE_KIT.get()
        ));
        add("add_algdressing", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.10f*sanityScale).build()
                },       // no extra conditions
                ModItems.ALGANATE_DRESSING.get()
        ));
        add("add_medicalgauze", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.08f*sanityScale).build()
                },       // no extra conditions
                ModItems.MEDICAL_GAUZE.get()
        ));
        add("add_boneweld", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.04f*sanityScale).build()
                },       // no extra conditions
                ModItems.BONE_WELDER.get()
        ));
        add("add_medsuture", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.08f*sanityScale).build()
                },       // no extra conditions
                ModItems.MEDICAL_SUTURE.get()
        ));
        add("add_lrd", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.01f*sanityScale).build()
                },       // no extra conditions
                ModItems.LRD.get()
        ));
        add("add_makeshiftlrd", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.02f*sanityScale).build()
                },       // no extra conditions
                ModItems.MAKESHIFT_LRD.get()
        ));
        add("add_heatpack",new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.14f*sanityScale).build()
                },       // no extra conditions
                ModItems.HEAT_PACK.get()
        ));
        add("add_auto_pump",new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.01f*sanityScale).build()
                },       // no extra conditions
                ModItems.HEAT_PACK.get()
        ));

        // UUUUUUUUUUUUUU RNADOM

        add("add_alcohol", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.09f * sanityScale).build()
                },
                ModItems.ALCOHOL_BOTTLE.get()
        ));

        add("add_antiseptic", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.075f * sanityScale).build()
                },
                ModItems.ANTISEPTIC_SPRAY.get()
        ));

        add("add_relief_cream", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.06f * sanityScale).build()
                },
                ModItems.RELIEF_CREAM_BOTTLE.get()
        ));

        add("add_saline", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.07f * sanityScale).build()
                },
                ModItems.SALINE_SYRINGE.get()
        ));

        add("add_painkillers", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.07f * sanityScale).build()
                },
                ModItems.PainkillersPills.get()
        ));

        add("add_antibiotics", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.06f * sanityScale).build()
                },
                ModItems.ANTIBIOTICS_PILLS.get()
        ));

        add("add_ceftriaxone", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.05f * sanityScale).build()
                },
                ModItems.CEFTRIAXONE_VIAL.get()
        ));

        add("add_naloxone", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.04f * sanityScale).build()
                },
                ModItems.NALOXONE_VIAL.get()
        ));

        add("add_procoagulant", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.04f * sanityScale).build()
                },
                ModItems.PROCOAGULANT_INJECTOR.get()
        ));

        add("add_streptokinase", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.03f * sanityScale).build()
                },
                ModItems.STREPTOKINASE_INJECTOR.get()
        ));

        add("add_antiserum", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.03f * sanityScale).build()
                },
                ModItems.ANTISERUM_INJECTOR.get()
        ));

        add("add_morphine", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.03f * sanityScale).build()
                },
                ModItems.MORPHINE_VIAL.get()
        ));

        add("add_opium", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.02f * sanityScale).build()
                },
                ModItems.OPIUM_VIAL.get()
        ));

        add("add_fentanyl", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.01f * sanityScale).build()
                },
                ModItems.FENTANYL_VIAL.get()
        ));

        add("add_heroin", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.005f * sanityScale).build()
                },
                ModItems.HEROIN_SYRINGE.get()
        ));

        add("add_brain_grow", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.0075f * sanityScale).build()
                },
                ModItems.BRAIN_GROW_PILLS.get()
        ));

        add("add_experimental_treatment", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.0075f * sanityScale).build()
                },
                ModItems.EXPERIMENTAL_TREATMENT.get()
        ));

        add("ohhhh_expie_hiiiiiiiii", new AddFilledToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.01f * sanityScale).build()
                },
                ModItems.ScavPlush.get()
        ));











        add("add_random_vial",new AddRandomFillToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.1f*sanityScale).build()
                },       // no extra conditions
                ModItems.MEDICINE_VIAL.get()
        ));
        add("add_random_syringe",new AddRandomFillToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(.1f*sanityScale).build()
                },       // no extra conditions
                ModItems.MEDICINE_VIAL.get()
        ));
        add("add_random_bottle",new AddRandomFillToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(.1f*sanityScale).build()
                },       // no extra conditions
                ModItems.BOTTLE.get()
        ));
        add("add_random_pill",new AddRandomFillToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(.1f*sanityScale).build()
                },       // no extra conditions
                ModItems.PILL_BOTTLE.get()
        ));
        add("add_random_injector",new AddRandomFillToChestsModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(.1f*sanityScale).build()
                },       // no extra conditions
                ModItems.AUTO_INJECTOR.get()
        ));



    }
}
