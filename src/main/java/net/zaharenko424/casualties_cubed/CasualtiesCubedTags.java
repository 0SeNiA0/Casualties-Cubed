package net.zaharenko424.casualties_cubed;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.zaharenko424.casualties_cubed.registry.ModMedicalRegistry;

public class CasualtiesCubedTags {

    public static class Item {

        public static final TagKey<net.minecraft.world.item.Item> ARMOR_FULL_ARM = tag("armorfullarm");
        public static final TagKey<net.minecraft.world.item.Item> ARMOR_CHEST_ONLY = tag("armorchestonly");
        public static final TagKey<net.minecraft.world.item.Item> VIAL_ITEMS = tag("vial_items");
        public static final TagKey<net.minecraft.world.item.Item> DRESSINGS = tag("dressings");
        public static final TagKey<net.minecraft.world.item.Item> ALCOHOL_CREATABLE = tag("alcohol_create");
        public static final TagKey<net.minecraft.world.item.Item> CAUTERIZE = tag("medical_cauterize");
        public static final TagKey<net.minecraft.world.item.Item> AMPUTATE_MINIGAME = tag("amputate_minigame");

        private static TagKey<net.minecraft.world.item.Item> tag(String name) {
            return ItemTags.create(CasualtiesCubed.resourceLoc(name));
        }
    }

    public static class MedicalFluid {

        public static final TagKey<net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid> OPIOIDS = tag("opioids");
        public static final TagKey<net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid> DISINFECTING = tag("disinfect");

        private static TagKey<net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid> tag(String name) {
            return TagKey.create(ModMedicalRegistry.MEDICAL_FLUIDS_KEY, CasualtiesCubed.resourceLoc(name));
        }
    }

    public static class DamageType {

        public static final TagKey<net.minecraft.world.damagesource.DamageType> SHRAPNELL = tag("shrapnel");
        public static final TagKey<net.minecraft.world.damagesource.DamageType> MAGIC = tag("magic");
        public static final TagKey<net.minecraft.world.damagesource.DamageType> IGNORE = tag("ignore");
        public static final TagKey<net.minecraft.world.damagesource.DamageType> ABSTRACT_PROJECTILE = tag("abstract_projectile");

        private static TagKey<net.minecraft.world.damagesource.DamageType> tag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, CasualtiesCubed.resourceLoc(name));
        }
    }
}
