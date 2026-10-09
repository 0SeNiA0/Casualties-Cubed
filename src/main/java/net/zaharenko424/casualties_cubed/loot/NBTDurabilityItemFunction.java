package net.zaharenko424.casualties_cubed.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.registry.LootFunctionRegistry;

public class NBTDurabilityItemFunction extends LootItemConditionalFunction {

    final float dropChance;
    final NumberProvider durabilityProvider;

    NBTDurabilityItemFunction(LootItemCondition[] pConditions, float dropChance, NumberProvider durabilityProvider) {
        super(pConditions);
        this.dropChance = dropChance;
        this.durabilityProvider = durabilityProvider;
    }

    public static LootItemConditionalFunction.Builder<?> durability(float dropChance, NumberProvider provider) {
        return simpleBuilder(a -> new NBTDurabilityItemFunction(a, dropChance, provider));
    }

    @Override
    public LootItemFunctionType getType() {
        return LootFunctionRegistry.NBT_DURABILITY_ITEM_FUNC.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext lootContext) {
        if (!(stack.getItem() instanceof INbtDrivenDurability durability)) return stack;

        if (lootContext.getRandom().nextFloat() > dropChance) return ItemStack.EMPTY;

        durability.setNbtDurability(stack, durabilityProvider.getFloat(lootContext));
        return stack;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<NBTDurabilityItemFunction> {

        public void serialize(JsonObject pJson, NBTDurabilityItemFunction pSetItemCountFunction, JsonSerializationContext pSerializationContext) {
            super.serialize(pJson, pSetItemCountFunction, pSerializationContext);
            pJson.addProperty("dropChance", pSetItemCountFunction.dropChance);
            pJson.add("durability", pSerializationContext.serialize(pSetItemCountFunction.durabilityProvider));
        }

        public NBTDurabilityItemFunction deserialize(JsonObject pObject, JsonDeserializationContext pDeserializationContext, LootItemCondition[] pConditions) {
            NumberProvider numberprovider = GsonHelper.getAsObject(pObject, "durability", pDeserializationContext, NumberProvider.class);
            return new NBTDurabilityItemFunction(pConditions, GsonHelper.getAsFloat(pObject, "dropChance"), numberprovider);
        }
    }
}
