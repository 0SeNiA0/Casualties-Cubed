package net.zaharenko424.casualties_cubed.tags;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {

    public static final TagKey<Item> ARMOR_FULL_ARM = tag("armorfullarm");
    public static final TagKey<Item> ARMOR_CHEST_ONLY = tag("armorchestonly");
    public static final TagKey<Item> VIAL_ITEMS =tag("vial_items");
    public static final TagKey<Item> DRESSINGS = tag("dressings");
    public static final TagKey<Item> ALCOHOL_CREATABLE = tag("alcohol_create");
    public static final TagKey<Item> CAUTERIZE = tag("medical_cauterize");
    public static final TagKey<Item> AMPUTATE_MINIGAME = tag("amputate_minigame");

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(CasualtiesCubed.resourceLoc(name));
    }
}
