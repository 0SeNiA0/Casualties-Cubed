package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;
import net.zaharenko424.casualties_cubed.fluid_system.ModFluids;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AntisepticSprayItem extends SprayBottleItem implements IAllowInMedicBags {

    @Override
    public void setupDefault(ItemStack pStack) {
        MultiTankHelper.addMedicalFluid(pStack,
                200,
                ModMedicalFluids.ANTISEPTIC.getId().toString(),
                new FluidStack(ModFluids.SRC_MEDICAL.get().getSource(), 1));
    }
}
