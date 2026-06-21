package net.zaharenko424.casualties_cubed.datagen.lang;

import net.minecraft.data.PackOutput;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.ModDamageTypes;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.registry.ModBlocks;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static net.zaharenko424.casualties_cubed.registry.ModItems.*;

public class ENLanguageProvider extends LanguageProvider {

    public ENLanguageProvider(PackOutput output) {
        super(output, CasualtiesCubed.MOD_ID, "en_us");
    }

    protected void addLimbs() {
        for (Limb limb : Limb.values()) {
            add(CasualtiesCubed.MOD_ID + ".limb." + limb.name().toLowerCase(), Arrays.stream(limb.name().toLowerCase().split("_"))
                    .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                    .collect(Collectors.joining(" ")));
        }
    }

    @Override
    protected void addTranslations() {
        addLimbs();

        addBlockFromId(ModBlocks.BROWN_CAP);
        addBlockFromId(ModBlocks.GLOW_FRUIT_BUSH);
        addBlockFromId(ModBlocks.EXPIE_PLUSHY);
        addBlockFromId(ModBlocks.MEDICAL_MIXER);

        add("itemGroup.casualties_cubed_tab", "Casualties: Cubed");

        //Unused
        add("item.casualties_cubed.aid_gel", "Relief Gel");
        add("item.casualties_cubed.aid_gel.description", "A crudely made gel for aiding sore limbs.");

        addItemWDesc(DRESSING, "Bandage", "Basic bandage for covering and protecting wounds.");
        addItemFromIdWDesc(PLASTIC_DRESSING, "Stronger bandage that protects wounds more effectively.");
        addItemFromIdWDesc(STERILIZED_DRESSING, "Cleans and covers wounds, preventing infection.");
        addItemFromIdWDesc(CANTEEN, "Simple, small bottle for holding 300mb of liquid.");
        addItemFromIdWDesc(WATER_BOTTLE, "A plastic bottle that can hold 0.5B of liquid.");
        addItemFromIdWDesc(WATER_JUG, "Big, heavy plastic bottle. Stores 3B of liquid.");
        addItemFromIdWDesc(ALCOHOL_BOTTLE, "A glass bottle that can hold 0.5B of liquid.");
        addItemFromIdWDesc(ICE_PACK, "Reduces swelling and eases sore muscles.");
        addItemWDesc(IV_BAG, "IV Bag", "A 750mb fluid bag with a needle at the end.");
        addItemWDesc(OPIUM_VIAL, "Poppy Extract Vial", "A 100mb bottle. The overdose label says 200mb.");
        addItemFromIdWDesc(MORPHINE_VIAL, "A 100mb bottle. The overdose label says 90mb.");
        addItemFromIdWDesc(HEROIN_SYRINGE, "A 150mb syringe for intravenous injection. The dosage marks are scratched off.");
        addItemFromIdWDesc(FENTANYL_VIAL, "A 100mb bottle containing diluted fentanyl for pain relief. The overdose label says 20mb.");
        addItemFromIdWDesc(NALOXONE_VIAL, "A 100mb bottle. The label reads \"Nal...\" — the rest is unreadable.");
        addItemFromIdWDesc(PAINKILLERS_PILLS, "A 100mb pill bottle. The dosage is 10mb.");
        addItemFromIdWDesc(ANTIBIOTICS_PILLS, "A 100mb bottle of antibiotic pills. The dose is 10mb.");
        addItemFromIdWDesc(PROCOAGULANT_INJECTOR, "A 100mb bottle with an autoinjector attached. The dosage is 34mb.");
        addItemFromIdWDesc(WOUND_GLUE_SPRAY, "A 80mb spray bottle. Sprays 20mb of liquid at a time.");
        addItemFromIdWDesc(STREPTOKINASE_INJECTOR, "A 100mb bottle with an autoinjector attached. The dosage is 34mb.");
        addItemFromIdWDesc(SPLINT, "Stabilizes broken bones and aids healing.");
        addItemFromIdWDesc(TWEEZERS, "Removes shrapnel, glass, or debris from wounds.");
        addItemFromIdWDesc(TOURNIQUET, "Stops heavy bleeding but limits blood flow.");
        addItemWDesc(SMALL_MEDIBAG, "Small Medical Bag", "A compact pouch for four medical items.");
        addItemWDesc(MEDIUM_MEDIBAG, "Medium Medical Bag", "A balanced pack with room for eight items.");
        addItemWDesc(LARGE_MEDIBAG, "Large Medical Bag", "A spacious medical kit that can hold up to twelve items.");
        addItemFromIdWDesc(ADHESIVE_BANDAGE, "Simple adhesive bandages usually used on small cuts and bruises.");
        addItemFromIdWDesc(CEFTRIAXONE_VIAL, "A 100mb bottle. The safe dose label says \"may vary.\"");
        addItemFromIdWDesc(BLEACH_JUG, "A 1.5B jug for storing bleach. Applies 100mb of liquid at a time.");
        addItemFromIdWDesc(MEDICINE_VIAL, "A 100mb bottle meant for medicine.");
        addItemFromIdWDesc(ANTISEPTIC_SPRAY, "A 200mb spray bottle. Sprays 10mb of liquid at a time.");
        addItemFromIdWDesc(RELIEF_CREAM_BOTTLE, "A 200mb bottle. The label warns, \"Do not use more than 10 bottles in one day.\"");
        addItemFromIdWDesc(ANTISERUM_INJECTOR, "A 100mb bottle with an autoinjector attached. The dosage is 50mb.");
        addItemWDesc(BRAIN_GROW_PILLS, "BrainGrow Pill Bottle", "A 50mb bottle. The label says, \"Take up to one dose per day.\"");
        addItemFromIdWDesc(SYRINGE, "A 100mb syringe for intravenous injection.");
        addItemFromIdWDesc(OLD_RAG, "A worn piece of cloth. Can be used as a makeshift dressing.");
        addItemFromIdWDesc(RIPPED_DRESSING, "A worn piece of what used to be a bandage. Can be used as a makeshift dressing.");
        addItemFromIdWDesc(BRUISE_KIT, "A combination of various medicines, made to aid sore muscles. Cannot stop the bleeding.");
        addItemFromIdWDesc(ALGINATE_DRESSING, "A specially treated piece of a dressing. Slows down bleeding and highly improves skin healing.");
        addItemFromIdWDesc(MEDICAL_GAUZE, "Woven gauze containing painkilling drugs. Decent at slowing bleeding and reducing pain from injuries.");
        addItemWDesc(BONE_WELDER, "Bone Welding Tool", "An experimental piece of technology. Welds the bone structure back together. Causes some damage on the way in.");
        addItemWDesc(LRD, "L.R.D", "A Localized Resuscitation Device, made to easily provide effective general trauma care. Many thank their lives to this piece of machinery. Holds 75mb of fluid. Needs at least 25mb of LRD serum to function");
        addItemWDesc(MAKESHIFT_LRD, "Makeshift L.R.D", "A Localized Resuscitation Device, made to easily provide effective general trauma care. This one seems to be crudely made. Holds 50mb of fluid. Needs at least 25mb of LRD serum to function");
        addItemFromIdWDesc(MEDICAL_SUTURE, "Used to stitch wounds back together. Instantly stops most of the bleeding.");
        addItemFromIdWDesc(HEAT_PACK, "A slightly larger chemical hand warmer. Aids in healing of muscles slightly.");
        addItemFromIdWDesc(AUTO_PUMP, "A very advanced Life support device with internal batteries. Keeps oxygen at stable levels,provides adrenaline and energy. Batteries last about 5min. Usable on Chest");
        addItemFromIdWDesc(THERMOMETER, "A small device that reads the temperature around the user.");
        addItemFromId(SimpleEarProtection);
        addItemFromIdWDesc(PILL_BOTTLE, "A small pill container, the dosage is 10mb.");
        addItemFromIdWDesc(AUTO_INJECTOR, "A 100mb bottle with an autoinjector attached. The dosage is 34mb.");
        addItemFromIdWDesc(GLOW_FRUIT, "A bioluminescent plant often found in caves. Counteracts infections but is highly toxic when ingested.");
        add(BROWN_CAP.getId().toLanguageKey("item", "description"), "You have no idea what is this fungus. For all you know it can cure cancer or kill you on the spot");
        addItemFromIdWDesc(BROWN_CAP_MUSH, "Probably a bad idea to eat this...");
        addItemFromId(EXPERIMENTAL_TREATMENT);
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "description1"), "The label says:");
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "description2"), "EXPERIMENTAL TREATMENT EX-8UN, For authorized Study participants only. Effects may include:");
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "description3"), "Mild dizziness ;Permanent dizziness ;Energy ;Lethargy ;Metallic taste ;Sudden bleeding ;Cardiac arrest;Minor brain damage ;Major brain damage ; Respiratory arrest; Deja vu; Paranoia; Loss of limbs; Grow of limbs; Hunger; Vomiting; Deja vu; Memory loss...");
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "description4"), "The list goes on for another 2 lines.");
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "doom"), "You feel a sudden sense of Doom");

        addMedicalFluidFromIdWDesc(ModFluids.OPIUM, "A light opiate, lesser effect when taken orally");
        addMedicalFluidFromIdWDesc(ModFluids.MORPHINE, "A strong opiate, lesser effect when taken orally");
        addMedicalFluidFromIdWDesc(ModFluids.FENTANYL, "Highly dangerous opiate. 100 times stronger than morphine");
        addMedicalFluidFromIdWDesc(ModFluids.HEROIN, "A very strong opiate. Very addicting");
        addMedicalFluidFromIdWDesc(ModFluids.CLEAN_WATER, "Tasteless");
        addMedicalFluidFromIdWDesc(ModFluids.PAINKILLERS, "Medication for pain relief. Overdose at 80mb");
        addMedicalFluidWDesc(ModFluids.BRAINGROW, "BrainGrow", "Special medicine used for heavy concussions and brain trauma. May cause discomfort. Meant to be taken orally");
        addMedicalFluidFromIdWDesc(ModFluids.ALCOHOL, "Highly distilled spirit. Useful for disinfecting wounds.");
        addMedicalFluidFromIdWDesc(ModFluids.ANTISEPTIC, "A potent disinfecting gel. Stings a lot.");
        addMedicalFluidFromIdWDesc(ModFluids.SALINE, "A mix of water, salt, and minerals. Useful for thirst and regaining body liquid.");
        addMedicalFluidFromIdWDesc(ModFluids.BLOOD, "Used in treating hypovolemia.");
        addMedicalFluidFromIdWDesc(ModFluids.ANTIBIOTICS, "Pills that increase your immunity to infection. For some reason they taste like nickels.");
        addMedicalFluidFromIdWDesc(ModFluids.ANTISERUM, "Blood serum containing antibodies. Helps when fighting infections. Slightly increases blood volume.");
        addMedicalFluidFromIdWDesc(ModFluids.CEFTRIAXONE, "Very advanced form of injected antibiotic. May cause excruciating chest pain.");
        addMedicalFluidFromIdWDesc(ModFluids.BLEACH, "Chemical product used to remove color from fiber, or to disinfect. Can be used to disinfect wounds. Drinking is lethal.");
        addMedicalFluidFromIdWDesc(ModFluids.PROCOAGULANT, "A simple blood-clotting agent. Decreases bleeding overall but causes clotting. Dangerous in excess.");
        addMedicalFluidFromIdWDesc(ModFluids.WOUND_GLUE, "A hemostatic, antibiotic glue-like substance. Fights infection, promotes healing and is very effective at sealing bleeding wounds. Might cause circulation problems - use with care.");
        addMedicalFluidFromIdWDesc(ModFluids.STREPTOKINASE, "A blood thinner. Decreases blood clotting. May cause increased bleeding.");
        addMedicalFluidFromIdWDesc(ModFluids.NALOXONE, "A common opiate antagonist. Used to reverse opioid overdose.");
        addMedicalFluidFromIdWDesc(ModFluids.RELIEF_CREAM, "A slightly antiseptic cream with soothing properties.");
        addMedicalFluidFromIdWDesc(ModFluids.REACTION_LIQUID, "Slightly acidic liquid made from glow fruits. Has some reactive properties.");
        addMedicalFluidFromIdWDesc(ModFluids.CHOCOLATE_MILK, "Who doesn't like chocolate milk?");
        addMedicalFluidWDesc(ModFluids.LRD_SERUM, "LRD Serum", "Special mixture used by a Localized Resuscitation Device. Does nothing when injected with other means. Unpleasant taste.");
        addMedicalFluidFromIdWDesc(ModFluids.ANTIVENOM, "A treatment for envenomation, it's composed of antibodies that disable hemotoxins in the bloodstream. Works intravenously.");

        add("key.casualties_cubed.open_pain_gui", "Open Health Screen");
        add("key.casualties_cubed.give_up", "Give Up");
        add("key.categories.casualties_cubed", "Casualties: Cubed");

        addGuiO("tourniquet_button", "Remove Tourniquet");
        addGuiO("dislocation_button", "Fix Dislocation");
        addGuiO("shrapnel_button", "Remove Shrapnel");
        addGuiO("splint_button", "Remove Splint");
        addGuiO("inject", "Inject");
        addGuiO("transfer", "Transfer");
        addGuiO("give_up", "Press %1$s to Give Up.");
        addGuiO("fluid_screen", "Press %1$s to transfer fluids");
        addGuiO("fluid_exchange", "Fluid Exchange");
        addGuiO("fluid_transfer", "Fluid Transfer");
        addGuiO("syringe_instruction", "Push the syringe down to inject. Make sure to check the dosage.");
        addGuiO("minigame_exit", "Press ESC to Exit");
        addGuiO("dislocation_instruction1", "Hit the bone into the correct spot to fix dislocation.");
        addGuiO("shrapnel_instruction", "Slowly extract shrapnel from thie limb. Use tweezers to ignore the speed restriction");
        addGuiO("cpr_instruction1", "Preform chest compressions to the rythm of the timer.");
        addGuiO("cpr_instruction2", "Don't mess up.");
        addGuiO("bandage_instruction1", "Wrap the bandage around the limb to stop the bleeding.");
        addGuiO("bandage_instruction2", " You dont have to use the entire bandage.");
        addGuiO("amputation_instruction", "Saw through the limb to complete the amputation.");
        addGuiO("minigame.bandage", "Bandage");
        addGuiO("minigame.cpr", "CPR");
        addGuiO("minigame.dislocation", "Dislocation");
        addGuiO("minigame.shrapnel", "Shrapnel");
        addGuiO("minigame.inject", "Injection");
        addGuiO("minigame.amputation", "Amputation");
        addGuiO("fluid_amount", "%1$smb");
        addGuiO("hud.oxygen", "O₂ %1$s%%");
        addGuiO("hud.pain", "%1$s%%");
        addGuiO("health.conscious", "CONSCIOUS");
        addGuiO("health.pain_percent", "%1$s%% PAIN");
        addGuiO("health.skin", "SKIN");
        addGuiO("health.muscle", "MUSCLE");
        addGuiO("health.fracture_short", "FRACT");
        addGuiO("health.dislocation_short", "DISL");
        addGuiO("health.blood_volume", "%1$sB");
        addGuiO("health.bleed_rate", "%1$sB/m");
        addGuiO("temperature_celsius", "%1$sC");

        addContainer("looting", "Looting %1$s");

        addDeathMessage(ModDamageTypes.GIVE_UP.location().getPath(), "%1$s gave up", null, "%1$s gave up, with some encouragement from %2$s");
        addDeathMessage(ModDamageTypes.BLEED.location().getPath(), "%1$s lost too much blood", null, "%1$s bled out thanks to %2$s");
        addDeathMessage(ModDamageTypes.HEAVY_BLEED.location().getPath(), "%1$s was drained of blood", null, "%1$s was drained dry after meeting %2$s");
        addDeathMessage(ModDamageTypes.INTERNAL_BLEED.location().getPath(), "%1$s learned the hard way that blood isn’t meant to be everywhere inside your body.", null, "%1$s discovered misplaced blood, courtesy of %2$s");
        addDeathMessage(ModDamageTypes.OPIOIDS.location().getPath(), "%1$s took too much pain medication", null, "%1$s overdosed with a little help from %2$s");
        addDeathMessage(ModDamageTypes.OXYGEN.location().getPath(), "%1$s forgot to breathe", null, "%1$s forgot to breathe while fighting %2$s");

        addMoodle("sleep.title", "Sleeping");
        addMoodle("sleep.description", "Just taking a nap.");

        addMoodle("immunocompetent.title", "Immunocompetent");
        addMoodle("immunocompetent.description", "Your immune system is working at high capacity, likely due to antibiotics. Infections are much less dangerous, and might even regress on their own if near 200%.");

        addMoodle("immunocompromised.title", "Immunocompromised");
        addMoodle("immunocompromised.description", "Your immune system is severely weakened, be it due to dirtiness, hunger, thirst or more. Infections are much more dangerous. Take better care of yourself.");

        addMoodle("sepsis.title2", "Sepsis");
        addMoodle("sepsis.description2", "A widespread infection is causing your body to react badly. You feel confused, with lowered blood pressure and a fever. Lethal if progresses.");
        addMoodle("sepsis.title3", "Severe sepsis");
        addMoodle("sepsis.description3", "Increased heart rate, lowered blood pressure, lightheadedness and a strong fever. Signs of organ dysfunction. Likely to soon enter septic shock.");
        addMoodle("sepsis.title4", "Septic shock");
        addMoodle("sepsis.description4", "You couldn't stop for death, so death has kindly stopped by for you. Circulatory collapse imminent.");

        addMoodle("toxicosis.title1", "Mild toxicosis");
        addMoodle("toxicosis.description1", "Slight hemotoxin presence in the bloodstream.");
        addMoodle("toxicosis.title2", "Toxicosis");
        addMoodle("toxicosis.description2", "Hemotoxin present in the bloodstream, impairing blood clotting and destroying blood cells. Watch for vital signs.");
        addMoodle("toxicosis.title3", "Severe toxicosis");
        addMoodle("toxicosis.description3", "Large amount of hemotoxin present in the bloodstream, clogging your blood and destroying blood cells. Watch for vital signs.");
        addMoodle("toxicosis.title4", "Toxic crisis");
        addMoodle("toxicosis.description4", "Your blood more closely resembles marmalade than something that can upkeep life.");

        addMoodle("sickness.title1", "Queasy");
        addMoodle("sickness.description1", "Feeling discomfort. Minorly sick.");
        addMoodle("sickness.title2", "Nauseous");
        addMoodle("sickness.description2", "Confused, uncomfortable around the stomach. Prone to vomiting.");
        addMoodle("sickness.title3", "Sick");
        addMoodle("sickness.description3", "Lethargic, tired and in major discomfort. Very prone to vomiting.");
        addMoodle("sickness.title4", "Grossly sick");
        addMoodle("sickness.description4", "Dangerously sick. Something is VERY wrong on the inside. Weak, confused and in a world of pain.");

        addMoodleO("bleeding.title1", "Minor Bleeding");
        addMoodleO("bleeding.description1", "Blood is slowly escaping. No imidiate danger.");
        addMoodleO("bleeding.title2", "Bleeding");
        addMoodleO("bleeding.description2", "Blood is flowing out from a decently sized wound. Treatment recomended.");
        addMoodleO("bleeding.title3", "Heavy Bleeding");
        addMoodleO("bleeding.description3", "A large volume of blood is comming out of your body. Treatment needed.");
        addMoodleO("bleeding.title4", "Catastrophic Bleeding");
        addMoodleO("bleeding.description4", "Blood is spraying like out of a fire hose. Treatment critical!");

        addMoodleO("internal_bleeding.title3", "Internal Bleeding");
        addMoodleO("internal_bleeding.description3", "Turns out your guts and lungs are NOT where the blood is supposed to be. Treatment recommended.");

        addMoodleO("brain_health.title1", "Cognitive impairment");
        addMoodleO("brain_health.description1", "Mentally impaired from damage to the brain. You feel weirdly confused...");
        addMoodleO("brain_health.title2", "Neurological damage");
        addMoodleO("brain_health.description2", "Strong mental deficit. Ability to think intelligently and self-sustain is limited. Serious brain damage present.");
        addMoodleO("brain_health.title3", "Severe neurophysiological deterioration");
        addMoodleO("brain_health.description3", "reality  ..  stops   making   sense");
        addMoodleO("brain_health.title4", "Comatose");
        addMoodleO("brain_health.description4", ". . .");

        addMoodleO("consiousness.title1", "Confused");
        addMoodleO("consiousness.description1", "Feeling disoriented and slightly dizzy.");
        addMoodleO("consiousness.title2", "Very confused");
        addMoodleO("consiousness.description2", "Confused and dizzy, struggling to keep up with the world around you.");
        addMoodleO("consiousness.title3", "Fainting");
        addMoodleO("consiousness.description3", "Barely conscious, feeling like you could collapse at any moment.");
        addMoodleO("consiousness.title4", "Incapacitated");
        addMoodleO("consiousness.description4", "Can't stand or think. You barely feel anything.");
        addMoodleO("consiousness.title5", "Unconscious");
        addMoodleO("consiousness.description5", "Not responding to any external stimuli. Lights out.");

        addMoodleO("dirty.title1", "Dirty");
        addMoodleO("dirty.description1", "Consider taking a quick bath.");
        addMoodleO("dirty.title2", "Very dirty");
        addMoodleO("dirty.description2", "While you don't particularly mind the lack of hygiene, your immune system certainly will. Take a bath.");

        addMoodleO("dislocation.title3", "Dislocated joint");
        addMoodleO("dislocation.description3", "You dislocated something. Try not to use the affected limb, and find a way to fix it. Treatment recommended.");

        addMoodleO("fracture.title3", "Fractured bone");
        addMoodleO("fracture.description3", "You broke something. Try not to use the affected limb, and rest as much as possible. Treatment highly recommended.");

        addMoodleO("hemothorax.title3", "Hemothorax");
        addMoodleO("hemothorax.description3", "Blood is accumulating in the pleural cavity due to internal bleeding. Your chest hurts... Treatment needed.");

        addMoodleO("high_blood.title1", "Bloated");
        addMoodleO("high_blood.description1", "Feeling uncomfortable. Increased blood pressure.");
        addMoodleO("high_blood.title2", "Hypervolemic");
        addMoodleO("high_blood.description2", "Body bloated, blood pressure high, feeling dizzy with a headache.");
        addMoodleO("high_blood.title3", "Critically hypervolemic");
        addMoodleO("high_blood.description3", "Grossly bloated. Extremely high blood pressure. Can barely stand, or think.");
        addMoodleO("high_blood.title4", "Lethally hypervolemic");
        addMoodleO("high_blood.description4", "There's a terrible pain in your chest... Vital signs fading. Blood volume and pressure way too high.");

        addMoodleO("low_blood.title1", "Pale");
        addMoodleO("low_blood.description1", "Minor blood loss. You're feeling a little weak, with pale skin.");
        addMoodleO("low_blood.title2", "Hypovolemic");
        addMoodleO("low_blood.description2", "Weak and confused from blood loss. You feel really unwell. Treatment recommended.");
        addMoodleO("low_blood.title3", "Critically hypovolemic");
        addMoodleO("low_blood.description3", "Missing a major amount of blood, barely conscious. Your vision is fuzzy... Treatment needed.");
        addMoodleO("low_blood.title4", "Exsanguinated");
        addMoodleO("low_blood.description4", "Life-threatening blood loss, about to pass out. Moments away from total heart failure. Death imminent.");

        addMoodleO("infection.title1", "Infection");
        addMoodleO("infection.description1", "The infection site is swollen and warm. Treatment needed. Find something that can kill off foreign bodies.");
        addMoodleO("infection.title2", "Painful infection");
        addMoodleO("infection.description2", "The infection site is leaking dark pus, and is swelling more. Treatment needed.");
        addMoodleO("infection.title3", "Severe infection");
        addMoodleO("infection.description3", "The infection is spreading across the limb quickly and painfully. Chances of going into sepsis. Urgent treatment needed.");
        addMoodleO("infection.title4", "Life-threatening infection");
        addMoodleO("infection.description4", "The infection is spreading to other limbs, and the infected limb is starting to undergo necrosis. Sepsis is setting in. Emergency care needed.");

        addMoodleO("lung_faliure.title4", "Lung failure");
        addMoodleO("lung_faliure.description4", "Your lungs are too damaged to function, caused by critically low chest muscle health. Asphyxiation imminent.");

        addMoodleO("not_breathing.title4", "Respiratory arrest");
        addMoodleO("not_breathing.description4", "Suffocating! Loss of consciousness and death imminent if untreated.");

        addMoodleO("opiate.title1", "Opiated");
        addMoodleO("opiate.description1", "Relaxed and calm. Your body feels numb.");
        addMoodleO("opiate.title2", "Drugged");
        addMoodleO("opiate.description2", "Very relaxed and calm, but your lungs feel heavy. A little more tired than usual. This feels pretty good, for now...");
        addMoodleO("opiate.title3", "Highly drugged");
        addMoodleO("opiate.description3", "Breathing is difficult, but your mind is in euphoria. This definitely isn't healthy. If only it could last forever...");
        addMoodleO("opiate.title4", "Fatal opioid overdose");
        addMoodleO("opiate.description4", "Respiratory failure. You are leaving this world in a drug-filled euphoria. Brain damage and death imminent.");

        addMoodleO("oxygen.title1", "Hypoxemic");
        addMoodleO("oxygen.description1", "Lowered blood oxygen. Slightly confused, with rubbery skin. Something's wrong...");
        addMoodleO("oxygen.title2", "Very hypoxemic");
        addMoodleO("oxygen.description2", "Not enough oxygen is circulating around the body. Feeling light-headed and numb in the extremities. Something is very wrong.");
        addMoodleO("oxygen.title3", "Asphyxiating");
        addMoodleO("oxygen.description3", "Consciousness lost. Tissues deprived of oxygen. Brain damage imminent.");
        addMoodleO("oxygen.title4", "Cardiac arrest");
        addMoodleO("oxygen.description4", "Heart stopped. Brain dying from oxygen deprivation. Your entire body is rapidly shutting down; Death imminent.");

        addMoodleO("pain.title1", "Discomfort");
        addMoodleO("pain.description1", "Feeling mild pain.");
        addMoodleO("pain.title2", "Pain");
        addMoodleO("pain.description2", "Hurting a decent bit. Unhappiness rising slowly.");
        addMoodleO("pain.title3", "Severe pain");
        addMoodleO("pain.description3", "Half-conscious, mind fogged by intense pain. Unhappiness rising.");
        addMoodleO("pain.title4", "Agony");
        addMoodleO("pain.description4", "Mind-shattering pain. Movement compromised. Death sounds preferable right about now. Unhappiness rising quickly.");

        addMoodleO("shock.title3", "Shock");
        addMoodleO("shock.description3", "Going into shock from agony.");

        addMoodleO("low_temp.title1", "Chilly");
        addMoodleO("low_temp.description1", " A little cold for comfort.");
        addMoodleO("low_temp.title2", "Cold");
        addMoodleO("low_temp.description2", "Unpleasantly cold. Your body is slowing down.");
        addMoodleO("low_temp.title3", "Hypothermia");
        addMoodleO("low_temp.description3", "Dangerously low temperature, body and mind taken by the cold. Your entire body is slowly shutting down.");
        addMoodleO("low_temp.title4", "Freezing to death");
        addMoodleO("low_temp.description4", "You will soon fall asleep and never wake up. Goodnight.");

        addMoodleO("high_temp.title1", "Warm");
        addMoodleO("high_temp.description1", "A little warm for comfort.");
        addMoodleO("high_temp.title2", "Hot");
        addMoodleO("high_temp.description2", "Unpleasantly hot. Thirst increased.");
        addMoodleO("high_temp.title3", "Hyperthermia");
        addMoodleO("high_temp.description3", "Dangerously hot. You're struggling to go on in the heat... Thirst highly increased.");
        addMoodleO("high_temp.title4", "Heatstroke");
        addMoodleO("high_temp.description4", "Cells are starting to die from the intense heat. Irreversible organ damage imminent.");

        addMoodleO("withdrawal.title1", "Opioid craving");
        addMoodleO("withdrawal.description1", "You really want another shot.");
        addMoodleO("withdrawal.title2", "Withdrawal");
        addMoodleO("withdrawal.description2", "Your body is desperate for another hit. You want to vomit... This feels really bad. Treatment recommended.");
        addMoodleO("withdrawal.title3", "Severe withdrawal");
        addMoodleO("withdrawal.description3", "You are struggling to hold yourself together, shaking, desperate for any sort of relief. It feels horrible. Treatment needed.");
        addMoodleO("withdrawal.title4", "Dying of withdrawal");
        addMoodleO("withdrawal.description4", "Your body is failing to sustain itself without the opiates it became so reliant on. Your heart is struggling to pump oxygen around the body, resulting in hypoxic brain damage during a desperate episode.");

        addMoodleO("adrenaline.title1", "Adrenaline");
        addMoodleO("adrenaline.description1", "Pain numbed. You're on high alert.");

        addMoodleO("life_support.title1", "Life Support");
        addMoodleO("life_support.description1", "Oxygen and awareness levels kept at sustainable levels by a life support machine");

        addMoodleO("amputated.title1", "Amputated");
        addMoodleO("amputated.description1", "One of your extremities has been dismembered. Traumatizing. Obviously, any use of the lobbed off limb is permanently gone.");

        addMoodleO("fracture_chest.title3", "Fractured ribs");
        addMoodleO("fracture_chest.description3", "Your ribs are broken, making breathing painful and way harder. Hope none of them punctured your lungs.");

        addMoodleO("fracture_head.title3", "Fractured neck");
        addMoodleO("fracture_head.description3", "Your neck is broken. Your spinal cord isn't severed, but any amount of moving around will be agonizing. Physically weaker from the neck down");

        addMoodleO("dislocation_chest.title3", "Dislocated spine");
        addMoodleO("dislocation_chest.description3", "Your spine is dislocated. Your spinal cord isn't severed, but moving your body at all is really painful. Physically weaker from the waist down.");

        addMoodleO("dislocation_head.title3", "Dislocated jaw");
        addMoodleO("dislocation_head.description3", "Your jaw is dislocated, impairing your speech and making eating anything extremely painful.");

        addMoodleO("eye_gone.title1", "Half-blind");
        addMoodleO("eye_gone.description1", "One of your eyes has been ripped out, halving your field of view. You are somewhat scarred by the experience. Don't lose the remaining one.");
        addMoodleO("eye_gone.title2", "Blind");
        addMoodleO("eye_gone.description2", "Both of your eyes have been ripped out. You can only get by using feel and sound. At least you no longer need a light source...");

        addMoodleO("mouth_gone.title2", "Disfigured");
        addMoodleO("mouth_gone.description2", "Your lower jaw has been ripped off due severe trauma to the head. Eating and talking is much harder. You are horribly scarred by the experience.");

        addMoodleO("sound_loss.title1", "Impaired hearing");
        addMoodleO("sound_loss.description1", "You can't hear those high frequencies as well as before. Let your ears rest for a while.");
        addMoodleO("sound_loss.title2", "Hearing loss");
        addMoodleO("sound_loss.description2", "Everything sounds very muffled and echo-y. You feel irritated. This may take a bit.");
        addMoodleO("sound_loss.title3", "Severe hearing loss");
        addMoodleO("sound_loss.description3", "You can barely hear anything. You feel very irritated. Your ears will take a good while to improve.");

        add(modid + ".multi_tank.hint", "Press SHIFT for fluid description");

        addTooltipO("contents", "Contents:");
        addTooltipO("liquid.amount.with.capacity", "%s / %s mb");
        addTooltipO("liquid.amount", "%s mb");
        addTooltipO("percent", "%1$s%%");

        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "extra_note"), "A small line on the bottom says: \"If found return to Doctor Ry**\" the rest is not readable.");

        addCommand("heal.success", "Healed %1$s player(s).");
        addCommand("error.unknown_field", "Unknown field: %1$s");
        addCommand("setlimb.success", "Applied value %1$s to %2$s | %3$s for %4$s");
        addCommand("setbody.success", "Applied value %1$s to %2$s for %3$s");
        addCommand("amputate.success", "Amputated %1$s");
        addCommand("fillfluid.error.player_only", "Can only be run by a player");
        addCommand("fillfluid.error.invalid_fluid", "Invalid fluid");
        addCommand("fillfluid.error.no_item", "No compatible item found in main hand");
        addCommand("fillfluid.success", "Added %1$smb of %2$s to item");
    }
}
