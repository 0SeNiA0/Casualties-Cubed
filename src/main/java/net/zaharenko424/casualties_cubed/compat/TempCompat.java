package net.zaharenko424.casualties_cubed.compat;

import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.floats.FloatObjectPair;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempCompat {

    public record BiomeTemperatureEntry(float temperature, float nightChange, float spring, float summer, float fall, float winter) {}

    private static final Map<ResourceLocation, BiomeTemperatureEntry> BIOME_TEMPERATURE_BY_ID = new HashMap<>();
    private static final Map<Block, Either<Float, List<FloatObjectPair<StatePropertiesPredicate>>>> BLOCK_TEMPERATURE_DATA = new HashMap<>();

    public static void clearBlock() {
        BLOCK_TEMPERATURE_DATA.clear();
    }

    public static void clearBiome() {
        BIOME_TEMPERATURE_BY_ID.clear();
    }

    public static void addEntryBlock(ResourceLocation id, Either<@NotNull Float, List<FloatObjectPair<StatePropertiesPredicate>>> value) {
        Block block = ForgeRegistries.BLOCKS.getValue(id);
        if (block != null) BLOCK_TEMPERATURE_DATA.put(block, value);
    }

    public static void addEntryBiome(ResourceLocation id, BiomeTemperatureEntry value) {
        if (id != null) BIOME_TEMPERATURE_BY_ID.put(id, value);
    }

    public static float get(BlockState state) {
        Either<Float, List<FloatObjectPair<StatePropertiesPredicate>>> entry = BLOCK_TEMPERATURE_DATA.get(state.getBlock());
        if (entry == null) {
            return state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? 100 : 0;
        }

        if (entry.left().isPresent()) return entry.left().get();

        List<FloatObjectPair<StatePropertiesPredicate>> predicates = entry.right().get();
        for (FloatObjectPair<StatePropertiesPredicate> pair : predicates) {
            if (pair.second().matches(state)) return pair.firstFloat();
        }

        return 0;
    }

    public static BiomeTemperatureEntry getForPosition(Level level, BlockPos pos) {
        return level.getBiome(pos).unwrapKey() // Optional<ResourceKey<Biome>>
                .map(key -> BIOME_TEMPERATURE_BY_ID.get(key.location()))
                .orElse(null);
    }
}
