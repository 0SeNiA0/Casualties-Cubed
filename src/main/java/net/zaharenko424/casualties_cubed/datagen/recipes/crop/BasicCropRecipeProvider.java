package net.zaharenko424.casualties_cubed.datagen.recipes.crop;

import net.darkhax.botanypots.data.displaystate.DisplayState;
import net.darkhax.botanypots.data.displaystate.SimpleDisplayState;
import net.darkhax.botanypots.data.displaystate.TransitionalDisplayState;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.registry.ModItems;

import java.util.List;
import java.util.function.Consumer;

public class BasicCropRecipeProvider {

    public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // Example: Sweet Berries with age phases

        // 1. Define the individual states (ages 0, 1, and 2)
        DisplayState age0 = new SimpleDisplayState(Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, 0));
        DisplayState age1 = new SimpleDisplayState(Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, 1));
        DisplayState age2 = new SimpleDisplayState(Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, 2));

        // 2. Wrap them in a Transitional state
        DisplayState transitionalDisplay = new TransitionalDisplayState(List.of(age0, age1, age2));

        // 3. Build and save the recipe
        new CropRecipeBuilder(Ingredient.of(ModItems.GLOW_FRUIT.get()))
                .addCategory("dirt").addCategory("stone")
                .setGrowthTicks(1200)
                .setLightLevel(0)
                .addDisplayState(transitionalDisplay)
                .addDrop(ModItems.GLOW_FRUIT.get(), 1.0f, 1, 1)
                .save(consumer, CasualtiesCubed.resourceLoc("crop/glow_fruit"));
    }
}