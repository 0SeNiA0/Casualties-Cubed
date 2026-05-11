package net.zaharenko424.casualties_cubed.datagen;


import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.datagen.worldgen.ModWorldGenProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CasualtiesCubed.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenretors {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModWorldGenProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModGlobaLootModifiersProvider(output));

        generator.addProvider(event.includeServer(), ModLootTableProvider.create(output));
        ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new ModBlockTagGenerator(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModItemTagProvider(output, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

        if (event.includeServer()) {
            event.getGenerator().addProvider(
                    true,
                    new ModMedicalFluidTagProvider(
                            output,
                            lookupProvider,
                            CompletableFuture.completedFuture(TagsProvider.TagLookup.empty())
                    )
            );
        }

        generator.addProvider(event.includeServer(), new ModRecipeProvider(output));

        generator.addProvider(event.includeClient(), new BlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ItemModelProvider(output, existingFileHelper));
    }
}
