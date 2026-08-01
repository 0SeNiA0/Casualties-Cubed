package net.zaharenko424.casualties_cubed.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.registry.ModBlocks;
import net.zaharenko424.casualties_cubed.registry.ModItems;

import java.util.Set;

import static net.zaharenko424.casualties_cubed.registry.ModBlocks.*;

public class ModBlockLootTables extends BlockLootSubProvider {

    protected ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropOther(BROWN_CAP.get(), ModItems.BROWN_CAP.get());
        this.dropSelf(EXPIE_PLUSHY.get());
        this.dropSelf(MEDICAL_MIXER.get());

        add(GLOW_FRUIT_BUSH.get(), applyExplosionDecay(GLOW_FRUIT_BUSH.get(), LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GLOW_FRUIT_BUSH.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 3)))
                                .add(LootItem.lootTableItem(ModItems.GLOW_FRUIT.get()))
                )
        ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
