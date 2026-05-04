package net.adinvas.prototype_pain.client.gui;

import net.adinvas.prototype_pain.PrototypePain;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum StatusSprites {

    BLEED(PrototypePain.resourceLoc("textures/gui/icons/blood.png")),
    INFECTION(PrototypePain.resourceLoc("textures/gui/icons/infection.png"), 1.3f),
    FRACTURE(PrototypePain.resourceLoc("textures/gui/icons/fracture.png"), 1.5f),
    DISLOCATION(PrototypePain.resourceLoc("textures/gui/icons/dislocation.png"), 1.5f, Component.translatable("prototype_pain.gui.dislocation_button")),
    SHRAPNEL(PrototypePain.resourceLoc("textures/gui/icons/shrapnel.png"), 1.2f, Component.translatable("prototype_pain.gui.shrapnel_button")),
    SPLINT(PrototypePain.resourceLoc("textures/gui/icons/splint.png"), 1.6f, Component.translatable("prototype_pain.gui.splint_button")),
    DISINFECTION(PrototypePain.resourceLoc("textures/gui/icons/disinfection.png"), 1.5f),
    TOURNIQUET(PrototypePain.resourceLoc("textures/gui/icons/tourniquet.png"), 2, Component.translatable("prototype_pain.gui.tourniquet_button"));

    public final ResourceLocation tex;
    public final float scale;
    public final Component comp;

    StatusSprites(ResourceLocation tex) {
        this(tex, 1, Component.empty());
    }

    StatusSprites(ResourceLocation tex, float scale) {
        this(tex, scale, Component.empty());
    }

    StatusSprites(ResourceLocation tex, float scale, Component comp) {
        this.tex = tex;
        this.scale = scale;
        this.comp = comp;
    }
}
