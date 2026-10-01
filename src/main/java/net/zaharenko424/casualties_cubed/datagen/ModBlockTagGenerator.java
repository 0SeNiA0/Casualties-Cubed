package net.zaharenko424.casualties_cubed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.registry.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.zaharenko424.casualties_cubed.registry.ModBlocks.*;

public class ModBlockTagGenerator extends BlockTagsProvider {

    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CasualtiesCubed.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CasualtiesCubedTags.Block.SLEEP_GOOD).addTags(BlockTags.BEDS, BlockTags.WOOL, BlockTags.SAND)
                .add(ModBlocks.RUBBER.get());
        tag(CasualtiesCubedTags.Block.SLEEP_OKAY).addTags(BlockTags.WOOL_CARPETS, Tags.Blocks.GRAVEL, BlockTags.PLANKS, BlockTags.LEAVES)
                .add(ModBlocks.PLASTIC.get(), Blocks.CLAY, Blocks.AIR);
        tag(CasualtiesCubedTags.Block.SLEEP_MEDIOCRE).addTags(BlockTags.LOGS, Tags.Blocks.GLASS)
                .add(ModBlocks.SCRAP_PILE.get(), ModBlocks.TRASH_PILE.get(), STEEL_TILE.get(),
                        ModBlocks.HEAT_RESISTANT_ALLOY.get(), Blocks.MUSHROOM_STEM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK);

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(STEEL_TILE.get(), HEAT_RESISTANT_ALLOY.get(), MARBLE.get(), LIMESTONE.get(),
                TOXIROCK.get(), COPPER.get(), ILMENITE.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(SCRAP_PILE.get(), TRASH_PILE.get());
    }
}
