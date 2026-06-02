package net.zaharenko424.casualties_cubed.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.CasualtiesCubedTags;
import net.zaharenko424.casualties_cubed.ModDamageTypes;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagProvider extends DamageTypeTagsProvider {

    public DamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookup, CasualtiesCubed.MOD_ID, existingFileHelper);
    }

    private static ResourceLocation createBigCannons(String path) {
        return ResourceLocation.fromNamespaceAndPath("createbigcannons", path);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CasualtiesCubedTags.DamageType.ABSTRACT_PROJECTILE)
                .addOptional(createBigCannons("shrapnel"))
                .addOptional(createBigCannons("big_cannon_projectile"))
                .addOptional(createBigCannons("cannon_projectile"))
                .addOptional(createBigCannons("grapeshot"))
                .addOptional(createBigCannons("machine_gun_fire"))
                .addOptional(createBigCannons("machine_gun_fire_in_water"))
                .addOptional(createBigCannons("traffic_cone"));

        tag(CasualtiesCubedTags.DamageType.IGNORE)
                .add(ModDamageTypes.BLEED, ModDamageTypes.HEAVY_BLEED, ModDamageTypes.INTERNAL_BLEED, ModDamageTypes.OPIOIDS,
                        ModDamageTypes.OXYGEN, ModDamageTypes.GIVE_UP);

        tag(CasualtiesCubedTags.DamageType.MAGIC)
                .add(DamageTypes.INDIRECT_MAGIC, DamageTypes.MAGIC);

        tag(CasualtiesCubedTags.DamageType.SHRAPNELL)
                .addOptional(createBigCannons("shrapnel"))
                .addOptional(createBigCannons("flak"))
                .addOptional(createBigCannons("grapeshot"))
                .addOptional(createBigCannons("traffic_cone"));
    }
}
