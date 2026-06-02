package net.zaharenko424.casualties_cubed.recipe.ingridients;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class FluidIngredient {

    @Nullable
    private final Fluid fluid;

    @Nullable
    private final TagKey<Fluid> fluidTag;

    private final int amount;

    @Nullable
    private final CompoundTag nbt;

    /* ------------------------------------------------------------ */
    /* Constructors */
    /* ------------------------------------------------------------ */

    // Exact fluid
    public FluidIngredient(Fluid fluid, int amount, @Nullable CompoundTag nbt) {
        this.fluid = fluid;
        this.fluidTag = null;
        this.amount = amount;
        this.nbt = nbt;
    }

    // Fluid tag
    public FluidIngredient(TagKey<Fluid> tag, int amount, @Nullable CompoundTag nbt) {
        this.fluid = null;
        this.fluidTag = tag;
        this.amount = amount;
        this.nbt = nbt;
    }



    /* ------------------------------------------------------------ */
    /* Info */
    /* ------------------------------------------------------------ */

    public boolean isTagged() {
        return fluidTag != null;
    }

    @Nullable
    public TagKey<Fluid> getFluidTag() {
        return fluidTag;
    }

    @Nullable
    public Fluid getFluid() {
        return fluid;
    }

    public int getAmount() {
        return amount;
    }

    @Nullable
    public CompoundTag getNbt() {
        return nbt;
    }

    /* ------------------------------------------------------------ */
    /* Matching */
    /* ------------------------------------------------------------ */

    public boolean matches(FluidStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getAmount() < amount) return false;

        /* ---------- Forge fluid tag ---------- */
        if (fluidTag != null) {
            if (!stack.getFluid().is(fluidTag)) return false;
        }

        /* ---------- Exact fluid ---------- */
        if (fluid != null) {
            if (!stack.getFluid().isSame(fluid)) return false;
        }

        /* ---------- NBT ---------- */
        if (nbt != null) {
            return stack.hasTag() && nbt.equals(stack.getTag());
        }

        return true;
    }

    /* ------------------------------------------------------------ */
    /* Output */
    /* ------------------------------------------------------------ */

    public FluidStack getAsFluidStack() {
        FluidStack stack = new FluidStack(
                fluid != null ? fluid : Fluids.EMPTY,
                amount
        );

        if (nbt != null) {
            stack.setTag(nbt.copy());
        }

        return stack;
    }

    /* ------------------------------------------------------------ */
    /* Serialization */
    /* ------------------------------------------------------------ */

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("amount", amount);

        if (fluidTag != null) {
            json.addProperty("tag", fluidTag.location().toString());
        } else if (fluid != null) {
            json.addProperty("fluid",
                    ForgeRegistries.FLUIDS.getKey(fluid).toString());
        }

        if (nbt != null) {
            json.add("nbt", JsonParser.parseString(nbt.toString()));
        }

        return json;
    }

    public static FluidIngredient fromJson(JsonObject obj) {
        int amount = GsonHelper.getAsInt(obj, "amount");

        if (obj.has("fluid")) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.parse(GsonHelper.getAsString(obj, "fluid")));
            CompoundTag nbt = null;
            if (obj.has("nbt")) {
                try {
                    nbt = TagParser.parseTag(obj.get("nbt").toString());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid NBT in fluid ingredient", e);
                }
            }
            return new FluidIngredient(fluid, amount, nbt);
        } else if (obj.has("tag")) {
            TagKey<Fluid> tag = TagKey.create(Registries.FLUID, ResourceLocation.parse(GsonHelper.getAsString(obj, "tag")));
            return new FluidIngredient(tag, amount, null);
        } else throw new IllegalArgumentException("Invalid fluid ingredient JSON: " + obj);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        if (isTagged()) {
            buf.writeBoolean(true); // vanilla fluid tag
            buf.writeResourceLocation(getFluidTag().location());

            buf.writeInt(getAmount());
        } else {
            // plain fluid
            buf.writeBoolean(false);
            buf.writeFluidStack(getAsFluidStack());
        }
    }

    public static FluidIngredient fromNetwork(FriendlyByteBuf buf) {
        if (!buf.readBoolean()) {
            FluidStack stack = buf.readFluidStack();
            return new FluidIngredient(stack.getFluid(), stack.getAmount(), stack.getTag());
        }

        TagKey<Fluid> tag = TagKey.create(Registries.FLUID, buf.readResourceLocation());
        int amount = buf.readInt();
        return new FluidIngredient(tag, amount, null);
    }
}
