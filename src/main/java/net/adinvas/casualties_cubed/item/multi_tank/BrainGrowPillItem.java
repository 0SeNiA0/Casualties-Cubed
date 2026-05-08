package net.adinvas.casualties_cubed.item.multi_tank;

import net.adinvas.casualties_cubed.registry.ModMedicalFluids;
import net.adinvas.casualties_cubed.fluid_system.ModFluids;
import net.adinvas.casualties_cubed.fluid_system.MultiTankHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BrainGrowPillItem extends PillContainerItem {

    @Override
    public void setupDefault(ItemStack pStack) {
        MultiTankHelper.addMedicalFluid(pStack,
                50,
                ModMedicalFluids.BRAINGROW.getId().toString(),
                new FluidStack(ModFluids.SRC_MEDICAL.get().getSource(), 1));
    }

    @Override
    public int getCapacity() {
        return 50;
    }
}
