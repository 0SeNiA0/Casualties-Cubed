package net.adinvas.casualties_cubed.client.overlays.exp;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public interface IShaderOverlay {

    boolean shouldRender();

    void render(RenderLevelStageEvent event, RenderTarget input, RenderTarget output);
}
