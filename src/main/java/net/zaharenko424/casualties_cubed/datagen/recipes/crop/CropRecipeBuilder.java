package net.zaharenko424.casualties_cubed.datagen.recipes.crop;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.darkhax.botanypots.data.displaystate.DisplayState;
import net.darkhax.botanypots.data.recipes.crop.HarvestEntry;
import net.darkhax.botanypots.data.recipes.crop.SerializerHarvestEntry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    private final List<HarvestEntry> drops = new ArrayList<>();
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

    /**
     * Adiciona um drop padrão a partir de um Item.
     */
    public CropRecipeBuilder addDrop(Item item, float chance, int minRolls, int maxRolls) {
        return this.addDrop(new ItemStack(item), chance, minRolls, maxRolls);
    }

    /**
     * Adiciona um drop permitindo ItemStacks customizados (com NBT).
     */
    public CropRecipeBuilder addDrop(ItemStack stack, float chance, int minRolls, int maxRolls) {
        this.drops.add(new HarvestEntry(chance, stack, minRolls, maxRolls));
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, seed, categories, growthTicks, drops, displayStates, lightLevel));
    }

    private record Result(ResourceLocation id, Ingredient seed, List<String> categories, int growthTicks,
                          List<HarvestEntry> drops, List<DisplayState> displayStates, int lightLevel) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("seed", seed.toJson());

            JsonArray categoryArray = new JsonArray();
            categories.forEach(categoryArray::add);
            json.add("categories", categoryArray);

            json.addProperty("growthTicks", growthTicks);

            // Utiliza o Serializer oficial do Botany Pots para cada HarvestEntry
            JsonArray dropArray = new JsonArray();
            for (HarvestEntry entry : drops) {
                dropArray.add(SerializerHarvestEntry.SERIALIZER.toJSON(entry));
            }
            json.add("drops", dropArray);

            // Utiliza o Serializer oficial para DisplayState
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