package net.zaharenko424.casualties_cubed.recipe;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.zaharenko424.casualties_cubed.recipe.ingridients.CountIngredient;
import net.zaharenko424.casualties_cubed.recipe.ingridients.FluidIngredient;
import net.zaharenko424.casualties_cubed.registry.ModRecipes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MedicalMixerRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;

    private final List<CountIngredient> itemInputs;
    private final List<FluidIngredient> fluidInputs;

    private final List<ItemStack> itemOutputs;
    private final List<FluidStack> fluidOutputs;

    private final int processingTime;

    public MedicalMixerRecipe(ResourceLocation id, List<CountIngredient> itemInputs, List<FluidIngredient> fluidInputs, List<ItemStack> itemOutputs, List<FluidStack> fluidOutputs, int processingTime) {
        this.id = id;
        this.itemInputs = itemInputs;
        this.fluidInputs = fluidInputs;
        this.itemOutputs = itemOutputs;
        this.fluidOutputs = fluidOutputs;
        this.processingTime = processingTime;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        int size = Math.min(5, itemInputs.size());
        for (int i = 0; i < size; i++) {
            if (!itemInputs.get(i).test(simpleContainer.getItem(i))) return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MEDICAL_MIXER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MEDICAL_MIXER_RECIPE.get();
    }

    public List<CountIngredient> getItemInputs() {
        return itemInputs;
    }

    public List<FluidIngredient> getFluidInputs() {
        return fluidInputs;
    }

    public List<ItemStack> getItemOutputs() {
        return itemOutputs;
    }

    public List<FluidStack> getFluidOutputs() {
        return fluidOutputs;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public boolean matches(IItemHandler currentItems, List<FluidStack> currentFluids) {
        int size = Math.min(5, itemInputs.size());
        for (int i = 0; i < size; i++) {
            if (!itemInputs.get(i).test(currentItems.getStackInSlot(i))) return false;
        }

        size = Math.min(3, fluidInputs.size());
        for (int i = 0; i < size; i++) {
            if (!fluidInputs.get(i).matches(currentFluids.get(i))) return false;
        }

        return true;
    }

    public static class Serializer implements RecipeSerializer<MedicalMixerRecipe> {

        @Override
        public MedicalMixerRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
            int time = GsonHelper.getAsInt(json, "processingTime", 100);

            List<CountIngredient> itemInputs = parseItemIngredients(json);
            List<FluidIngredient> fluidInputs = parseFluidIngredients(json);
            List<ItemStack> itemOutputs = parseItemOutputs(json);
            List<FluidStack> fluidOutputs = parseFluidOutputs(json);

            return new MedicalMixerRecipe(resourceLocation, itemInputs, fluidInputs, itemOutputs, fluidOutputs, time);
        }

        private List<FluidStack> parseFluidOutputs(JsonObject json) {
            List<FluidStack> fluidOutputs = new ArrayList<>(3);
            if (json.has("fluid_outputs")) {
                for (var el : GsonHelper.getAsJsonArray(json, "fluid_outputs")) {
                    JsonObject obj = el.getAsJsonObject();
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.parse(GsonHelper.getAsString(obj, "fluid")));
                    int amount = GsonHelper.getAsInt(obj, "amount");
                    FluidStack stack = new FluidStack(fluid, amount);

                    if (obj.has("nbt")) {
                        try {
                            String snbt = GsonHelper.getAsString(obj, "nbt");
                            stack.setTag(TagParser.parseTag(snbt));
                        } catch (CommandSyntaxException e) {
                            throw new RuntimeException("Invalid SNBT in fluid_outputs", e);
                        }
                    }

                    fluidOutputs.add(stack);
                }
            }
            return fluidOutputs;
        }

        private List<ItemStack> parseItemOutputs(JsonObject json) {
            List<ItemStack> itemOutputs = new ArrayList<>();
            if (json.has("item_outputs")) {
                for (var el : GsonHelper.getAsJsonArray(json, "item_outputs")) {
                    JsonObject obj = el.getAsJsonObject();
                    Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(GsonHelper.getAsString(obj, "item")));
                    int count = GsonHelper.getAsInt(obj, "count", 1);
                    itemOutputs.add(new ItemStack(item, count));
                }
            }
            return itemOutputs;
        }


        private List<FluidIngredient> parseFluidIngredients(JsonObject json) {
            List<FluidIngredient> list = new ArrayList<>();
            if (json.has("fluid_inputs")) {
                for (var el : GsonHelper.getAsJsonArray(json, "fluid_inputs")) {
                    FluidIngredient ingredient = FluidIngredient.fromJson(el.getAsJsonObject());
                    list.add(ingredient);

                }
            }
            return list;
        }

        private List<CountIngredient> parseItemIngredients(JsonObject json) {
            List<CountIngredient> list = new ArrayList<>();
            if (json.has("item_inputs")) {
                for (var el : GsonHelper.getAsJsonArray(json, "item_inputs")) {
                    CountIngredient ingredient = CountIngredient.fromJson(el.getAsJsonObject());
                    list.add(ingredient);

                }
            }
            return list;
        }

        @Override
        public @Nullable MedicalMixerRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            int time = buf.readInt();

            int itemInputCount = buf.readInt();
            List<CountIngredient> itemInputs = new ArrayList<>(itemInputCount);
            for (int i = 0; i < itemInputCount; i++) {
                itemInputs.add(CountIngredient.fromNetwork(buf));
            }

            int fluidInputsCount = buf.readInt();
            List<FluidIngredient> fluidInputs = new ArrayList<>(fluidInputsCount);
            for (int i = 0; i < fluidInputsCount; i++) {
                fluidInputs.add(FluidIngredient.fromNetwork(buf));
            }

            int itemOutputsCount = buf.readInt();
            List<ItemStack> itemOutputs = new ArrayList<>(itemOutputsCount);
            for (int i = 0; i < itemOutputsCount; i++) {
                ItemStack stack = buf.readItem();
                itemOutputs.add(stack);
            }

            int fluidOutputsCount = buf.readInt();
            List<FluidStack> fluidOutputs = new ArrayList<>(fluidOutputsCount);
            for (int i = 0; i < fluidOutputsCount; i++) {
                fluidOutputs.add(buf.readFluidStack());
            }
            return new MedicalMixerRecipe(resourceLocation, itemInputs, fluidInputs, itemOutputs, fluidOutputs, time);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MedicalMixerRecipe recipe) {
            // --- Processing Time ---
            buf.writeInt(recipe.getProcessingTime());

            // --- Item Inputs ---
            List<CountIngredient> itemInputs = recipe.getItemInputs();
            buf.writeInt(itemInputs.size());
            for (CountIngredient ing : itemInputs) {
                ing.toNetwork(buf);
            }

            // --- Fluid Inputs ---
            List<FluidIngredient> fluidInputs = recipe.getFluidInputs();
            buf.writeInt(fluidInputs.size());
            for (FluidIngredient ing : fluidInputs) {
                ing.toNetwork(buf);
            }

            // --- Item Outputs ---
            List<ItemStack> itemOutputs = recipe.getItemOutputs();
            buf.writeInt(itemOutputs.size());
            for (ItemStack stack : itemOutputs) {
                buf.writeItem(stack);
            }

            // --- Fluid Outputs ---
            List<FluidStack> fluidOutputs = recipe.getFluidOutputs();
            buf.writeInt(fluidOutputs.size());
            for (FluidStack stack : fluidOutputs) {
                buf.writeFluidStack(stack);
            }
        }
    }
}
