package net.adinvas.casualties_cubed.datagen;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.registry.ModMedicalFluids;
import net.adinvas.casualties_cubed.registry.ModMedicalRegistry;
import net.adinvas.casualties_cubed.fluid_system.MedicalFluid;
import net.adinvas.casualties_cubed.tags.ModMedicalFluidTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModMedicalFluidTagProvider extends TagsProvider<MedicalFluid> {

    public ModMedicalFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<MedicalFluid>> parentProvider) {
        super(output, ModMedicalRegistry.MEDICAL_FLUIDS_KEY, lookupProvider, parentProvider, CasualtiesCubed.MOD_ID, null);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(ModMedicalFluidTags.OPIOIDS)
                .add(ModMedicalFluids.OPIUM.getKey())
                .add(ModMedicalFluids.FENTANYL.getKey())
                .add(ModMedicalFluids.HEROIN.getKey())
                .add(ModMedicalFluids.MORPHINE.getKey())
                .add(ModMedicalFluids.PAINKILLERS.getKey())
                .add(ModMedicalFluids.RELIEF_CREAM.getKey());

        tag(ModMedicalFluidTags.DISINFECTING)
                .add(ModMedicalFluids.ALCOHOL.getKey())
                .add(ModMedicalFluids.ANTISEPTIC.getKey());
    }
}
