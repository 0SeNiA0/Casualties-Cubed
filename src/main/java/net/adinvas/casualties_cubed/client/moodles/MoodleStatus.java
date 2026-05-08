package net.adinvas.casualties_cubed.client.moodles;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.minecraft.resources.ResourceLocation;

public enum MoodleStatus {
    NONE(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_light.png")),
    LIGHT(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_light.png")),
    NORMAL(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_normal.png")),
    HEAVY(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_heavy.png")),
    CRITICAL(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_critical.png"));

    public final ResourceLocation tex;
    
    MoodleStatus(ResourceLocation tex) {
        this.tex = tex;
    }
}
