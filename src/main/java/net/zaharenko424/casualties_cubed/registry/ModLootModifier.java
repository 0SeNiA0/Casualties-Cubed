package net.zaharenko424.casualties_cubed.registry;

import com.mojang.serialization.Codec;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.loot.AddFilledToChestsModifier;
import net.zaharenko424.casualties_cubed.loot.AddRandomFillToChestsModifier;

public class ModLootModifier {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", AddFilledToChestsModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_VIAL_RANDOM_FILL =
            LOOT_MODIFIER_SERIALIZERS.register("add_vial_random_fill", AddRandomFillToChestsModifier.CODEC);

    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}
