package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.zaharenko424.casualties_cubed.registry.ModFluids.*;

public class FluidTagProvider extends TagsProvider<Fluid> {

    public FluidTagProvider(PackOutput p_256596_, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256596_, Registries.FLUID, p_256513_, CasualtiesCubed.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CasualtiesCubedTags.Fluid.OPIOIDS)
                .add(new ResourceKey[]{OPIUM.getKey(), MORPHINE.getKey(), FENTANYL.getKey(), HEROIN.getKey(),
                        PAINKILLERS.getKey(), RELIEF_CREAM.getKey()});

        tag(CasualtiesCubedTags.Fluid.DISINFECTING)
                .add(new ResourceKey[]{ALCOHOL.getKey(), ANTISEPTIC.getKey()});
    }
}
