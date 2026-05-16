package net.zaharenko424.casualties_cubed.datagen;

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

        getBuilder(AUTO_INJECTOR.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", AUTO_INJECTOR.getId().withPrefix("item/"))
                .texture("layer1", AUTO_INJECTOR.getId().withPrefix("item/").withSuffix("_fill"));

        withExistingParent(ANTISERUM_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        basicItem(ADHESIVE_BANDAGE.get());
        withExistingParent(PROCOAGULANT_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        withExistingParent(STREPTOKINASE_INJECTOR.getId().toString(), AUTO_INJECTOR.getId());
        basicItem(BONE_WELDER.get());

        getBuilder(BOTTLE.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", BOTTLE.getId().withPrefix("item/").withSuffix("0"))
                .texture("layer1", BOTTLE.getId().withPrefix("item/").withSuffix("1"));

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

        getBuilder(MEDICINE_VIAL.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", CasualtiesCubed.resourceLoc("item/empty_bottle"))
                .texture("layer1", CasualtiesCubed.resourceLoc("item/vial_color"));

        withExistingParent(CEFTRIAXONE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(FENTANYL_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(MORPHINE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(NALOXONE_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        withExistingParent(OPIUM_VIAL.getId().toString(), MEDICINE_VIAL.getId());
        basicItem(OLD_RAG.get());
        basicItem(PAINKILLERS_PILLS.get());

        getBuilder(PILL_BOTTLE.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", CasualtiesCubed.resourceLoc("item/generic_pill0"))
                .texture("layer1", CasualtiesCubed.resourceLoc("item/generic_pill1"));

        basicItem(PLASTIC_DRESSING.get());
        basicItem(RELIEF_CREAM_BOTTLE.get());
        basicItem(RIPPED_DRESSING.get());
        basicItem(SALINE_SYRINGE.get());
        basicItem(SPLINT.get());
        basicItem(STERILIZED_DRESSING.get());
        basicItem(SYRINGE.get());
        basicItem(THERMOMETER.get());
        basicItem(TOURNIQUET.get());
        basicItem(TWEEZERS.get());
    }
}
