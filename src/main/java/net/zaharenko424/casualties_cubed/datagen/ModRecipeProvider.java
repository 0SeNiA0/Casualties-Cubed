package net.zaharenko424.casualties_cubed.datagen;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.datagen.recipes.crop.BasicCropRecipeProvider;
import net.zaharenko424.casualties_cubed.recipe.MedicalMixerRecipeBuilder;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;
import net.zaharenko424.casualties_cubed.tags.ModItemTags;
import net.zaharenko424.casualties_cubed.tags.ModMedicalFluidTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.EXPIE_PLUSHY.get())
                .pattern("OYO")
                .pattern("WWW")
                .pattern("OWO")
                .define('O', Ingredient.of(Tags.Items.DYES_ORANGE))
                .define('Y', Ingredient.of(Tags.Items.DYES_YELLOW))
                .define('W', Ingredient.of(Items.BLACK_WOOL))
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.RIPPED_DRESSING.get())
                .requires(Items.STRING)
                .requires(Items.STRING)
                .requires(Items.STRING)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.ADHESIVE_BANDAGE.get())
                .pattern("PHP")
                .define('P',Items.PAPER.asItem())
                .define('H',Items.HONEY_BOTTLE)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.DRESSING.get())
                .pattern(" S ")
                .pattern("RWR")
                .pattern(" S ")
                .define('S', Items.STRING)
                .define('R', ModItems.RIPPED_DRESSING.get())
                .define('W', ItemTags.WOOL)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.PLASTIC_DRESSING.get())
                .requires(ModItemTags.DRESSINGS)
                .requires(Items.SLIME_BALL)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.BONE_WELDER.get())
                .pattern("II")
                .pattern("RI")
                .pattern("RG")
                .define('I',Items.IRON_INGOT)
                .define('R',Items.REDSTONE)
                .define('G',Items.GUNPOWDER)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.THERMOMETER.get())
                .pattern(" C ")
                .pattern("CRC")
                .pattern("CGC")
                .define('C',Items.COPPER_INGOT)
                .define('R',Items.REDSTONE)
                .define('G',Items.GOLD_INGOT)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.MEDICAL_SUTURE.get())
                .pattern("GSS")
                .pattern("G S")
                .pattern("N S")
                .define('N',Items.IRON_NUGGET)
                .define('G',Items.GOLD_INGOT)
                .define('S',Items.STRING)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.ICE_PACK.get())
                .requires(Items.ICE)
                .requires(Items.SLIME_BALL)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.SMALL_MEDIBAG.get())
                .pattern("NNN")
                .pattern("WLW")
                .define('N',Items.IRON_INGOT)
                .define('W',ItemTags.WOOL)
                .define('L',Items.LEATHER)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.MEDIUM_MEDIBAG.get())
                .pattern("GWG")
                .pattern("SLS")
                .define('W',ItemTags.WOOL)
                .define('L',Items.LEATHER)
                .define('G',Items.GOLD_INGOT)
                .define('S',ModItems.SMALL_MEDIBAG.get())
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.LARGE_MEDIBAG.get())
                .pattern("DWD")
                .pattern("MNM")
                .define('N',Items.IRON_NUGGET)
                .define('W',ItemTags.WOOL)
                .define('D',Items.DIAMOND)
                .define('M',ModItems.MEDIUM_MEDIBAG.get())
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.SPLINT.get())
                .requires(Items.STICK)
                .requires(Items.STICK)
                .requires(Items.STRING)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.TWEEZERS.get())
                .pattern("IN")
                .pattern(" I")
                .define('N',Items.IRON_NUGGET)
                .define('I',Items.IRON_INGOT)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.TOURNIQUET.get())
                .pattern(" I ")
                .pattern("WLW")
                .define('W',ItemTags.WOOL)
                .define('L',Items.LEATHER)
                .define('I',Items.IRON_INGOT)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.HEAT_PACK.get())
                .requires(Items.GUNPOWDER)
                .requires(ItemTags.WOOL)
                .requires(Items.GUNPOWDER)
                .requires(Items.IRON_NUGGET)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.BROWN_CAP_MUSH.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A',ModItems.BROWN_CAP.get())
                .define('B',Items.BOWL)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.EXPERIMENTAL_TREATMENT.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern("GDG")
                .define('M',ModItems.BROWN_CAP_MUSH.get())
                .define('G', Items.GLISTERING_MELON_SLICE)
                .define('D',Items.DIAMOND)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer);

        MedicalMixerRecipeBuilder.mixer()
                .input(Fluids.WATER,10)
                .input(new ItemStack(ModItems.GLOW_FRUIT.get(),1))
                .output(ModMedicalFluids.REACTION_LIQUID.get(),10)
                .save(consumer, CasualtiesCubed.resourceLoc("reaction_liquid"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Fluids.WATER,25)
                .input(new ItemStack(Items.POPPY,1))
                .input(new ItemStack(Items.SUGAR,2))
                .output(ModMedicalFluids.OPIUM.get(),25)
                .save(consumer, CasualtiesCubed.resourceLoc("opium"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.MILK_BUCKET,1)
                .input(Items.COCOA_BEANS,8)
                .output(ModMedicalFluids.CHOCO_MILK.get(),1000)
                .output(Items.BUCKET,1)
                .save(consumer, CasualtiesCubed.resourceLoc("chocolate_milk"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ModItemTags.ALCOHOL_CREATABLE,4)
                .input(ModMedicalFluids.REACTION_LIQUID.get(),10)
                .input(Fluids.WATER,100)
                .output(ModMedicalFluids.ALCOHOL.get(),100)
                .save(consumer, CasualtiesCubed.resourceLoc("alcohol"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ModItemTags.ALCOHOL_CREATABLE,4)
                .input(Fluids.WATER,100)
                .processingTime(6000)
                .output(ModMedicalFluids.ALCOHOL.get(),100)
                .save(consumer, CasualtiesCubed.resourceLoc("alcohol_slow"));

        MedicalMixerRecipeBuilder.mixer()
                .inputM(ModMedicalFluidTags.DISINFECTING,100)
                .input(ModItemTags.DRESSINGS,1)
                .processingTime(200)
                .output(ModItems.STERILIZED_DRESSING.get(),1)
                .save(consumer, CasualtiesCubed.resourceLoc("sterilized_dressing"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ModMedicalFluids.OPIUM.get(),100)
                .input(Items.FERMENTED_SPIDER_EYE,1)
                .output(ModMedicalFluids.MORPHINE.get(),50)
                .save(consumer, CasualtiesCubed.resourceLoc("morphine"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.FERMENTED_SPIDER_EYE,1)
                .input(Items.SUGAR,2)
                .input(Fluids.WATER,100)
                .output(ModMedicalFluids.HEROIN.get(),100)
                .processingTime(1200)
                .save(consumer, CasualtiesCubed.resourceLoc("heroin"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.GLISTERING_MELON_SLICE,4)
                .input(Items.FERMENTED_SPIDER_EYE,1)
                .input(ModMedicalFluids.MORPHINE.get(),40)
                .input(ModMedicalFluids.REACTION_LIQUID.get(),10)
                .output(ModMedicalFluids.FENTANYL.get(),5)
                .save(consumer, CasualtiesCubed.resourceLoc("fentanyl"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.SUGAR,2)
                .inputM(ModMedicalFluidTags.OPIOIDS,25)
                .output(ModMedicalFluids.PAINKILLERS.get(), 50)
                .save(consumer, CasualtiesCubed.resourceLoc("painkillers"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ItemTags.FISHES,8)
                .input(Items.EGG,2)
                .input(Fluids.WATER,20)
                .input(Items.GOLD_INGOT,1)
                .output(ModMedicalFluids.BRAINGROW.get(),20)
                .save(consumer, CasualtiesCubed.resourceLoc("brain_grow"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ModMedicalFluids.ALCOHOL.get(),100)
                .input(Items.GUNPOWDER,1)
                .input(ModMedicalFluids.REACTION_LIQUID.get(),10)
                .output(ModMedicalFluids.ANTISEPTIC.get(),50)
                .save(consumer, CasualtiesCubed.resourceLoc("antiseptic"));

        MedicalMixerRecipeBuilder.mixer()
                .inputM(ModMedicalFluidTags.OPIOIDS,10)
                .input(Items.SLIME_BALL,1)
                .input(Fluids.WATER,100)
                .output(ModMedicalFluids.RELIEF_CREAM.get(), 100)
                .save(consumer, CasualtiesCubed.resourceLoc("relief_cream"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Fluids.WATER,250)
                .input(Items.SUGAR,1)
                .output(ModMedicalFluids.SALINE.get(), 250)
                .save(consumer, CasualtiesCubed.resourceLoc("saline"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.BROWN_MUSHROOM,1)
                .input(Items.SUGAR,1)
                .input(Items.FERMENTED_SPIDER_EYE,1)
                .output(ModMedicalFluids.ANTIBIOTICS.get(), 10)
                .save(consumer, CasualtiesCubed.resourceLoc("antibiotics"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ModMedicalFluids.ANTIBIOTICS.get(),50)
                .input(Fluids.WATER,50)
                .input(Items.FERMENTED_SPIDER_EYE,1)
                .input(ModMedicalFluids.REACTION_LIQUID.get(),10)
                .output(ModMedicalFluids.ANTISERUM.get(), 25)
                .save(consumer, CasualtiesCubed.resourceLoc("antiserum"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ModMedicalFluids.ANTISERUM.get(),10)
                .input(Fluids.WATER,10)
                .output(ModMedicalFluids.ANTISERUM.get(), 20)
                .processingTime(3000)
                .save(consumer, CasualtiesCubed.resourceLoc("antiserum_grow"));

        MedicalMixerRecipeBuilder.mixer()
                .processingTime(200)
                .input(Fluids.WATER,10)
                .input(ModMedicalFluids.REACTION_LIQUID.get(),50)
                .input(Items.GUNPOWDER,1)
                .output(ModMedicalFluids.CEFTRIAXONE.get(), 20)
                .save(consumer, CasualtiesCubed.resourceLoc("ceftriaxone"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.SPIDER_EYE,2)
                .input(Items.SUGAR,1)
                .input(Fluids.WATER,20)
                .output(ModMedicalFluids.PROCOAGULANT.get(),20)
                .save(consumer, CasualtiesCubed.resourceLoc("procoagulant"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.SUGAR,1)
                .input(Items.GUNPOWDER,2)
                .input(Items.GLOWSTONE_DUST,1)
                .input(Fluids.WATER,20)
                .output(ModMedicalFluids.STREPTOKINASE.get(), 20)
                .save(consumer, CasualtiesCubed.resourceLoc("streptokinase"));

        MedicalMixerRecipeBuilder.mixer()
                .inputM(ModMedicalFluidTags.OPIOIDS,50)
                .input(Items.GLISTERING_MELON_SLICE,2)
                .output(ModMedicalFluids.NALOXONE.get(),25)
                .save(consumer, CasualtiesCubed.resourceLoc("naloxone"));

        MedicalMixerRecipeBuilder.mixer()
                .inputM(ModMedicalFluidTags.OPIOIDS,25)
                .input(ModMedicalFluids.REACTION_LIQUID.get(),25)
                .input(ModItemTags.DRESSINGS,1)
                .input(Items.STRING,1)
                .output(ModItems.BRUISE_KIT.get(), 1)
                .save(consumer, CasualtiesCubed.resourceLoc("bruise_kit"));

        MedicalMixerRecipeBuilder.mixer()
                .inputM(ModMedicalFluidTags.OPIOIDS,50)
                .input(ItemTags.WOOL,1)
                .input(Items.STRING,2)
                .output(ModItems.MEDICAL_GAUZE.get(), 1)
                .save(consumer, CasualtiesCubed.resourceLoc("medical_gauze"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ItemTags.WOOL,2)
                .inputM(ModMedicalFluidTags.DISINFECTING,20)
                .output(ModItems.ALGINATE_DRESSING.get(), 1)
                .save(consumer, CasualtiesCubed.resourceLoc("alganate_dressing"));

        MedicalMixerRecipeBuilder.mixer()
                .inputM(ModMedicalFluidTags.DISINFECTING,25)
                .inputM(ModMedicalFluidTags.OPIOIDS,25)
                .input(ModMedicalFluids.REACTION_LIQUID.get(),25)
                .output(ModMedicalFluids.LRD_SERUM.get(), 25)
                .save(consumer, CasualtiesCubed.resourceLoc("lrd_serum"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.MAKESHIFT_LRD.get())
                .pattern(" I ")
                .pattern("IRI")
                .pattern("IGI")
                .define('I',Items.IRON_INGOT)
                .define('R',Items.REDSTONE)
                .define('G',Items.GOLD_INGOT)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer, CasualtiesCubed.resourceLoc("m_lrd"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.LRD.get())
                .pattern(" I ")
                .pattern("CMC")
                .pattern("CDC")
                .define('I',Items.IRON_INGOT)
                .define('M',ModItems.MAKESHIFT_LRD.get())
                .define('C',Items.COPPER_INGOT)
                .define('D',Items.DIAMOND)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer, CasualtiesCubed.resourceLoc("lrd"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.MEDICAL_MIXER.get())
                .pattern("MMM")
                .pattern("IPI")
                .pattern("PGP")
                .define('M',ModItems.MEDICINE_VIAL.get())
                .define('I',Items.IRON_INGOT)
                .define('P',ItemTags.PLANKS)
                .define('G',Items.GOLD_INGOT)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer, CasualtiesCubed.resourceLoc("med_mixer"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,Items.GLASS_BOTTLE)
                .requires(ModItemTags.VIAL_ITEMS)
                .unlockedBy("has_poppy", has(Items.AIR))
                .save(consumer, CasualtiesCubed.resourceLoc("all_to_bottle"));

        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.MEDICINE_VIAL.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.BOTTLE.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC, ModItems.PILL_BOTTLE.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.AUTO_INJECTOR.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.ANTISERUM_INJECTOR.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.STREPTOKINASE_INJECTOR.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.PROCOAGULANT_INJECTOR.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC, ModItems.OPIUM_VIAL.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.NALOXONE_VIAL.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.MORPHINE_VIAL.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC,ModItems.FENTANYL_VIAL.get(),Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer,RecipeCategory.MISC, ModItems.CEFTRIAXONE_VIAL.get(),Items.GLASS_BOTTLE);


        // Botany Pots Compatibility
        BasicCropRecipeProvider.buildRecipes(consumer);
    }

    protected static void stonecutterResultFromBase(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial) {
        stonecutterResultFromBase(pFinishedRecipeConsumer, pCategory, pResult, pMaterial, 1);
    }

    protected static void stonecutterResultFromBase(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial, int pResultCount) {
        SingleItemRecipeBuilder var10000 = SingleItemRecipeBuilder.stonecutting(Ingredient.of(pMaterial), pCategory, pResult, pResultCount).unlockedBy(getHasName(pMaterial), has(pMaterial));
        String var10002 = getConversionRecipeName(pResult, pMaterial);
        var10000.save(pFinishedRecipeConsumer, var10002 + "_stonecutting");
    }
}
