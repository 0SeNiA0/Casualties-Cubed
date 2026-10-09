package net.zaharenko424.casualties_cubed.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.loot.NBTDurabilityItemFunction;
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

        scrap(SCRAP_PILE, 0.5f, UniformGenerator.between(5, 20));
        dropWhenSilkTouch(TRASH_PILE.get());
        scrap(STEEL_TILE, 0.75f, UniformGenerator.between(50, 100));

        plasticChunk(RUBBER);
        plasticChunk(PLASTIC);

        scrap(HEAT_RESISTANT_ALLOY, 0.75f, UniformGenerator.between(50, 100));
        dropSelf(MARBLE.get());
        dropSelf(LIMESTONE.get());
        dropWhenSilkTouch(TOXIROCK.get());
        ore(COPPER, Items.RAW_COPPER);
        ore(ILMENITE, ModItems.ILMENITE_CHUNK.get());
    }

    protected void scrap(RegistryObject<Block> block, float dropChance, NumberProvider durability) {
        Block b = block.get();
        add(b, createSilkTouchDispatchTable(b, applyExplosionDecay(b, LootItem.lootTableItem(ModItems.SCRAP_METAL.get()).apply(NBTDurabilityItemFunction.durability(dropChance, durability)))));
    }

    protected void plasticChunk(RegistryObject<Block> block) {
        Block b = block.get();
        add(b, createSilkTouchDispatchTable(b, applyExplosionDecay(b, LootItem.lootTableItem(ModItems.CHUNK_OF_PLASTIC.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))));
    }

    protected void ore(RegistryObject<Block> block, Item item) {
        Block b = block.get();
        add(b, createSilkTouchDispatchTable(b, applyExplosionDecay(b, LootItem.lootTableItem(item))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
