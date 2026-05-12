package net.zaharenko424.casualties_cubed.datagen.recipes.crop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.darkhax.botanypots.data.displaystate.DisplayState;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CropRecipeBuilder {
    private final Ingredient seed;
    private final List<String> categories = new ArrayList<>();
    private int growthTicks = 1200;
    private int lightLevel = 0;
    private final List<JsonObject> drops = new ArrayList<>();
    private final List<DisplayState> displayStates = new ArrayList<>();

    public CropRecipeBuilder(Ingredient seed) {
        this.seed = seed;
    }

    public static CropRecipeBuilder seed(Item item) {
        return new CropRecipeBuilder(Ingredient.of(item));
    }

    public CropRecipeBuilder addCategory(String category) {
        this.categories.add(category);
        return this;
    }

    public CropRecipeBuilder setGrowthTicks(int ticks) {
        this.growthTicks = ticks;
        return this;
    }

    public CropRecipeBuilder setLightLevel(int level) {
        this.lightLevel = level;
        return this;
    }

    public CropRecipeBuilder addDisplayState(DisplayState state) {
        this.displayStates.add(state);
        return this;
    }

    public CropRecipeBuilder addDrop(Item item, float chance, int min, int max) {
        JsonObject dropJson = new JsonObject();
        dropJson.addProperty("chance", chance);

        JsonObject itemObj = new JsonObject();
        itemObj.addProperty("item", item.toString());
        dropJson.add("item", itemObj);

        dropJson.addProperty("min", min);
        dropJson.addProperty("max", max);
        this.drops.add(dropJson);
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, seed, categories, growthTicks, drops, displayStates, lightLevel));
    }

    private record Result(ResourceLocation id, Ingredient seed, List<String> categories, int growthTicks,
                          List<JsonObject> drops, List<DisplayState> displayStates, int lightLevel) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("seed", seed.toJson());

            JsonArray categoryArray = new JsonArray();
            categories.forEach(categoryArray::add);
            json.add("categories", categoryArray);

            json.addProperty("growthTicks", growthTicks);

            JsonArray dropArray = new JsonArray();
            drops.forEach(dropArray::add);
            json.add("drops", dropArray);

            // Utilizing the official serializer for DisplayState
            JsonArray displayArray = new JsonArray();
            for (DisplayState state : displayStates) {
                displayArray.add(DisplayState.SERIALIZER.toJSON(state));
            }
            json.add("display", displayArray);

            json.addProperty("lightLevel", lightLevel);
        }

        @Override
        public ResourceLocation getId() { return id; }

        @Override
        public RecipeSerializer<?> getType() { return null; }

        @Override
        public JsonObject serializeRecipe() {
            JsonObject json = new JsonObject();
            json.addProperty("type", "botanypots:crop");
            this.serializeRecipeData(json);
            return json;
        }

        @Nullable @Override public JsonObject serializeAdvancement() { return null; }
        @Nullable @Override public ResourceLocation getAdvancementId() { return null; }
    }
}