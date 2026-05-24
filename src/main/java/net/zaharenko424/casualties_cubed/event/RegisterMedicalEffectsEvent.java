package net.zaharenko424.casualties_cubed.event;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.fluid_system.ExtraMedFluids;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid;

import java.util.Map;

public class RegisterMedicalEffectsEvent extends Event {

    private final Map<Fluid, ExtraMedFluids.Data> extraData;

    public RegisterMedicalEffectsEvent(Map<Fluid, ExtraMedFluids.Data> extraData) {
        this.extraData = extraData;
    }

    public void register(Fluid fluid, ExtraMedFluids.Data data) {
        if (fluid instanceof MedicalFluid) return;

        extraData.put(fluid, data);
    }

    public void register(RegistryObject<? extends Fluid> fluid, ExtraMedFluids.Data data) {
        register(fluid.get(), data);
    }
}
