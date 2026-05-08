package net.adinvas.casualties_cubed.tags;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.fluid_system.MedicalFluid;
import net.adinvas.casualties_cubed.registry.ModMedicalRegistry;
import net.minecraft.tags.TagKey;

public class ModMedicalFluidTags {

    public static final TagKey<MedicalFluid> OPIOIDS = create("opioids");

    public static final TagKey<MedicalFluid> DISINFECTING = create("disinfect");

    private static TagKey<MedicalFluid> create(String name) {
        return TagKey.create(ModMedicalRegistry.MEDICAL_FLUIDS_KEY,
                CasualtiesCubed.resourceLoc(name));
    }
}
