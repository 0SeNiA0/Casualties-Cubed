package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;
import net.zaharenko424.casualties_cubed.fluid_system.ModFluids;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MorphineVialItem extends MedicineVialItem {

    @Override
    public void setupDefault(ItemStack pStack) {
        MultiTankHelper.addMedicalFluid(pStack,
                100,
                ModMedicalFluids.MORPHINE.getId().toString(),
                new FluidStack(ModFluids.SRC_MEDICAL.get().getSource(), 1));
    }
}
