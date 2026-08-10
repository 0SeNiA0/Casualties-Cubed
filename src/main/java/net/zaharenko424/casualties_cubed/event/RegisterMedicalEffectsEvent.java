package net.zaharenko424.casualties_cubed.event;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.fluid_system.ExtraMedFluids;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluidType;

import java.util.Map;

public class RegisterMedicalEffectsEvent extends Event {

    private final Map<FluidType, ExtraMedFluids.Data> extraData;

    public RegisterMedicalEffectsEvent(Map<FluidType, ExtraMedFluids.Data> extraData) {
        this.extraData = extraData;
    }

    public void register(FluidType fluid, ExtraMedFluids.Data data) {
        if (fluid instanceof MedicalFluidType) return;

        extraData.put(fluid, data);
    }

    public void register(RegistryObject<? extends FluidType> fluid, ExtraMedFluids.Data data) {
        register(fluid.get(), data);
    }
}
