package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;

public class FentanylVialItem extends MedicineVialItem {

    @Override
    public ItemStack withDefFluid() {
        ItemStack stack = withMedicalFluid(ModMedicalFluids.FENTANYL, 10);
        addMedicalFluid(stack, ModMedicalFluids.CLEAN_WATER, 90);
        return stack;
    }
}
