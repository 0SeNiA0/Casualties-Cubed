package net.zaharenko424.casualties_cubed.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;

import java.util.function.Function;

import static net.zaharenko424.casualties_cubed.registry.ModSounds.*;

public class SoundProvider extends SoundDefinitionsProvider {

    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    protected SoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, CasualtiesCubed.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        addSimpleSound(HEALTH_SCREEN_OPEN);
        addSimpleSound(HEALTH_SCREEN_CLOSE);
        addSimpleSound(HEART_THUMP);
        addSimpleSound(HEART_THUMP_HEAVY);
        addSimpleSound(HEART_THUMP_HEAVY_MONITOR);
        addSimpleSound(SPRAY);
        addSimpleSound(SPLINT);
        addSimpleSound(AUTO_PUMP);
        addSimpleSound(BONE_WELD);
        addSimpleSound(DRAIN_USE);
        addSimpleSound(LEVEL_UP);
        addSimpleSound(CLICK);
        addSimpleSound(SMALL_CLICK);
        addSimpleSound(VOMIT_WARNING);
        addSimpleSound(BLOOD_VOMIT_WARNING);
        addSound(VOMIT, "vomit_1", "vomit_2");

        addSimpleSound(SCRAP_PILE_HIT);
        addSound(SCRAP_PILE_STEP, "scrap_pile/step_1", "scrap_pile/step_2", "scrap_pile/step_3", "scrap_pile/step_4",
                "scrap_pile/step_5", "scrap_pile/step_6", "scrap_pile/step_7", "scrap_pile/step_8", "scrap_pile/step_9");
        addSimpleSound(TRASH_PILE_HIT);
        addSimpleSound(STEEL_HIT);
        addSound(STEEL_STEP, "steel/step_1", "steel/step_2", "steel/step_3", "steel/step_4", "steel/step_5", "steel/step_6",
                "steel/step_7", "steel/step_8", "steel/step_9", "steel/step_10");
        addSimpleSound(RUBBER_HIT);
        addSound(RUBBER_STEP, "rubber/step_1", "rubber/step_2", "rubber/step_3", "rubber/step_4", "rubber/step_5", "rubber/step_6",
                "rubber/step_7", "rubber/step_8", "rubber/step_9", "rubber/step_10");
        addSound(PLASTIC_STEP, "plastic/step_1", "plastic/step_2", "plastic/step_3", "plastic/step_4", "plastic/step_5", "plastic/step_6");
        addSimpleSound(ROCK_HIT);
        addSound(ROCK_STEP, "rock/step_1", "rock/step_2", "rock/step_3", "rock/step_4", "rock/step_5", "rock/step_6",
                "rock/step_7", "rock/step_8", "rock/step_9", "rock/step_10", "rock/step_11", "rock/step_12", "rock/step_13",
                "rock/step_14");
        addSimpleSound(CRYSTAL_HIT);
        addSound(CONCRETE_STEP, "concrete/step_1", "concrete/step_2", "concrete/step_3", "concrete/step_4", "concrete/step_5",
                "concrete/step_6", "concrete/step_7", "concrete/step_8", "concrete/step_9", "concrete/step_10", "concrete/step_11",
                "concrete/step_12", "concrete/step_13", "concrete/step_14");

        addSimpleSound(BANDAGE_USE);
        addSimpleSound(SYRINGE_USE);
        addSimpleSound(SYRINGE_LOOP);
        addSimpleSound(PILLS);
        addSimpleSound(PAINDRONE, "broken", SoundDefinition.Sound::stream);
        addSimpleSound(RINGING, "tinnitus");
        addSound(BROKEN_BONE, "bone-break1", "bone-break2", "bone-break3");
        addSimpleSound(AMPUTATION);
        addSimpleSound(LAST_STAND);
    }

    private void addSimpleSound(RegistryObject<SoundEvent> sound) {
        addSimpleSound(sound, sound.getId().getPath(), def -> def);
    }

    private void addSimpleSound(RegistryObject<SoundEvent> sound, String filename) {
        addSimpleSound(sound, filename, def -> def);
    }

    private void addSimpleSound(RegistryObject<SoundEvent> sound, String filename, Function<SoundDefinition.Sound, SoundDefinition.Sound> config) {
        ResourceLocation id = sound.getId();
        add(sound, definition()
                .subtitle(subtitle(id.getPath()))
                .with(sound(CasualtiesCubed.resourceLoc(filename))));
    }

    private void addSound(RegistryObject<SoundEvent> sound, String... sounds){
        ResourceLocation id = sound.getId();
        SoundDefinition def = definition().subtitle(subtitle(id.getPath()));

        for (String str : sounds) {
            def.with(sound(CasualtiesCubed.resourceLoc(str)));
        }

        add(sound, def);
    }

    private String subtitle(String str){
        return "subtitles." + CasualtiesCubed.MOD_ID + "." + str;
    }
}
