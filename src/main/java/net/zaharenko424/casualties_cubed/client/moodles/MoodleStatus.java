package net.zaharenko424.casualties_cubed.client.moodles;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.minecraft.resources.ResourceLocation;

public enum MoodleStatus {
    LIGHT_NEG(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_light.png")),
    NORMAL_NEG(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_normal.png")),
    HEAVY_NEG(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_heavy.png")),
    CRITICAL_NEG(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_critical.png")),
    LIGHT_POS(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_light_pos.png")),
    NORMAL_POS(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_normal_pos.png")),
    HIGH_POS(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_high_pos.png"));

    public final ResourceLocation tex;
    
    MoodleStatus(ResourceLocation tex) {
        this.tex = tex;
    }
}
