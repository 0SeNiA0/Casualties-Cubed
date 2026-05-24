package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class FentanylVialItem extends MedicineVialItem {

    @Override
    public ItemStack withDefFluid() {
        ItemStack stack = withFluid(ModFluids.FENTANYL, 10);
        addFluid(stack, ModFluids.CLEAN_WATER, 90);
        return stack;
    }
}
