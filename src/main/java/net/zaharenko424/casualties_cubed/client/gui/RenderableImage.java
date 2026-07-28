package net.zaharenko424.casualties_cubed.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderableImage implements Renderable {

    public final ResourceLocation texture;
    public final int uOffset, u;
    public final int vOffset, v;
    public final int texWidth, texHeight;

    public final Vector3f offset = new Vector3f();
    public final Quaternionf rotation = new Quaternionf();
    public final Vector3f scale = new Vector3f(1);

    public int tint = -1;

    public RenderableImage(ResourceLocation texture, int texWidth, int texHeight) {
        this(texture, 0, texWidth, 0, texHeight, texWidth, texHeight);
    }

    public RenderableImage(ResourceLocation texture, int uOffset, int u, int vOffset, int v, int texWidth, int texHeight) {
        this.texture = texture;
        this.uOffset = uOffset;
        this.u = u;
        this.vOffset = vOffset;
        this.v = v;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        PoseStack stack = pGuiGraphics.pose();
        stack.pushPose();

        stack.translate(offset.x, offset.y, offset.z);
        stack.mulPose(rotation);
        stack.scale(scale.x, scale.y, scale.z);

        if (tint != -1) RenderSystem.setShaderColor(FastColor.ARGB32.red(tint) / 255f, FastColor.ARGB32.green(tint) / 255f, FastColor.ARGB32.blue(tint) / 255f, FastColor.ARGB32.alpha(tint) / 255f);

        pGuiGraphics.blit(texture, -u / 2, -v / 2, uOffset, vOffset, u, v, texWidth, texHeight);

        if (tint != -1) RenderSystem.setShaderColor(1, 1, 1, 1);

        stack.popPose();
    }
}
