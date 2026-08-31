package net.zaharenko424.casualties_cubed.datagen.lang;

import net.minecraft.data.PackOutput;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.ModDamageTypes;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.SleepQuality;
import net.zaharenko424.casualties_cubed.registry.ModBlocks;
import net.zaharenko424.casualties_cubed.registry.ModFluids;
import net.zaharenko424.casualties_cubed.registry.ModSounds;

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

    protected void addSleepQuality() {
        for (SleepQuality quality : SleepQuality.values()) {
            addGui("sleep_quality." + quality.name().toLowerCase(), Arrays.stream(quality.name().toLowerCase().split("_"))
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
        addItemFromIdWDesc(CANTEEN, "Simple, small bottle for holding 300ml of liquid.");
        addItemFromIdWDesc(WATER_BOTTLE, "A plastic bottle that can hold 0.5L of liquid.");
        addItemFromIdWDesc(WATER_JUG, "Big, heavy plastic bottle. Stores 3L of liquid.");
        addItemFromIdWDesc(ALCOHOL_BOTTLE, "A glass bottle that can hold 0.5L of liquid.");
        addItemFromIdWDesc(ICE_PACK, "Reduces swelling and eases sore muscles.");
        addItemWDesc(IV_BAG, "IV Bag", "A 0.75L fluid bag with a needle at the end.");
        addItemFromIdWDesc(BLOOD_BAG, "A 0.75L plastic bag with an autoinjector at the end. Injects 375mL on use. Use to draw blood from yourself.");
        addItemWDesc(OPIUM_VIAL, "Poppy Extract Vial", "A 100ml bottle. The overdose label says 200ml.");
        addItemFromIdWDesc(MORPHINE_VIAL, "A 100ml bottle. The overdose label says 90ml.");
        addItemFromIdWDesc(HEROIN_SYRINGE, "A 150ml syringe for intravenous injection. The dosage marks are scratched off.");
        addItemFromIdWDesc(FENTANYL_VIAL, "A 100ml bottle containing diluted fentanyl for pain relief. The overdose label says 20ml.");
        addItemFromIdWDesc(NALOXONE_VIAL, "A 100ml bottle. The label reads \"Nal...\" — the rest is unreadable.");
        addItemFromIdWDesc(PAINKILLERS_PILLS, "A 100ml pill bottle. The dosage is 10ml.");
        addItemFromIdWDesc(ANTIBIOTICS_PILLS, "A 100ml bottle of antibiotic pills. The dose is 10ml.");
        addItemFromIdWDesc(PROCOAGULANT_INJECTOR, "A 100ml bottle with an autoinjector attached. The dosage is 34ml.");
        addItemFromIdWDesc(WOUND_GLUE_SPRAY, "A 80ml spray bottle. Sprays 20ml of liquid at a time.");
        addItemFromIdWDesc(STREPTOKINASE_INJECTOR, "A 100ml bottle with an autoinjector attached. The dosage is 34ml.");
        addItemFromIdWDesc(SPLINT, "Stabilizes broken bones and aids healing.");
        addItemFromIdWDesc(TWEEZERS, "Removes shrapnel, glass, or debris from wounds.");
        addItemFromIdWDesc(TOURNIQUET, "Stops heavy bleeding but limits blood flow.");
        addItemWDesc(SMALL_MEDIBAG, "Small Medical Bag", "A compact pouch for four medical items.");
        addItemWDesc(MEDIUM_MEDIBAG, "Medium Medical Bag", "A balanced pack with room for eight items.");
        addItemWDesc(LARGE_MEDIBAG, "Large Medical Bag", "A spacious medical kit that can hold up to twelve items.");
        addItemFromIdWDesc(ADHESIVE_BANDAGE, "Simple adhesive bandages usually used on small cuts and bruises.");
        addItemFromIdWDesc(CEFTRIAXONE_VIAL, "A 100ml bottle. The safe dose label says \"may vary.\"");
        addItemFromIdWDesc(BLEACH_JUG, "A 1.5L jug for storing bleach. Applies 100ml of liquid at a time.");
        addItemFromIdWDesc(MEDICINE_VIAL, "A 100ml bottle meant for medicine.");
        addItemFromIdWDesc(ANTISEPTIC_SPRAY, "A 200ml spray bottle. Sprays 10ml of liquid at a time.");
        addItemFromIdWDesc(RELIEF_CREAM_BOTTLE, "A 200ml bottle. The label warns, \"Do not use more than 10 bottles in one day.\"");
        addItemFromIdWDesc(ANTISERUM_INJECTOR, "A 100ml bottle with an autoinjector attached. The dosage is 50ml.");
        addItemFromIdWDesc(COMBAT_PEN, "A 100ml injector pen which instantly injects its contents into the body. Contains a mixture intended to quickly stabilize casualties and aid in combat scenarios.");
        addItemWDesc(BRAIN_GROW_PILLS, "BrainGrow Pill Bottle", "A 50ml bottle. The label says, \"Take up to one dose per day.\"");
        addItemFromIdWDesc(SYRINGE, "A 100ml syringe for intravenous injection.");
        addItemFromIdWDesc(OLD_RAG, "A worn piece of cloth. Can be used as a makeshift dressing.");
        addItemFromIdWDesc(RIPPED_DRESSING, "A worn piece of what used to be a bandage. Can be used as a makeshift dressing.");
        addItemFromIdWDesc(BRUISE_KIT, "A combination of various medicines, made to aid sore muscles. Cannot stop the bleeding.");
        addItemFromIdWDesc(ALGINATE_DRESSING, "A specially treated piece of a dressing. Slows down bleeding and highly improves skin healing.");
        addItemFromIdWDesc(MEDICAL_GAUZE, "Woven gauze containing painkilling drugs. Decent at slowing bleeding and reducing pain from injuries.");
        addItemWDesc(BONE_WELDER, "Bone Welding Tool", "An experimental piece of technology. Welds the bone structure back together. Causes some damage on the way in.");
        addItemWDesc(LRD, "L.R.D", "A Localized Resuscitation Device, made to easily provide effective general trauma care. Many thank their lives to this piece of machinery. Holds 75ml of fluid. Needs at least 25ml of LRD serum to function");
        addItemWDesc(MAKESHIFT_LRD, "Makeshift L.R.D", "A Localized Resuscitation Device, made to easily provide effective general trauma care. This one seems to be crudely made. Holds 50ml of fluid. Needs at least 25ml of LRD serum to function");
        addItemFromIdWDesc(MEDICAL_SUTURE, "Used to stitch wounds back together. Instantly stops most of the bleeding.");
        addItemFromIdWDesc(HEAT_PACK, "A slightly larger chemical hand warmer. Aids in healing of muscles slightly.");
        addItemFromIdWDesc(AUTO_PUMP, "A very advanced Life support device with internal batteries. Keeps oxygen at stable levels,provides adrenaline and energy. Batteries last about 5min. Only usable on the chest");
        addItemFromIdWDesc(CHEST_DRAIN, "Used to drain fluid from the thorax. Reduces hemothorax, reusable after some time. Only usable on the chest.");
        addItemFromIdWDesc(THERMOMETER, "A small device that reads the temperature around the user.");
        addItemFromId(SimpleEarProtection);
        addItemFromIdWDesc(PILL_BOTTLE, "A small pill container, the dosage is 10ml.");
        addItemFromIdWDesc(AUTO_INJECTOR, "A 100ml bottle with an autoinjector attached. The dosage is 34ml.");
        addItemFromIdWDesc(GLOW_FRUIT, "A bioluminescent plant often found in caves. Counteracts infections but is highly toxic when ingested.");
        add(BROWN_CAP.getId().toLanguageKey("item", "description"), "You have no idea what is this fungus. For all you know it can cure cancer or kill you on the spot");
        addItemFromIdWDesc(BROWN_CAP_MUSH, "Probably a bad idea to eat this...");
        addItemFromId(EXPERIMENTAL_TREATMENT);
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "description1"), "The label says:");
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "description2"), "EXPERIMENTAL TREATMENT EX-8UN, For authorized Study participants only. Effects may include:");
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "description3"), "Mild dizziness ;Permanent dizziness ;Energy ;Lethargy ;Metallic taste ;Sudden bleeding ;Cardiac arrest;Minor brain damage ;Major brain damage ; Respiratory arrest; Deja vu; Paranoia; Loss of limbs; Grow of limbs; Hunger; Vomiting; Deja vu; Memory loss...");
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "description4"), "The list goes on for another 2 lines.");
        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "doom"), "You feel a sudden sense of Doom");

        addMedicalFluidFromIdWDesc(ModFluids.CLEAN_WATER, "Clean, filtered water. Safe, but tastes like nothing.");
        addMedicalFluidFromIdWDesc(ModFluids.CARBONATED_WATER, "It's spicy water. You like the fizz.");
        addMedicalFluidWDesc(ModFluids.LRD_SERUM, "LRD Serum", "Special mixture used by a Localised Resuscitation Device. Does nothing if injected via other tools.");
        addMedicalFluidFromIdWDesc(ModFluids.MORPHINE, "Strong opiate for pain relief. Lesser effect when taken orally.");
        addMedicalFluidWDesc(ModFluids.BIO_CHEM, "Bio-chem Fluid", "Advanced chemical compound that greatly promotes chemical reactions and is able to mend certain materials together, or even melt them. Very commonly used in crafting.");
        addMedicalFluidFromIdWDesc(ModFluids.OPIUM, "Mild opiate for pain relief. Lesser effect when taken orally.");
        addMedicalFluidFromIdWDesc(ModFluids.PAINKILLERS, "Opioid pills used to treat pain. Overdose at 60ml.");
        addMedicalFluidFromIdWDesc(ModFluids.HEROIN, "Very strong opiate for pain relief. You know little about it.");
        addMedicalFluidFromIdWDesc(ModFluids.NALOXONE, "Opioid antagonist, used in treatment of opiate overdose. Quick treatment might save your life during accidental overdose.");
        addMedicalFluidFromIdWDesc(ModFluids.NALTREXONE, "Pills used to treat general ailments like sickness or opiate abuse. Lowers happiness and may cause vomiting.");
        addMedicalFluidFromIdWDesc(ModFluids.CEFTRIAXONE, "Third-generation cephalosporin antibiotic used for the treatment of infections. Increases body immunity for some time, but causes pain.");
        addMedicalFluidFromIdWDesc(ModFluids.FENTANYL, "Highly potent opioid, 100 times stronger than morphine. Consider diluting it.");
        addMedicalFluidFromIdWDesc(ModFluids.KETCHUP, "Sweet and sour tomato paste. Slightly disgusting to drink by itself.");
        add("medical_fluid." + modid + ".milk", "White liquid food produced by the mammary glands of mammals. Tasty and filling!");
        addMedicalFluidFromIdWDesc(ModFluids.CHLOROFORM, "A volatile, colorless, sweet-smelling, dense liquid produced on a large scale as a precursor to refrigerants and polytetrafluoroethylene. In practical use, it's very useful at knocking people out.");
        addMedicalFluidWDesc(ModFluids.HIGH_GRADE_STIMULANT, "Medical-Grade Stimulant", "Medical stimulant drug. Gives you a very good kick for a pretty long while, and is generally safe to use. Overdose generally entails weakness, with a negligible crash. Dosage is 100ml.");
        addMedicalFluidWDesc(ModFluids.MID_GRADE_STIMULANT, "Hard Stimulant", "Decently refined stimulating hard drug. Only works intravenously. Will give you a pretty good kick for a short while, but has some side effects. Overdose entails weakness and internal bleeding. Dosage is 50ml.");
        addMedicalFluidWDesc(ModFluids.LOW_GRADE_STIMULANT, "Off-Brand Stimulant", "Unrefined stimulating hard drug. Dodgy. Gives you a pretty nice kick, but overdose and the subsequent crash is extremely debilitating. Dosage is unknown. Use with caution.");
        addMedicalFluidFromIdWDesc(ModFluids.APPLE_JUICE, "Fruit juice made by the maceration and pressing of an apple. Tasty!");
        addMedicalFluidFromIdWDesc(ModFluids.ORANGE_JUICE, "Liquid extract of the orange tree fruit. While it is healthy, you're really not a fan of the taste.");
        addMedicalFluidFromIdWDesc(ModFluids.LEMONADE, "Sweetened lemon-flavored drink. Tasty!");
        addMedicalFluidFromIdWDesc(ModFluids.ICE_TEA, "Cold, sweetened decaffeinated tea. Tasty!");
        addMedicalFluidFromIdWDesc(ModFluids.SOUP, "Unidentifiable soup, though still tasty and filling.");
        addMedicalFluidFromIdWDesc(ModFluids.CHOCOLATE_MILK, "A type of flavoured milk made by mixing it with cocoa solids. Very sweet and tasty, but toxic for certain species. Increases happiness, consume with caution.");
        addMedicalFluidFromIdWDesc(ModFluids.CEREAL, "The world's greatest invention - milk and cereal. Very tasty and filling.");
        addMedicalFluidFromIdWDesc(ModFluids.COFFEE, "Beverage brewed from roasted coffee beans. Tasty and stimulating, but can be rather toxic to some due to the caffeine content.");
        addMedicalFluidFromIdWDesc(ModFluids.ENERGY_DRINK, "Tasty, caffeinated drink. Tasty and stimulating, but can be slightly toxic for certain species.");
        addMedicalFluidFromIdWDesc(ModFluids.SPORTS_DRINK, "Non-caffeinated electrolyte drink. Quenches thirst efficiently, gives you a little boost and tastes nice.");
        addMedicalFluidFromIdWDesc(ModFluids.OLIVE_OIL, "A vegetable oil obtained by pressing whole olives. Very fatty and somewhat unpleasant to drink.");
        addMedicalFluidFromIdWDesc(ModFluids.HOT_SAUCE, "Condiment made from particularly spicy peppers. Burns your mouth, but warms you up... Somehow.");
        addMedicalFluidFromIdWDesc(ModFluids.ICE_CREAM, "A frozen dessert made from milk and cream that has been flavoured with a sweetener. Somehow still cold. Delicious!");
        addMedicalFluidFromIdWDesc(ModFluids.YOGURT, "Bacterially fermented milk. Tastes okay. Decently filling.");
        addMedicalFluidFromIdWDesc(ModFluids.MOLD, "Any indication of what this once was is long gone. Smells putrid...");
        addMedicalFluidFromIdWDesc(ModFluids.POWDERED_MILK, "Dehydrated and compressed efficient milk powder. While not edible on its own, it'll turn into milk when mixed with hot water.");
        addMedicalFluidFromIdWDesc(ModFluids.RAD_WATER, "Clean, filtered water. Safe, but tastes like nothing... On further inspection, it seems mildly radioactive.");
        addMedicalFluidFromIdWDesc(ModFluids.MERCURY, "Liquid metal. Great for skin care, not so great for your bodily functions.");
        addMedicalFluidFromIdWDesc(ModFluids.SODA, "Tasty, sugary drink. Good in moderation.");
        addMedicalFluidFromIdWDesc(ModFluids.ALCOHOL, "Unlabeled alcohol. Useful for disinfecting wounds.");
        addMedicalFluidFromIdWDesc(ModFluids.BLEACH, "Chemical product used to remove color from fiber, or to disinfect. Can be used to disinfect wounds. Drinking is lethal.");
        addMedicalFluidFromIdWDesc(ModFluids.RELIEF_CREAM, "Mix of chemicals and drugs that instantly relieves pain and disinfects wounds.");
        addMedicalFluidFromIdWDesc(ModFluids.WOUND_GLUE, "A hemostatic, antibiotic glue-like substance. Fights infection, promotes healing and is very effective at sealing bleeding wounds. Might cause circulation problems - use with care.");
        addMedicalFluidWDesc(ModFluids.BRAINGROW, "BrainGrow", "Special orally-taken medicine that stimulates brain regrowth. Increases brain integrity, but often has serious side effects.");
        addMedicalFluidFromIdWDesc(ModFluids.ANTIBIOTICS, "Pills which increase your immunity for some time, counteracting all infections. Upsetting taste.");
        addMedicalFluidFromIdWDesc(ModFluids.ANTIVENOM, "A treatment for envenomation, it's composed of antibodies that disable hemotoxins in the bloodstream. Works intravenously.");
        addMedicalFluidFromIdWDesc(ModFluids.ANTISERUM, "Blood serum containing antibodies to fight off most common infections. Increases immunity and helps reduce sepsis. Slightly increases blood volume.");
        addMedicalFluidFromIdWDesc(ModFluids.PROCOAGULANT, "Chemical which promotes blood clotting. Globally slows down blood loss, including internal bleeding. Will cause complications if overused.");
        addMedicalFluidFromIdWDesc(ModFluids.EPINEPHRINE, "Pure adrenaline. Reduces pain for a while and has a low chance to restart the heart if it stops. Going over 40ml will cause fibrillation.");
        addMedicalFluidFromIdWDesc(ModFluids.OXYLINE, "Synthesized reagent that quickly reoxygenates the bloodstream when injected, also stabilizing fibrillation and promoting hemoglobin production. Has explosive tendencies when mixed with stomach acid.");
        addMedicalFluidFromIdWDesc(ModFluids.SODIUM_NITROPRUSSIDE, "A medication used to lower blood pressure, useful for avoiding hypertensive crisis. Only works intravenously. 10ml per minute.");
        addMedicalFluidFromIdWDesc(ModFluids.VASOPRESSIN, "A hormone used to increase blood pressure, useful for avoiding lethal hypotension. Only works intravenously. 10ml per minute.");
        addMedicalFluidFromIdWDesc(ModFluids.AMIODARONE, "An antiarrhythmic medication used to treat and prevent fibrillation from occurring. Will cause most arrhythmias to slowly regress, but causes mild internal damage. Only works intravenously. 20ml per minute.");
        addMedicalFluidFromIdWDesc(ModFluids.STREPTOKINASE, "Blood thinner. Strongly reduces blood viscosity and breaks down clots.");
        addMedicalFluidFromIdWDesc(ModFluids.SALINE, "Mixture of salt and water, often used in treating hypovolemia and thirst.");
        addMedicalFluidFromIdWDesc(ModFluids.BLOOD, "Used in treating hypovolemia.");
        addMedicalFluidFromIdWDesc(ModFluids.ANTISEPTIC, "Basic antiseptic fluid. Counteracts infections and stings a little.");
        addMedicalFluidFromIdWDesc(ModFluids.GROUNDWATER, "Slightly dirty groundwater. Mostly safe to drink, though slightly sickening.");
        addMedicalFluidFromIdWDesc(ModFluids.LUMALGAE, "Water with dense, bioluminescent algae growing in it. Very buoyant. Inedible.");
        addMedicalFluidFromIdWDesc(ModFluids.OIL, "A thick, black tar. Difficult to move through. Inedible.");
        addMedicalFluidFromIdWDesc(ModFluids.SAP, "A thick, tasty sap, produced by jungle trees. Very sugary.");
        addMedicalFluidFromIdWDesc(ModFluids.DIRTY_WATER, "At least it's not on your body anymore. Not safe to drink.");
        addMedicalFluidFromIdWDesc(ModFluids.FAT, "Liquid animal fat, gotten from flesh. Can be eaten, though not particularly pleasant. Can be processed into soap.");
        addMedicalFluidFromIdWDesc(ModFluids.SOAP, "Soap made from animal fat. Can be used in the health panel to clean dirt off of you, and slightly disinfect wounds.");
        addMedicalFluidFromIdWDesc(ModFluids.PRODUCE_JUICE, "A bunch of fruit, vegetables and whatever else you could find, mashed up and mixed with water. It tastes... interesting.");
        addMedicalFluidFromIdWDesc(ModFluids.REFINED_JUICE, "Cooked through, better mixed and mashed produce juice. Very tasty and nutritious!");

        add("key.casualties_cubed.open_pain_gui", "Open Health Screen");
        add("key.casualties_cubed.give_up", "Give Up");
        add("key.casualties_cubed.ragdoll", "Ragdoll");
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
        addGuiO("fluid_amount", "%1$sml");
        addGuiO("hud.oxygen", "O₂ %1$s%%");
        addGuiO("hud.pain", "%1$s%%");
        addGuiO("health.conscious", "CONSCIOUS");
        addGuiO("health.pain_percent", "%1$s%% PAIN");
        addGuiO("health.skin", "SKIN");
        addGuiO("health.muscle", "MUSCLE");
        addGuiO("health.fracture_short", "FRACT");
        addGuiO("health.dislocation_short", "DISL");
        addGuiO("health.blood_volume", "%1$sL");
        addGuiO("health.bleed_rate", "%1$sL/m");
        addGuiO("temperature_celsius", "%1$sC");

        addContainer("looting", "Looting %1$s");

        addDeathMessage(ModDamageTypes.GIVE_UP.location().getPath(), "%1$s gave up", null, "%1$s gave up, with some encouragement from %2$s");
        addDeathMessage(ModDamageTypes.BLEED.location().getPath(), "%1$s lost too much blood", null, "%1$s bled out thanks to %2$s");
        addDeathMessage(ModDamageTypes.HEAVY_BLEED.location().getPath(), "%1$s was drained of blood", null, "%1$s was drained dry after meeting %2$s");
        addDeathMessage(ModDamageTypes.INTERNAL_BLEED.location().getPath(), "%1$s learned the hard way that blood isn’t meant to be everywhere inside your body.", null, "%1$s discovered misplaced blood, courtesy of %2$s");
        addDeathMessage(ModDamageTypes.OPIOIDS.location().getPath(), "%1$s took too much pain medication", null, "%1$s overdosed with a little help from %2$s");
        addDeathMessage(ModDamageTypes.OXYGEN.location().getPath(), "%1$s forgot to breathe", null, "%1$s forgot to breathe while fighting %2$s");

        addMoodle("last_stand.title", "Last stand");
        addMoodle("last_stand.description", "You're not going down that easily. Something deep inside you compels you to push through.");

        addMoodle("stimulated.title", "Stimulated");
        addMoodle("stimulated.description", "Hard stimulant drugs in the bloodstream. You feel ready to tackle anything thrown at you...For now, at least.");

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

        addMoodle("energy.title1", "Drowsy");
        addMoodle("energy.description1", "Getting sleepy.");
        addMoodle("energy.title2", "Tired");
        addMoodle("energy.description2", "It's nap time.");
        addMoodle("energy.title3", "Very tired");
        addMoodle("energy.description3", "It's REALLY nap time.");
        addMoodle("energy.title4", "Half-asleep");
        addMoodle("energy.description4", "Barely awake, about to collapse from exhaustion.");

        addMoodle("hunger.title1", "Peckish");
        addMoodle("hunger.description1", "Could do with a bite to eat.");
        addMoodle("hunger.title2", "Hungry");
        addMoodle("hunger.description2", "Uncomfortably hungry. Slightly weaker than usual.");
        addMoodle("hunger.title3", "Very hungry");
        addMoodle("hunger.description3", "Extremely hungry, desperate for satiation. Weaker than usual.");
        addMoodle("hunger.title4", "Starving");
        addMoodle("hunger.description4", "Your entire body, just wasting away... Total organ failure imminent.");
        addMoodle("hunger.title5", "Satiated");
        addMoodle("hunger.description5", "Eating good today! Lowered weight loss rate and mobility. Slight happiness increase.");
        addMoodle("hunger.title6", "Full");
        addMoodle("hunger.description6", "Stomach totally stuffed with food! Any more and you'll be sick. Greatly lowered weight loss rate and mobility. Happiness increase.");

        addMoodle("thirst_low.title1", "Thirsty");
        addMoodle("thirst_low.description1", "Feeling a little thirsty.");
        addMoodle("thirst_low.title2", "Dehydrated");
        addMoodle("thirst_low.description2", "Low body water volume. Very thirsty.");
        addMoodle("thirst_low.title3", "Parched");
        addMoodle("thirst_low.description3", "Body drying out, in desperate need of water. Mind consumed by thirst.");
        addMoodle("thirst_low.title4", "Desiccated");
        addMoodle("thirst_low.description4", "Blood volume dangerously low and thick due to lack of fluids. §oYour dust is becoming one with the earth...");

        addMoodle("thirst_high.title1", "Slaked");
        addMoodle("thirst_high.description1", "Your thirst is fully satiated - you won't need water for a while. Thirst rate doubled. Blood pressure increased.");
        addMoodle("thirst_high.title2", "Overhydrated");
        addMoodle("thirst_high.description2", "You've drank considerably more than needed. Movement slightly impaired. Thirst rate doubled. Blood pressure increased.");
        addMoodle("thirst_high.title3", "Water-intoxicated");
        addMoodle("thirst_high.description3", "Excessive water intake. Your cells are swelling, causing high intracranial pressure, interrupting blood flow to the brain. Your head hurts... Thirst rate doubled. Blood pressure highly increased.");

        addMoodle("sickness.title1", "Queasy");
        addMoodle("sickness.description1", "Feeling discomfort. Minorly sick.");
        addMoodle("sickness.title2", "Nauseous");
        addMoodle("sickness.description2", "Confused, uncomfortable around the stomach. Prone to vomiting.");
        addMoodle("sickness.title3", "Sick");
        addMoodle("sickness.description3", "Lethargic, tired and in major discomfort. Very prone to vomiting.");
        addMoodle("sickness.title4", "Grossly sick");
        addMoodle("sickness.description4", "Dangerously sick. Something is VERY wrong on the inside. Weak, confused and in a world of pain.");

        addMoodle("wetness.title1", "Damp");
        addMoodle("wetness.description1", "Your fur feels slightly wet.");
        addMoodle("wetness.title2", "Wet");
        addMoodle("wetness.description2", "Considerably wet. Temperature and comfort decreased.");
        addMoodle("wetness.title3", "Soaked");
        addMoodle("wetness.description3", "Dripping wet. Temperature and comfort considerably decreased.");
        addMoodle("wetness.title4", "Water-logged");
        addMoodle("wetness.description4", "Completely drenched! The water is weighing you down. Temperature and comfort highly decreased.");


        addMoodleO("internal_bleeding.title3", "Internal Bleeding");
        addMoodleO("internal_bleeding.description3", "Turns out your guts and lungs are NOT where the blood is supposed to be. Treatment recommended.");

        addMoodleO("bleeding.title1", "Minor Bleeding");
        addMoodleO("bleeding.description1", "Blood is oozing out of a relatively small wound. There is no immediate danger.");
        addMoodleO("bleeding.title2", "Bleeding");
        addMoodleO("bleeding.description2", "Blood is flowing out of a decently sized wound. Unlikely to be fatal if you're healthy. Treatment recommended.");
        addMoodleO("bleeding.title3", "Heavy bleeding");
        addMoodleO("bleeding.description3", "A large volume of blood is hemorrhaging out of your body. Likely lethal if untreated. Treatment needed.");
        addMoodleO("bleeding.title4", "Catastrophic bleeding");
        addMoodleO("bleeding.description4", "§oAs your life gushes out behind you, you remember that you are mortal.");

        addMoodle("stamina.title1", "Slightly exerted");
        addMoodle("stamina.description1", "Mildly physically strained. (%1$s%)");
        addMoodle("stamina.title2", "Exerted");
        addMoodle("stamina.description2", "Uncomfortably exerted, struggling to move and work. (%1$s%)");
        addMoodle("stamina.title3", "Highly exerted");
        addMoodle("stamina.description3", "Barely able to move, highly physically exerted. (%1$s%)");
        addMoodle("stamina.title4", "Totally exhausted");
        addMoodle("stamina.description4", "Barely able to breathe. (%1$s%)");

        addMoodleO("brain_health.title1", "Cognitive impairment");
        addMoodleO("brain_health.description1", "Mentally impaired from damage to the brain. You feel weirdly confused...");
        addMoodleO("brain_health.title2", "Neurological damage");
        addMoodleO("brain_health.description2", "Strong mental deficit. Ability to think intelligently and self-sustain is limited. Serious brain damage present.");
        addMoodleO("brain_health.title3", "Severe neurophysiological deterioration");
        addMoodleO("brain_health.description3", "reality  ..  stops   making   sense");
        addMoodleO("brain_health.title4", "Comatose");
        addMoodleO("brain_health.description4", ". . .");

        addMoodle("stroke.title", "Stroke");
        addMoodle("stroke.description", "Bleeding in the brain, likely caused by severe hypertension. Already in its advanced stages, chances of survival are null unless you have Braingrow or a procoagulant.");

        addMoodle("cardiac_arrest.title", "Cardiac arrest");
        addMoodle("cardiac_arrest.description", "Asystole. If you're somehow conscious, this has a low chance of being treated via defibrillation. Otherwise, cerebral hypoxia and death is soon to follow.");

        addMoodle("arrhythmia.title1", "Arrhythmia");
        addMoodle("arrhythmia.description1", "Abnormal heart rhythm, likely caused by severe strain to the body. May progress further if the cause isn't addressed. Can be defibrillated.");
        addMoodle("arrhythmia.title2", "Ventricular tachycardia");
        addMoodle("arrhythmia.description2", "Severe irregular heartbeat, preventing effective blood pumping. Will progress into lethal V-fib if not treated. Can be defibrillated.");
        addMoodle("arrhythmia.title3", "Ventricular fibrillation");
        addMoodle("arrhythmia.description3", "Life threatening, chaotic heart rhythm. Asystole is soon to follow. Things have gone horribly wrong.");

        addMoodle("hypotension.title1", "Mild hypotension");
        addMoodle("hypotension.description1", "Slightly decreased blood pressure. You feel slightly dizzy.");
        addMoodle("hypotension.title2", "Hypotension");
        addMoodle("hypotension.description2", "Moderately decreased blood pressure. You look pale and have clammy skin. Something is wrong - check your vitals.");
        addMoodle("hypotension.title3", "Severe hypotension");
        addMoodle("hypotension.description3", "Severely decreased blood pressure, you're on the edge of fainting. Treatment needed. This might start fibrillation, check your vitals.");
        addMoodle("hypotension.title4", "Circulatory collapse");
        addMoodle("hypotension.description4", "Lethally low blood pressure. Blood is not getting to your organs. Cerebral hypoxia and death is imminent.");

        addMoodle("hypertension.title1", "Mild hypertension");
        addMoodle("hypertension.description1", "Slightly elevated blood pressure.");
        addMoodle("hypertension.title2", "Hypertension");
        addMoodle("hypertension.description2", "Moderately elevated blood pressure. You have a headache, you're dizzy and your nose is bleeding. Check your vitals.");
        addMoodle("hypertension.title3", "Severe hypertension");
        addMoodle("hypertension.description3", "Severely elevated blood pressure, dangerously close to having severe health complications. You feel terrible. Check your vitals.");
        addMoodle("hypertension.title4", "Hypertensive crisis");
        addMoodle("hypertension.description4", "Life-threatening blood pressure. Risk of hemorrhagic stroke. Check your vitals urgently, and watch for stroke symptoms.");


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

        addMoodle("drug_overdose.title", "Drug overdose");
        addMoodle("drug_overdose.description", "You have overdosed on a non-opioid drug. Health complications likely.");

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

        addMoodleO("infection.title1", "Infection");
        addMoodleO("infection.description1", "The infection site is swollen and warm. Treatment needed. Find something that can kill off foreign bodies.");
        addMoodleO("infection.title2", "Painful infection");
        addMoodleO("infection.description2", "The infection site is leaking dark pus, and is swelling more. Treatment needed.");
        addMoodleO("infection.title3", "Severe infection");
        addMoodleO("infection.description3", "The infection is spreading across the limb quickly and painfully. Chances of going into sepsis. Urgent treatment needed.");
        addMoodleO("infection.title4", "Life-threatening infection");
        addMoodleO("infection.description4", "The infection is spreading to other limbs, and the infected limb is starting to undergo necrosis. Sepsis is setting in. Emergency care needed.");

        addMoodle("hypoventilation.title1", "Mild hypoventilation");
        addMoodle("hypoventilation.description1", "Lowered respiratory rate.");
        addMoodle("hypoventilation.title2", "Hypoventilation");
        addMoodle("hypoventilation.description2", "Highly lowered respiratory rate, enough to slowly lower blood oxygen concentration.");
        addMoodle("hypoventilation.title3", "Respiratory arrest");
        addMoodle("hypoventilation.description3", "Suffocating! Loss of consciousness and death imminent if untreated.");

        addMoodleO("lung_faliure.title4", "Lung failure");
        addMoodleO("lung_faliure.description4", "Your lungs are too damaged to function, caused by critically low chest muscle health. Asphyxiation imminent.");

        addMoodleO("opiate.title1", "Opiated");
        addMoodleO("opiate.description1", "Relaxed and calm. Your body feels numb.");
        addMoodleO("opiate.title2", "Drugged");
        addMoodleO("opiate.description2", "Very relaxed and calm, but your lungs feel heavy. A little more tired than usual. This feels pretty good, for now...");
        addMoodleO("opiate.title3", "Highly drugged");
        addMoodleO("opiate.description3", "Breathing is difficult, but your mind is in euphoria. This definitely isn't healthy. If only it could last forever...");
        addMoodleO("opiate.title4", "Fatal opioid overdose");
        addMoodleO("opiate.description4", "Respiratory failure. You are leaving this world in a drug-filled euphoria. Brain damage and death imminent.");

        addMoodleO("oxygen.title1", "Mild hypoxemia");
        addMoodleO("oxygen.description1", "SpO2 below 90%. While not dangerous, it could be a sign of an underlying condition.");
        addMoodleO("oxygen.title2", "Hypoxemia");
        addMoodleO("oxygen.description2", "SpO2 below 75%, causing tachycardia and slowly depriving the brain of oxygen. While not lethal, it does point at an underlying condition.");
        addMoodleO("oxygen.title3", "Severe hypoxemia");
        addMoodleO("oxygen.description3", "SpO2 below 60%, depriving tissues of oxygen and likely causing heart arrhythmia. Lethal if left to progress.");
        addMoodleO("oxygen.title4", "Critical hypoxemia");
        addMoodleO("oxygen.description4", "SpO2 below 45%. Something in your body has gone horribly, horribly wrong. Lethal if left to progress.");

        addMoodle("irradiated.title1", "Uncomfortable");
        addMoodle("irradiated.description1", "Feeling weirdly irritated and uncomfortable.");
        addMoodle("irradiated.title2", "Radiation sickness");
        addMoodle("irradiated.description2", "Feverish and nauseous. Your skin feels hot and painful. Something is very wrong. It's terrifying.");
        addMoodle("irradiated.title3", "Severe radiation sickness");
        addMoodle("irradiated.description3", "Horribly sick and aching all over. Breathing and thinking is extremely difficult. You feel abhorrent.");
        addMoodle("irradiated.title4", "Chernobyl wannabe");
        addMoodle("irradiated.description4", "§oNot going gently into that good night...");

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

        addMoodle("happiness_low.title1", "Feeling down");
        addMoodle("happiness_low.description1", "Starting to realize the gravity of your situation. Try to distract yourself.");
        addMoodle("happiness_low.title2", "Gloomy");
        addMoodle("happiness_low.description2", "Lacking the motivation to go on, struggling with emotions. Things really aren't looking up. Find a way to distract yourself.");
        addMoodle("happiness_low.title3", "Depressed");
        addMoodle("happiness_low.description3", "Barely willing to do anything. Your mind, ravaged by desperation. Maybe it will all be over soon?");
        addMoodle("happiness_low.title4", "Miserable");
        addMoodle("happiness_low.description4", "§oHow does it feel, knowing you're not coming back up..?");

        addMoodle("happiness_high.title1", "Satisfied");
        addMoodle("happiness_high.description1", "Content with your current predicament.");
        addMoodle("happiness_high.title2", "Excited");
        addMoodle("happiness_high.description2", "Looking forward to what's around the corner.");
        addMoodle("happiness_high.title3", "Happy");
        addMoodle("happiness_high.description3", "You've found peace in this strange land.");
        addMoodle("happiness_high.title4", "Gleeful");
        addMoodle("happiness_high.description4", "You're at the top of the world, shaping the way forward at your will. Nothing can stop you!");

        addMoodleO("withdrawal.title1", "Opioid craving");
        addMoodleO("withdrawal.description1", "You really want another shot.");
        addMoodleO("withdrawal.title2", "Withdrawal");
        addMoodleO("withdrawal.description2", "Your body is desperate for another hit. You want to vomit... This feels really bad. Treatment recommended.");
        addMoodleO("withdrawal.title3", "Severe withdrawal");
        addMoodleO("withdrawal.description3", "You are struggling to hold yourself together, shaking, desperate for any sort of relief. It feels horrible. Treatment needed.");
        addMoodleO("withdrawal.title4", "Dying of withdrawal");
        addMoodleO("withdrawal.description4", "Your body is failing to sustain itself without the opiates it became so reliant on. Your heart is struggling to pump oxygen around the body, resulting in hypoxic brain damage during a desperate episode.");

        addMoodle("concussion.title", "Concussed");
        addMoodle("concussion.description", "Rendered unconscious by low head vitality - you will wake up when your head heals.");

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

        addMoodle("braingrow_sickness.title", "Rapid neuron regeneration sickness");
        addMoodle("braingrow_sickness.description", "Neural shock caused by a sudden and rapid regeneration of brain tissue. While you'll adjust after some time, you might not want to take another dose for now. (%1$ss)");

        addMoodle("weight_low.title1", "Underweight");
        addMoodle("weight_low.description1", "Try to pack on a few kilos.");
        addMoodle("weight_low.title2", "Skinny");
        addMoodle("weight_low.description2", "Unhealthily underweight considering your species. Weaker than normal. Lowered carrying capacity.");
        addMoodle("weight_low.title3", "Malnourished");
        addMoodle("weight_low.description3", "You're dangerously thin and frail. Way weaker than normal, prone to being tired and lethargic. Lowered carrying capacity.");
        addMoodle("weight_low.title4", "Emaciated");
        addMoodle("weight_low.description4", "You're excessively skinny. You can easily see and feel your bones. At high risk of heart failure.");

        addMoodle("weight_high.title1", "Chubby");
        addMoodle("weight_high.description1", "Try to lose a few kilos.");
        addMoodle("weight_high.title2", "Overweight");
        addMoodle("weight_high.description2", "Unhealthily overweight, though you look about average compared to other species. Clumsier, you get tired quicker.");
        addMoodle("weight_high.title3", "Fat");
        addMoodle("weight_high.description3", "Dangerously wide and soft. Way clumsier and more prone to exhaustion.");
        addMoodle("weight_high.title4", "Obese");
        addMoodle("weight_high.description4", "Life-threateningly obese. It's like you stuffed a bag of fur with pure fat and hunger. At high risk of heart failure.");

        addMoodle("energized.title", "Energized");
        addMoodle("energized.description", "Stamina loss and sleepiness decreased. (%1$ss)");

        addMoodle("bad_sleep.title", "Bad sleep");
        addMoodle("bad_sleep.description", "Your back really isn't appreciating your choice of where you slept. Try picking a less harsh surface next time. Consciousness decreased.");

        addMoodle("impaired_speech.title", "Impaired speech");
        addMoodle("impaired_speech.description", "Something is making speaking difficult... You might have trouble communicating with others.");

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
        addTooltipO("liquid.amount.with.capacity", "%s / %s ml");
        addTooltipO("liquid.amount", "%s ml");
        addTooltipO("percent", "%1$s%%");
        addTooltip("health_panel_button", "Health panel (%s)");
        addTooltip("switch_main_hand_button.title", "Swap your main hand ");
        addTooltip("switch_main_hand_button.description", "Switch main hand between the Right/Left\nUse this to dig with your second arm if the main one is broken\nYour left arm is slightly weaker than the right");
        addTooltip("workout_button.title", "Do some working out");
        addTooltip("workout_button.description", "Gives experience in STR/RES at the cost of some time and energy. Good to pass the time, though it will hurt if you're seriously injured.");
        addTooltip("sleep_button.title", "Sleep (%s)");
        addTooltip("sleep_button.description", "Sleep quality is determined by the ground you sleep on\nCan't sleep if in pain/sick/not tired");
        addSleepQuality();

        add(EXPERIMENTAL_TREATMENT.getId().toLanguageKey("item", "extra_note"), "A small line on the bottom says: \"If found return to Doctor Ry**\" the rest is not readable.");

        addCommand("add_exp.success", "Added %1$s exp in %2$s to %3$s player(s).");
        addCommand("coagulate.success", "Coagulated bleeding of %1$s player(s).");
        addCommand("heal.success", "Healed %1$s player(s).");
        addCommand("error.unknown_field", "Unknown field: %1$s");
        addCommand("setlimb.success", "Applied value %1$s to %2$s | %3$s for %4$s");
        addCommand("setbody.success", "Applied value %1$s to %2$s for %3$s");
        addCommand("amputate.success", "Amputated %1$s");
        addCommand("fillfluid.error.player_only", "Can only be run by a player");
        addCommand("fillfluid.error.invalid_fluid", "Invalid fluid");
        addCommand("fillfluid.error.no_item", "No compatible item found in main hand");
        addCommand("fillfluid.success", "Added %1$sml of %2$s to item");

        addSound(ModSounds.HEALTH_SCREEN_OPEN, "Health screen opened");
        addSound(ModSounds.HEALTH_SCREEN_CLOSE, "Health screen closed");
        addSound(ModSounds.HEART_THUMP, "Heart thump");
        addSound(ModSounds.HEART_THUMP_HEAVY, "Heavy heart thump");
        addSound(ModSounds.HEART_THUMP_HEAVY_MONITOR, "Heavy heart thump");
        addSound(ModSounds.SPRAY, "Spray applied");
        addSound(ModSounds.SPLINT, "Splint applied");
        addSound(ModSounds.AUTO_PUMP, "Auto pump used");
        addSound(ModSounds.BONE_WELD, "Bone welder used");
        addSound(ModSounds.DRAIN_USE, "Chest drain used");
        addSound(ModSounds.LEVEL_UP, "Skill leveled up");
        addSound(ModSounds.CLICK, "Mouse clicked");
        addSound(ModSounds.SMALL_CLICK, "Mouse clicked");
        addSound(ModSounds.VOMIT_WARNING, "Vomit incoming");
        addSound(ModSounds.BLOOD_VOMIT_WARNING, "Blood vomit incoming");
        addSound(ModSounds.VOMIT, "Vomiting");
    }
}
