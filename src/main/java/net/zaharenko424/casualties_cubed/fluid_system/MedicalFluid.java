package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class MedicalFluid extends ForgeFlowingFluid {

    public MedicalFluid(Properties properties) {
        super(properties);
    }

    public int getAmount(FluidState state) {
        return 8;
    }

    public boolean isSource(FluidState state) {
        return true;
    }
}
