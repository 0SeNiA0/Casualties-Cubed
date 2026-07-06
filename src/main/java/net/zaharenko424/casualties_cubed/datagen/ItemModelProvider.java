package net.zaharenko424.casualties_cubed.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static net.zaharenko424.casualties_cubed.registry.ModItems.*;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CasualtiesCubed.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ALCOHOL_BOTTLE);
        basicItem(ALGINATE_DRESSING);
        basicItem(ANTIBIOTICS_PILLS);
        basicItem(ANTISEPTIC_SPRAY);
        basicItem(AUTO_PUMP);
        basicItem(CHEST_DRAIN);

        withTintLayer(AUTO_INJECTOR, AUTO_INJECTOR.getId().withPrefix("item/"), AUTO_INJECTOR.getId().withPrefix("item/").withSuffix("_fill"));

        withExistingParent(ANTISERUM_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        basicItem(ADHESIVE_BANDAGE);
        withExistingParent(PROCOAGULANT_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        basicItem(WOUND_GLUE_SPRAY);
        withExistingParent(STREPTOKINASE_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        basicItem(BONE_WELDER);

        basicItem(CANTEEN);

        withTintLayer(WATER_BOTTLE);
        withTintLayer(WATER_JUG);

        basicItem(BRAIN_GROW_PILLS);
        basicItem(BROWN_CAP_MUSH);
        basicItem(BRUISE_KIT);
        basicItem(DRESSING);
        basicItem(EXPERIMENTAL_TREATMENT);
        basicItem(GLOW_FRUIT);
        basicItem(HEAT_PACK);
        basicItem(ICE_PACK);
        basicItem(LRD);
        basicItem(MAKESHIFT_LRD);
        basicItem(HEROIN_SYRINGE);
        basicItem(MEDICAL_GAUZE);
        basicItem(MEDICAL_SUTURE);

        withTintLayer(MEDICINE_VIAL, CasualtiesCubed.resourceLoc("item/empty_vial"), CasualtiesCubed.resourceLoc("item/vial_color"));

        withExistingParent(CEFTRIAXONE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        basicItem(BLEACH_JUG);
        withExistingParent(FENTANYL_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(MORPHINE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(NALOXONE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(OPIUM_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        basicItem(OLD_RAG);
        basicItem(PAINKILLERS_PILLS);

        withTintLayer(PILL_BOTTLE, CasualtiesCubed.resourceLoc("item/generic_pill0"), CasualtiesCubed.resourceLoc("item/generic_pill1"));

        basicItem(PLASTIC_DRESSING);
        basicItem(RELIEF_CREAM_BOTTLE);
        basicItem(RIPPED_DRESSING);
        basicItem(IV_BAG);

        withTintLayer(BLOOD_BAG, BLOOD_BAG.getId().withPrefix("item/"), BLOOD_BAG.getId().withPrefix("item/").withSuffix("_fill"));

        basicItem(SPLINT);
        basicItem(STERILIZED_DRESSING);
        basicItem(SYRINGE);
        basicItem(THERMOMETER);
        basicItem(TOURNIQUET);
        basicItem(TWEEZERS);
    }

    protected void basicItem(RegistryObject<? extends Item> item) {
        basicItem(item.getId());
    }

    protected void withTintLayer(RegistryObject<? extends Item> item) {
        ResourceLocation itemLoc = item.getId().withPrefix(ITEM_FOLDER + "/");
        withTintLayer(item, itemLoc.withSuffix("0"), itemLoc.withSuffix("1"));
    }

    protected void withTintLayer(RegistryObject<? extends Item> item, ResourceLocation base, ResourceLocation tint) {
        getBuilder(item.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", base)
                .texture("layer1", tint);
    }
}
