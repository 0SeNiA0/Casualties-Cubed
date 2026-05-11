package net.zaharenko424.casualties_cubed.datagen;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.tags.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, CasualtiesCubed.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(ModItemTags.DRESSINGS)
                .add(ModItems.DRESSING.get(),
                    ModItems.PLASTIC_DRESSING.get(),
                    ModItems.STERILIZED_DRESSING.get(),
                    ModItems.MEDICAL_GAUZE.get(),
                    ModItems.ALGANATE_DRESSING.get());

        this.tag(ModItemTags.VIAL_ITEMS)
                .add(ModItems.MEDICINE_VIAL.get(),
                        ModItems.BOTTLE.get(),
                        ModItems.PILL_BOTTLE.get(),
                        ModItems.AUTO_INJECTOR.get(),
                        ModItems.ANTISERUM_INJECTOR.get(),
                        ModItems.STREPTOKINASE_INJECTOR.get(),
                        ModItems.PROCOAGULANT_INJECTOR.get(),
                        ModItems.REACTION_LIQUID_VIAL.get(),
                        ModItems.OPIUM_VIAL.get(),
                        ModItems.NALOXONE_VIAL.get(),
                        ModItems.MORPHINE_VIAL.get(),
                        ModItems.FENTANYL_VIAL.get(),
                        ModItems.CEFTRIAXONE_VIAL.get()
                );

        this.tag(ModItemTags.ALCOHOL_CREATABLE)
                .add(
                        Items.APPLE,
                        Items.POTATO,
                        Items.POISONOUS_POTATO,
                        Items.BROWN_MUSHROOM,
                        Items.RED_MUSHROOM,
                        Items.GLOW_BERRIES,
                        Items.SWEET_BERRIES
                );

        this.tag(ModItemTags.CAUTERIZE)
                .add(
                        Items.TORCH,
                        Items.SOUL_TORCH,
                        Items.MAGMA_BLOCK,
                        Items.LAVA_BUCKET
                );

    }
}
