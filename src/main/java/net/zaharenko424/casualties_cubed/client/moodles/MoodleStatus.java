package net.zaharenko424.casualties_cubed.client.moodles;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.minecraft.resources.ResourceLocation;

public enum MoodleStatus {
    HIGH_POS(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_high_pos.png"), true),
    NORMAL_POS(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_normal_pos.png"), true),
    LIGHT_POS(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_light_pos.png"), true),
    NONE(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_light.png")),
    LIGHT_NEG(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_light.png")),
    NORMAL_NEG(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_normal.png")),
    HEAVY_NEG(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_heavy.png")),
    CRITICAL_NEG(CasualtiesCubed.resourceLoc("textures/gui/moodles/moodle_critical.png"));

    public final ResourceLocation tex;
    public final boolean positive;
    
    MoodleStatus(ResourceLocation tex) {
        this(tex, false);
    }

    MoodleStatus(ResourceLocation tex, boolean positive) {
        this.tex = tex;
        this.positive = positive;
    }
}
