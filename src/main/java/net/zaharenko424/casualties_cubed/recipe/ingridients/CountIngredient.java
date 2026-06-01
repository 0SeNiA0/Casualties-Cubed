package net.zaharenko424.casualties_cubed.recipe.ingridients;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Predicate;

public class CountIngredient implements Predicate<ItemStack> {

    private final Ingredient ingredient;
    private final int count;

    /* ---------- Constructors ---------- */

    public CountIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    /* ---------- Info ---------- */

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getCount() {
        return count;
    }

    /* ---------- Matching ---------- */

    @Override
    public boolean test(ItemStack stack) {
        if (stack.getCount() < count) return false;

        return ingredient.test(stack);
    }

    /* ---------- Serialization ---------- */

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("Ingredient", ingredient.toJson());
        json.addProperty("count", count);
        return json;
    }

    public static CountIngredient fromJson(JsonObject obj) {
        return new CountIngredient(Ingredient.fromJson(obj.get("Ingredient")), GsonHelper.getAsInt(obj, "count", 1));
    }

    public void toNetwork(FriendlyByteBuf buf) {
        ingredient.toNetwork(buf);
        buf.writeVarInt(count);
    }

    public static CountIngredient fromNetwork(FriendlyByteBuf buf) {
        return new CountIngredient(Ingredient.fromNetwork(buf), buf.readVarInt());
    }
}
