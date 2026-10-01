package net.zaharenko424.casualties_cubed.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.loot.NBTDurabilityItemFunction;

public class LootFunctionRegistry {

    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<LootItemFunctionType> NBT_DURABILITY_ITEM_FUNC = LOOT_FUNCTIONS.register("nbt_durability_item", () -> new LootItemFunctionType(new NBTDurabilityItemFunction.Serializer()));
}
