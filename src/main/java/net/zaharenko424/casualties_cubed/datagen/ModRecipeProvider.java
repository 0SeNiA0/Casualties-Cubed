package net.zaharenko424.casualties_cubed.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.datagen.recipes.crop.BasicCropRecipeProvider;
import net.zaharenko424.casualties_cubed.datagen.recipes.MedicalMixerRecipeBuilder;
import net.zaharenko424.casualties_cubed.registry.ModFluids;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SYRINGE.get())
                .pattern("P")
                .pattern("P")
                .pattern("I")
                .define('P', Ingredient.of(Tags.Items.GLASS_PANES))
                .define('I', Ingredient.of(Tags.Items.NUGGETS_IRON))
                .unlockedBy(getHasName(Blocks.GLASS), has(Tags.Items.GLASS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEROIN_SYRINGE.get())
                .pattern("P")
                .pattern("B")
                .pattern("I")
                .define('P', Ingredient.of(Tags.Items.GLASS_PANES))
                .define('B', Ingredient.of(Items.GLASS_BOTTLE))
                .define('I', Ingredient.of(Tags.Items.NUGGETS_IRON))
                .unlockedBy(getHasName(Blocks.GLASS), has(Tags.Items.GLASS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IV_BAG.get())
                .pattern("B")
                .pattern("B")
                .pattern("I")
                .define('B', Ingredient.of(Items.GLASS_BOTTLE))
                .define('I', Ingredient.of(Tags.Items.NUGGETS_IRON))
                .unlockedBy(getHasName(Blocks.GLASS), has(Tags.Items.GLASS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CANTEEN.get())
                .pattern(" N ")
                .pattern("I I")
                .pattern(" I ")
                .define('N', Ingredient.of(Tags.Items.NUGGETS_IRON))
                .define('I', Ingredient.of(Tags.Items.INGOTS_IRON))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ALCOHOL_BOTTLE.get())
                .pattern("N")
                .pattern("G")
                .pattern("G")
                .define('N', Ingredient.of(Tags.Items.NUGGETS_IRON))
                .define('G', Ingredient.of(Tags.Items.GLASS))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WATER_BOTTLE.get())
                .pattern("S")
                .pattern("P")
                .pattern("P")
                .define('S', Ingredient.of(Tags.Items.SLIMEBALLS))
                .define('P', Ingredient.of(Items.PHANTOM_MEMBRANE))
                .unlockedBy(getHasName(Items.PHANTOM_MEMBRANE), has(Items.PHANTOM_MEMBRANE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WATER_JUG.get())
                .pattern(" P ")
                .pattern("PSP")
                .pattern("PPP")
                .define('S', Ingredient.of(Tags.Items.SLIMEBALLS))
                .define('P', Ingredient.of(Items.PHANTOM_MEMBRANE))
                .unlockedBy(getHasName(Items.PHANTOM_MEMBRANE), has(Items.PHANTOM_MEMBRANE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.EXPIE_PLUSHY.get())
                .pattern("OYO")
                .pattern("WWW")
                .pattern("OWO")
                .define('O', Ingredient.of(Tags.Items.DYES_ORANGE))
                .define('Y', Ingredient.of(Tags.Items.DYES_YELLOW))
                .define('W', Ingredient.of(Items.BLACK_WOOL))
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.OLD_RAG.get())
                .requires(Tags.Items.STRING)
                .requires(Tags.Items.STRING)
                .unlockedBy(getHasName(Items.STRING), has(Tags.Items.STRING))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RIPPED_DRESSING.get())
                .requires(Tags.Items.STRING)
                .requires(Tags.Items.STRING)
                .requires(Tags.Items.STRING)
                .unlockedBy(getHasName(Items.STRING), has(Tags.Items.STRING))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADHESIVE_BANDAGE.get())
                .pattern("PHP")
                .define('P', Items.PAPER)
                .define('H', Items.HONEY_BOTTLE)
                .unlockedBy(getHasName(Items.HONEY_BOTTLE), has(Items.HONEY_BOTTLE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRESSING.get())
                .pattern(" S ")
                .pattern("RWR")
                .pattern(" S ")
                .define('S', Tags.Items.STRING)
                .define('R', ModItems.RIPPED_DRESSING.get())
                .define('W', ItemTags.WOOL)
                .unlockedBy(getHasName(ModItems.RIPPED_DRESSING.get()), has(ModItems.RIPPED_DRESSING.get()))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PLASTIC_DRESSING.get())
                .requires(CasualtiesCubedTags.Item.DRESSINGS)
                .requires(Tags.Items.SLIMEBALLS)
                .unlockedBy(getHasName(ModItems.DRESSING.get()), has(ModItems.DRESSING.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_WELDER.get())
                .pattern("II")
                .pattern("RI")
                .pattern("RG")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('G', Tags.Items.GUNPOWDER)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.THERMOMETER.get())
                .pattern(" C ")
                .pattern("CRC")
                .pattern("CGC")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('G', Tags.Items.INGOTS_GOLD)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Tags.Items.INGOTS_GOLD))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDICAL_SUTURE.get())
                .pattern("GSS")
                .pattern("G S")
                .pattern("N S")
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('S', Tags.Items.STRING)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Tags.Items.INGOTS_GOLD))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ICE_PACK.get())
                .requires(Items.ICE)
                .requires(Items.SLIME_BALL)
                .requires(Items.SLIME_BALL)
                .requires(Items.ICE)
                .unlockedBy(getHasName(Items.SLIME_BALL), has(Tags.Items.SLIMEBALLS))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.HEAT_PACK.get())
                .requires(Tags.Items.GUNPOWDER)
                .requires(ItemTags.WOOL)
                .requires(Tags.Items.GUNPOWDER)
                .requires(Tags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Tags.Items.GUNPOWDER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMALL_MEDIBAG.get())
                .pattern("NNN")
                .pattern("WLW")
                .define('N', Tags.Items.INGOTS_IRON)
                .define('W', ItemTags.WOOL)
                .define('L', Items.LEATHER)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDIUM_MEDIBAG.get())
                .pattern("GWG")
                .pattern("SLS")
                .define('W', ItemTags.WOOL)
                .define('L', Items.LEATHER)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('S', ModItems.SMALL_MEDIBAG.get())
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Tags.Items.INGOTS_GOLD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LARGE_MEDIBAG.get())
                .pattern("DWD")
                .pattern("MNM")
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('W', ItemTags.WOOL)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('M', ModItems.MEDIUM_MEDIBAG.get())
                .unlockedBy(getHasName(Items.DIAMOND), has(Tags.Items.GEMS_DIAMOND))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPLINT.get())
                .requires(Items.STICK)
                .requires(Items.STICK)
                .requires(Tags.Items.STRING)
                .unlockedBy(getHasName(Items.STRING), has(Tags.Items.STRING))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TWEEZERS.get())
                .pattern("IN")
                .pattern(" I")
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOURNIQUET.get())
                .pattern(" I ")
                .pattern("WLW")
                .define('W', ItemTags.WOOL)
                .define('L', Items.LEATHER)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BROWN_CAP_MUSH.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ModItems.BROWN_CAP.get())
                .define('B', Items.BOWL)
                .unlockedBy(getHasName(ModItems.BROWN_CAP.get()), has(ModItems.BROWN_CAP.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXPERIMENTAL_TREATMENT.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern("GDG")
                .define('M', ModItems.BROWN_CAP_MUSH.get())
                .define('G', Items.GLISTERING_MELON_SLICE)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy(getHasName(ModItems.BROWN_CAP_MUSH.get()), has(ModItems.BROWN_CAP_MUSH.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MAKESHIFT_LRD.get())
                .pattern(" I ")
                .pattern("IRI")
                .pattern("IGI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('G', Tags.Items.INGOTS_GOLD)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Tags.Items.INGOTS_GOLD))
                .save(consumer, CasualtiesCubed.resourceLoc("m_lrd"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LRD.get())
                .pattern(" I ")
                .pattern("CMC")
                .pattern("CDC")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('M', ModItems.MAKESHIFT_LRD.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy(getHasName(Items.DIAMOND), has(Tags.Items.GEMS_DIAMOND))
                .save(consumer, CasualtiesCubed.resourceLoc("lrd"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AUTO_PUMP.get())
                .pattern("IGI")
                .pattern("GRG")
                .pattern("IDI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer, ModItems.AUTO_PUMP.getId());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CHEST_DRAIN.get())
                .pattern(" DD")
                .pattern("RTD")
                .pattern("GTI")
                .define('T', Items.TINTED_GLASS)
                .define('D', Items.DRIED_KELP)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer, CasualtiesCubed.resourceLoc("chest_drain"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDICAL_MIXER.get())
                .pattern("MMM")
                .pattern("IPI")
                .pattern("PGP")
                .define('M', ModItems.MEDICINE_VIAL.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', ItemTags.PLANKS)
                .define('G', Tags.Items.INGOTS_GOLD)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer, CasualtiesCubed.resourceLoc("med_mixer"));



        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(Fluids.WATER, 1000)
                .input(Items.PAPER)
                .input(Items.CHARCOAL)
                .outputFluid(ModFluids.CLEAN_WATER, 1000)
                .save(consumer, CasualtiesCubed.resourceLoc("clean_water_vanilla"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.GROUNDWATER, 1000)
                .input(Items.PAPER)
                .input(Items.CHARCOAL)
                .outputFluid(ModFluids.CLEAN_WATER, 1000)
                .save(consumer, CasualtiesCubed.resourceLoc("clean_water"));



        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(Fluids.WATER, 10)
                .input(ModItems.GLOW_FRUIT.get())
                .outputFluid(ModFluids.BIO_CHEM.get(), 10)
                .save(consumer, CasualtiesCubed.resourceLoc("reaction_liquid"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(Fluids.WATER, 25)
                .input(Items.POPPY)
                .input(Items.SUGAR, 2)
                .outputFluid(ModFluids.OPIUM.get(), 25)
                .save(consumer, CasualtiesCubed.resourceLoc("opium"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.MILK_BUCKET, 1)
                .input(Items.COCOA_BEANS, 8)
                .outputFluid(ModFluids.CHOCOLATE_MILK.get(), 1000)
                .output(Items.BUCKET, 1)
                .save(consumer, CasualtiesCubed.resourceLoc("chocolate_milk"));

        MedicalMixerRecipeBuilder.mixer()
                .input(CasualtiesCubedTags.Item.ALCOHOL_CREATABLE, 4)
                .inputFluid(ModFluids.BIO_CHEM.get(), 10)
                .inputFluid(Fluids.WATER, 100)
                .outputFluid(ModFluids.ALCOHOL.get(), 100)
                .save(consumer, CasualtiesCubed.resourceLoc("alcohol"));

        MedicalMixerRecipeBuilder.mixer()
                .input(CasualtiesCubedTags.Item.ALCOHOL_CREATABLE, 4)
                .inputFluid(Fluids.WATER, 100)
                .processingTime(6000)
                .outputFluid(ModFluids.ALCOHOL.get(), 100)
                .save(consumer, CasualtiesCubed.resourceLoc("alcohol_slow"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(CasualtiesCubedTags.Fluid.DISINFECTING, 100)
                .input(CasualtiesCubedTags.Item.DRESSINGS, 1)
                .processingTime(200)
                .output(ModItems.STERILIZED_DRESSING.get(), 1)
                .save(consumer, CasualtiesCubed.resourceLoc("sterilized_dressing"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.OPIUM.get(), 100)
                .input(Items.FERMENTED_SPIDER_EYE, 1)
                .outputFluid(ModFluids.MORPHINE.get(), 50)
                .save(consumer, CasualtiesCubed.resourceLoc("morphine"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.FERMENTED_SPIDER_EYE, 1)
                .input(Items.SUGAR, 2)
                .inputFluid(Fluids.WATER, 100)
                .outputFluid(ModFluids.HEROIN.get(), 100)
                .processingTime(1200)
                .save(consumer, CasualtiesCubed.resourceLoc("heroin"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.FERMENTED_SPIDER_EYE, 1)
                .input(Items.GLISTERING_MELON_SLICE, 4)
                .inputFluid(ModFluids.MORPHINE.get(), 40)
                .inputFluid(ModFluids.BIO_CHEM.get(), 10)
                .outputFluid(ModFluids.FENTANYL.get(), 5)
                .save(consumer, CasualtiesCubed.resourceLoc("fentanyl"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.SUGAR, 2)
                .inputFluid(CasualtiesCubedTags.Fluid.OPIATES, 25)
                .outputFluid(ModFluids.PAINKILLERS.get(), 50)
                .save(consumer, CasualtiesCubed.resourceLoc("painkillers"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ItemTags.FISHES, 8)
                .input(Tags.Items.EGGS, 2)
                .input(Tags.Items.INGOTS_GOLD, 1)
                .inputFluid(Fluids.WATER, 20)
                .outputFluid(ModFluids.BRAINGROW.get(), 20)
                .save(consumer, CasualtiesCubed.resourceLoc("brain_grow"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.ALCOHOL.get(), 100)
                .input(Tags.Items.GUNPOWDER, 1)
                .inputFluid(ModFluids.BIO_CHEM.get(), 10)
                .outputFluid(ModFluids.ANTISEPTIC.get(), 50)
                .save(consumer, CasualtiesCubed.resourceLoc("antiseptic"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(CasualtiesCubedTags.Fluid.OPIATES, 10)
                .input(Tags.Items.SLIMEBALLS, 1)
                .inputFluid(Fluids.WATER, 100)
                .outputFluid(ModFluids.RELIEF_CREAM.get(), 100)
                .save(consumer, CasualtiesCubed.resourceLoc("relief_cream"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.CLEAN_WATER, 150)
                .inputFluid(ModFluids.BIO_CHEM, 10)
                .inputFluid(ModFluids.RED_BLOOD, 15)
                .outputFluid(ModFluids.SALINE.get(), 175)
                .save(consumer, CasualtiesCubed.resourceLoc("saline_red"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.CLEAN_WATER, 150)
                .inputFluid(ModFluids.BIO_CHEM, 10)
                .inputFluid(ModFluids.YELLOW_BLOOD, 15)
                .outputFluid(ModFluids.SALINE.get(), 175)
                .save(consumer, CasualtiesCubed.resourceLoc("saline_yellow"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.BROWN_MUSHROOM, 1)
                .input(Items.SUGAR, 1)
                .input(Items.FERMENTED_SPIDER_EYE, 1)
                .outputFluid(ModFluids.ANTIBIOTICS.get(), 10)
                .save(consumer, CasualtiesCubed.resourceLoc("antibiotics"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.ANTIBIOTICS.get(), 50)
                .inputFluid(Fluids.WATER, 50)
                .input(Items.FERMENTED_SPIDER_EYE, 1)
                .inputFluid(ModFluids.BIO_CHEM.get(), 10)
                .outputFluid(ModFluids.ANTISERUM.get(), 25)
                .save(consumer, CasualtiesCubed.resourceLoc("antiserum"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.ANTISERUM.get(), 10)
                .inputFluid(Fluids.WATER, 10)
                .outputFluid(ModFluids.ANTISERUM.get(), 20)
                .processingTime(3000)
                .save(consumer, CasualtiesCubed.resourceLoc("antiserum_grow"));

        MedicalMixerRecipeBuilder.mixer()
                .processingTime(200)
                .inputFluid(Fluids.WATER, 10)
                .inputFluid(ModFluids.BIO_CHEM.get(), 50)
                .input(Tags.Items.GUNPOWDER, 1)
                .outputFluid(ModFluids.CEFTRIAXONE.get(), 20)
                .save(consumer, CasualtiesCubed.resourceLoc("ceftriaxone"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.SPIDER_EYE, 2)
                .input(Items.SUGAR, 1)
                .inputFluid(Fluids.WATER, 20)
                .outputFluid(ModFluids.PROCOAGULANT.get(), 20)
                .save(consumer, CasualtiesCubed.resourceLoc("procoagulant"));

        MedicalMixerRecipeBuilder.mixer()
                .input(Items.SUGAR, 1)
                .input(Tags.Items.GUNPOWDER, 2)
                .input(Tags.Items.DUSTS_GLOWSTONE, 1)
                .inputFluid(Fluids.WATER, 20)
                .outputFluid(ModFluids.STREPTOKINASE.get(), 20)
                .save(consumer, CasualtiesCubed.resourceLoc("streptokinase"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(CasualtiesCubedTags.Fluid.OPIATES, 50)
                .input(Items.GLISTERING_MELON_SLICE, 2)
                .outputFluid(ModFluids.NALOXONE.get(), 25)
                .save(consumer, CasualtiesCubed.resourceLoc("naloxone"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(CasualtiesCubedTags.Fluid.OPIATES, 25)
                .inputFluid(ModFluids.BIO_CHEM.get(), 20)
                .input(CasualtiesCubedTags.Item.DRESSINGS, 1)
                .input(Tags.Items.STRING, 1)
                .output(ModItems.BRUISE_KIT.get(), 1)
                .save(consumer, CasualtiesCubed.resourceLoc("bruise_kit"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(CasualtiesCubedTags.Fluid.OPIATES, 50)
                .input(ItemTags.WOOL, 1)
                .input(Tags.Items.STRING, 2)
                .output(ModItems.MEDICAL_GAUZE.get(), 1)
                .save(consumer, CasualtiesCubed.resourceLoc("medical_gauze"));

        MedicalMixerRecipeBuilder.mixer()
                .input(ItemTags.WOOL, 2)
                .inputFluid(CasualtiesCubedTags.Fluid.DISINFECTING, 20)
                .output(ModItems.ALGINATE_DRESSING.get(), 1)
                .save(consumer, CasualtiesCubed.resourceLoc("alganate_dressing"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(CasualtiesCubedTags.Fluid.DISINFECTING, 25)
                .inputFluid(CasualtiesCubedTags.Fluid.OPIATES, 25)
                .inputFluid(ModFluids.BIO_CHEM, 25)
                .outputFluid(ModFluids.LRD_SERUM.get(), 25)
                .save(consumer, CasualtiesCubed.resourceLoc("lrd_serum"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.BIO_CHEM, 5)
                .inputFluid(ModFluids.RED_BLOOD, 25)
                .inputFluid(CasualtiesCubedTags.Fluid.DISINFECTING, 10)
                .input(Items.SPIDER_EYE)
                .outputFluid(ModFluids.ANTIVENOM, 50)
                .save(consumer, CasualtiesCubed.resourceLoc("antivenom_red"));

        MedicalMixerRecipeBuilder.mixer()
                .inputFluid(ModFluids.BIO_CHEM, 5)
                .inputFluid(ModFluids.YELLOW_BLOOD, 25)
                .inputFluid(CasualtiesCubedTags.Fluid.DISINFECTING, 10)
                .input(Items.SPIDER_EYE)
                .outputFluid(ModFluids.ANTIVENOM, 50)
                .save(consumer, CasualtiesCubed.resourceLoc("antivenom_yellow"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GLASS_BOTTLE)
                .requires(CasualtiesCubedTags.Item.VIAL_ITEMS)
                .unlockedBy(getHasName(ModItems.MEDICINE_VIAL.get()), has(CasualtiesCubedTags.Item.VIAL_ITEMS))
                .save(consumer, CasualtiesCubed.resourceLoc("all_to_bottle"));

        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.MEDICINE_VIAL.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.WATER_BOTTLE.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.PILL_BOTTLE.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.AUTO_INJECTOR.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.ANTISERUM_INJECTOR.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.STREPTOKINASE_INJECTOR.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.PROCOAGULANT_INJECTOR.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.OPIUM_VIAL.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.NALOXONE_VIAL.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.MORPHINE_VIAL.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.FENTANYL_VIAL.get(), Items.GLASS_BOTTLE);
        stonecutterResultFromBase(consumer, RecipeCategory.MISC, ModItems.CEFTRIAXONE_VIAL.get(), Items.GLASS_BOTTLE);

        stonecuttingAllToAll(consumer, RecipeCategory.MISC, ModItems.BLOOD_BAG, ModItems.IV_BAG);
        stonecuttingAllToAll(consumer, RecipeCategory.MISC, ModItems.WATER_BOTTLE, ModItems.ANTISEPTIC_SPRAY, ModItems.WOUND_GLUE_SPRAY, ModItems.RELIEF_CREAM_BOTTLE);
        stonecuttingAllToAll(consumer, RecipeCategory.MISC, ModItems.WATER_JUG, ModItems.BLEACH_JUG);


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

    @SafeVarargs
    private void stonecuttingAllToAll(Consumer<FinishedRecipe> out, RecipeCategory category, RegistryObject<? extends Item>... items) {
        for (RegistryObject<? extends Item> material : items) {
            for (RegistryObject<? extends Item> result : items) {
                if (material == result) continue;
                SingleItemRecipeBuilder.stonecutting(Ingredient.of(material.get()), category, result.get())
                        .unlockedBy(getHasName(material.get()), has(material.get()))
                        .save(out, result.getId().withSuffix("_stonecutting_from_" + material.getId().getPath()));
            }
        }
    }
}
