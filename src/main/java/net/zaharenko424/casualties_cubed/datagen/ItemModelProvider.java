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
        basicItem(ALCOHOL_BOTTLE.get());
        basicItem(ALGINATE_DRESSING.get());
        basicItem(ANTIBIOTICS_PILLS.get());
        basicItem(ANTISEPTIC_SPRAY.get());
        basicItem(AUTO_PUMP.get());

        withTintLayer(AUTO_INJECTOR, AUTO_INJECTOR.getId().withPrefix("item/"), AUTO_INJECTOR.getId().withPrefix("item/").withSuffix("_fill"));

        withExistingParent(ANTISERUM_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        basicItem(ADHESIVE_BANDAGE.get());
        withExistingParent(PROCOAGULANT_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        basicItem(WOUND_GLUE_SPRAY.get());
        withExistingParent(STREPTOKINASE_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        basicItem(BONE_WELDER.get());

        basicItem(CANTEEN.get());

        withTintLayer(WATER_BOTTLE);
        withTintLayer(WATER_JUG);

        basicItem(BRAIN_GROW_PILLS.get());
        basicItem(BROWN_CAP_MUSH.get());
        basicItem(BRUISE_KIT.get());
        basicItem(DRESSING.get());
        basicItem(EXPERIMENTAL_TREATMENT.get());
        basicItem(GLOW_FRUIT.get());
        basicItem(HEAT_PACK.get());
        basicItem(ICE_PACK.get());
        basicItem(LRD.get());
        basicItem(MAKESHIFT_LRD.get());
        basicItem(HEROIN_SYRINGE.get());
        basicItem(MEDICAL_GAUZE.get());
        basicItem(MEDICAL_SUTURE.get());

        withTintLayer(MEDICINE_VIAL, CasualtiesCubed.resourceLoc("item/empty_vial"), CasualtiesCubed.resourceLoc("item/vial_color"));

        withExistingParent(CEFTRIAXONE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        basicItem(BLEACH_JUG.get());
        withExistingParent(FENTANYL_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(MORPHINE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(NALOXONE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(OPIUM_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        basicItem(OLD_RAG.get());
        basicItem(PAINKILLERS_PILLS.get());

        withTintLayer(PILL_BOTTLE, CasualtiesCubed.resourceLoc("item/generic_pill0"), CasualtiesCubed.resourceLoc("item/generic_pill1"));

        basicItem(PLASTIC_DRESSING.get());
        basicItem(RELIEF_CREAM_BOTTLE.get());
        basicItem(RIPPED_DRESSING.get());
        basicItem(IV_BAG.get());
        basicItem(SPLINT.get());
        basicItem(STERILIZED_DRESSING.get());
        basicItem(SYRINGE.get());
        basicItem(THERMOMETER.get());
        basicItem(TOURNIQUET.get());
        basicItem(TWEEZERS.get());
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
