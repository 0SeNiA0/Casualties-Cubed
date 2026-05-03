package net.adinvas.prototype_pain.tags;

import net.adinvas.prototype_pain.PrototypePain;
import net.adinvas.prototype_pain.fluid_system.MedicalFluid;
import net.adinvas.prototype_pain.registry.ModMedicalRegistry;
import net.minecraft.tags.TagKey;

public class ModMedicalFluidTags {

    public static final TagKey<MedicalFluid> OPIOIDS = create("opioids");

    public static final TagKey<MedicalFluid> DISINFECTING = create("disinfect");

    private static TagKey<MedicalFluid> create(String name) {
        return TagKey.create(ModMedicalRegistry.MEDICAL_FLUIDS_KEY,
                PrototypePain.resourceLoc(name));
    }
}
