package net.adinvas.prototype_pain.datagen.worldgen;

import net.adinvas.prototype_pain.PrototypePain;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {

    static final ResourceKey<PlacedFeature> GLOW_FRUIT_PATCH =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    PrototypePain.resourceLoc("glow_fruit_patch"));

    static final ResourceKey<PlacedFeature> GLOW_FRUIT_PATCH_SWAMP =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    PrototypePain.resourceLoc("glow_fruit_patch_swamp"));

    static final ResourceKey<PlacedFeature> BROWN_CAP =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    PrototypePain.resourceLoc("brown_cap_patch"));

    private static final List<Block> STONE = List.of(
            Blocks.STONE,
            Blocks.DEEPSLATE,
            Blocks.DIRT,
            Blocks.ANDESITE,
            Blocks.GRANITE,
            Blocks.DIORITE
    );

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(GLOW_FRUIT_PATCH, new PlacedFeature(
                configured.getOrThrow(ModConfiguredFeatures.GLOW_FRUIT_CONFIG),
                List.of(
                        RarityFilter.onAverageOnceEvery(4), // much more common underground
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(40)), // caves
                        BlockPredicateFilter.forPredicate(
                                BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), STONE) // must be on stone
                        )
                )
        ));
        context.register(GLOW_FRUIT_PATCH_SWAMP, new PlacedFeature(
                configured.getOrThrow(ModConfiguredFeatures.SWAMP_GLOW_FRUIT_CONFIG),
                List.of(
                        RarityFilter.onAverageOnceEvery(24), // rarer
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(90)),
                        BiomeFilter.biome()
                )
        ));
        context.register(BROWN_CAP, new PlacedFeature(
                configured.getOrThrow(ModConfiguredFeatures.BROWN_CAP),
                List.of(
                        RarityFilter.onAverageOnceEvery(24), // rarer
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(90)),
                        BiomeFilter.biome()
                )
        ));
    }
}
