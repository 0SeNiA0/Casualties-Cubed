package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class MedicalFluid extends ForgeFlowingFluid {

    private final MedicalEffect effect;

    public MedicalFluid(Properties properties, MedicalEffect effect) {
        super(properties);
        this.effect = effect;
    }

    public MedicalEffect getEffect() {
        return effect;
    }

    public int getAmount(FluidState state) {
        return 8;
    }

    public boolean isSource(FluidState state) {
        return true;
    }
}
