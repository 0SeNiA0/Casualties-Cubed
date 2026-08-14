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
        return "sounds." + CasualtiesCubed.MOD_ID + "." + str;
    }
}
