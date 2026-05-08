package net.adinvas.casualties_cubed.client.gui;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum StatusSprites {

    BLEED(CasualtiesCubed.resourceLoc("textures/gui/icons/blood.png")),
    INFECTION(CasualtiesCubed.resourceLoc("textures/gui/icons/infection.png"), 1.3f),
    FRACTURE(CasualtiesCubed.resourceLoc("textures/gui/icons/fracture.png"), 1.5f),
    DISLOCATION(CasualtiesCubed.resourceLoc("textures/gui/icons/dislocation.png"), 1.5f, Component.translatable("casualties_cubed.gui.dislocation_button")),
    SHRAPNEL(CasualtiesCubed.resourceLoc("textures/gui/icons/shrapnel.png"), 1.2f, Component.translatable("casualties_cubed.gui.shrapnel_button")),
    SPLINT(CasualtiesCubed.resourceLoc("textures/gui/icons/splint.png"), 1.6f, Component.translatable("casualties_cubed.gui.splint_button")),
    DISINFECTION(CasualtiesCubed.resourceLoc("textures/gui/icons/disinfection.png"), 1.5f),
    TOURNIQUET(CasualtiesCubed.resourceLoc("textures/gui/icons/tourniquet.png"), 2, Component.translatable("casualties_cubed.gui.tourniquet_button"));

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
