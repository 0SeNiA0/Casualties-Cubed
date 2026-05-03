package net.adinvas.prototype_pain.client.moodles;

import net.adinvas.prototype_pain.PrototypePain;
import net.minecraft.resources.ResourceLocation;

public enum MoodleStatus {
    NONE(PrototypePain.resourceLoc("textures/gui/moodles/moodle_light.png")),
    LIGHT(PrototypePain.resourceLoc("textures/gui/moodles/moodle_light.png")),
    NORMAL(PrototypePain.resourceLoc("textures/gui/moodles/moodle_normal.png")),
    HEAVY(PrototypePain.resourceLoc("textures/gui/moodles/moodle_heavy.png")),
    CRITICAL(PrototypePain.resourceLoc("textures/gui/moodles/moodle_critical.png"));

    public final ResourceLocation tex;
    
    MoodleStatus(ResourceLocation tex) {
        this.tex = tex;
    }
}
