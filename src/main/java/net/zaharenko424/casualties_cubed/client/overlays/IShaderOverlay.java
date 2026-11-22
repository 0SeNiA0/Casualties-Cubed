package net.zaharenko424.casualties_cubed.client.overlays;

import com.mojang.blaze3d.pipeline.RenderTarget;

public interface IShaderOverlay {

    boolean shouldRender();

    void render(RenderTarget input, RenderTarget output, float partialTick);
}
