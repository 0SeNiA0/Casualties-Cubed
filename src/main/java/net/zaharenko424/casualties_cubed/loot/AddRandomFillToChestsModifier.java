package net.zaharenko424.casualties_cubed.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.zaharenko424.casualties_cubed.registry.ModFluids;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddRandomFillToChestsModifier extends LootModifier {

    public static final Supplier<Codec<AddRandomFillToChestsModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec()
                    .fieldOf("item").forGetter(m -> m.item)).apply(inst, AddRandomFillToChestsModifier::new)));

    private final Item item;

    public AddRandomFillToChestsModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generated, LootContext lootContext) {
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(lootContext)) return generated;
        }

        // Get which loot table triggered this modifier
        ResourceLocation tableId = lootContext.getQueriedLootTableId();
        if (tableId == null) return generated;

        // ✅ Only run for chest loot tables (including modded ones)
        if (!tableId.getPath().startsWith("chests/")) {
            return generated;
        }

        // --- At this point, we know it's a chest loot table ---

        ItemStack stack = new ItemStack(this.item);

        if (stack.getItem() instanceof MultiTankFluidItem vial) {
            RandomSource random = lootContext.getRandom();
            RegistryObject<Fluid>[] allowedFluids = ModFluids.FLUIDS.getEntries().toArray(new RegistryObject[]{});
            float capacity = MultiTankHelper.getCapacity(stack);
            float origCap = capacity;
            do {
                float addamount = random.nextFloat() * 50;
                addamount = Math.min(addamount, capacity);
                addamount = Mth.floor(addamount);

                vial.addFluid(stack, allowedFluids[random.nextInt(allowedFluids.length)], (int) addamount);

                capacity -= addamount;
                if (random.nextBoolean() && capacity / origCap < 0.5) {
                    break;
                }
            } while (capacity > 0);
        }


        generated.add(stack);
        return generated;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
