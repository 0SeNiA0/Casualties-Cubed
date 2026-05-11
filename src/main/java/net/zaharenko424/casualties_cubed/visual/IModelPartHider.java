package net.zaharenko424.casualties_cubed.visual;

import net.minecraft.client.player.LocalPlayer;

public interface IModelPartHider {
    void hidePart(LocalPlayer player,String partname);
    void showPart(LocalPlayer player,String partname);
}
