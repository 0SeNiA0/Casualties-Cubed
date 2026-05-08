package net.adinvas.casualties_cubed.visual;

import net.minecraft.client.player.LocalPlayer;

public interface IModelPartHider {
    void hidePart(LocalPlayer player,String partname);
    void showPart(LocalPlayer player,String partname);
}
