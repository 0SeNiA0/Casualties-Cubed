package net.zaharenko424.casualties_cubed.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.client.gui.WidgetHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderableImage implements Renderable {

    protected ResourceLocation texture;
    public final int uOffset, u;
    public final int vOffset, v;
    public final int texWidth, texHeight;

    public float fillAmount = 1;
    public FillMode fillMode = FillMode.LEFT_TO_RIGHT;

    public final Vector3f offset = new Vector3f();
    public OffsetMode offsetMode = OffsetMode.CENTER;
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

    public RenderableImage texture(@NotNull ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public RenderableImage fillMode(FillMode mode) {
        this.fillMode = mode;
        return this;
    }

    public RenderableImage offsetMode(OffsetMode mode) {
        this.offsetMode = mode;
        return this;
    }

    public void render(GuiGraphics graphics, float partialTick) {
        if (fillAmount <= 0) return;

        PoseStack stack = graphics.pose();
        stack.pushPose();

        stack.translate(offset.x, offset.y, offset.z);
        stack.mulPose(rotation);
        stack.scale(scale.x, scale.y, scale.z);
        if (offsetMode == OffsetMode.CENTER) stack.translate(-u / 2f, -v / 2f, 0);

        if (tint != -1) RenderSystem.setShaderColor(FastColor.ARGB32.red(tint) / 255f, FastColor.ARGB32.green(tint) / 255f, FastColor.ARGB32.blue(tint) / 255f, FastColor.ARGB32.alpha(tint) / 255f);

        if (fillAmount >= 1) {
            graphics.blit(texture, 0, 0, uOffset, vOffset, u, v, texWidth, texHeight);
        } else fillMode.renderer.render(graphics, this, partialTick);

        if (tint != -1) RenderSystem.setShaderColor(1, 1, 1, 1);

        stack.popPose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        render(graphics, partialTick);
    }

    public interface FillRenderer {
        void render(GuiGraphics graphics, RenderableImage image, float partialTick);
    }

    public enum FillMode {
        LEFT_TO_RIGHT((graphics, image, partialTick) ->
                WidgetHelper.blit(image.texture, graphics.pose(), 0, 0, image.u * image.fillAmount, image.v, image.uOffset, image.vOffset, image.u * image.fillAmount, image.v, image.texWidth, image.texHeight)),
        RIGHT_TO_LEFT((graphics, image, partialTick) ->
                WidgetHelper.blit(image.texture, graphics.pose(), image.u * (1 - image.fillAmount), 0, image.u * image.fillAmount, image.v, image.uOffset + image.u * (1 - image.fillAmount), image.vOffset, image.u * image.fillAmount, image.v, image.texWidth, image.texHeight)),
        BOTTOM_TO_TOP((graphics, image, partialTick) ->
                WidgetHelper.blit(image.texture, graphics.pose(), 0, image.v * (1 - image.fillAmount), image.u, image.v * image.fillAmount, image.uOffset, image.vOffset + image.v * (1 - image.fillAmount), image.u, image.v * image.fillAmount, image.texWidth, image.texHeight)),
        CLOCKWISE_360((graphics, image, partialTick) -> {//TODO test
            float amount = image.fillAmount;
            for (int i = 0; i < 4; i++) {
                if ((i + 1) * 0.25f <= amount) {//draw fully filled pieces
                    float x = i < 2 ? image.u / 2f : 0;
                    float y = i == 0 ? 0 : image.v / 2f;
                    WidgetHelper.blit(image.texture, graphics.pose(), x, y, image.u / 2f, image.v / 2f, image.uOffset + x, image.vOffset + y, image.u / 2f, image.v / 2f, image.texWidth, image.texHeight);
                    if ((i + 1) * 0.25f == amount) return;
                    continue;
                }

                float rad = Mth.TWO_PI * image.fillAmount;
                float x = Mth.cos(-(rad - Mth.HALF_PI));
                float y = Mth.sin(rad + Mth.HALF_PI);
                float squareX;
                float squareY;

                if (Math.abs(x) >= Math.abs(y)) {
                    squareX = Math.signum(x);
                    squareY = y / Math.abs(x);
                } else {
                    squareX = x / Math.abs(y);
                    squareY = Math.signum(y);
                }

                RenderSystem.setShaderTexture(0, image.texture);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                PoseStack stack = graphics.pose();
                stack.pushPose();
                stack.translate(-image.u / 4f, image.v / 4f, 0);
                stack.mulPose(new Quaternionf().rotateZ(i * Mth.HALF_PI));
                stack.translate(image.u / 4f, -image.v / 4f, 0);
                Matrix4f matrix4f = stack.last().pose();
                BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

                float uvRotation = i * Mth.HALF_PI;
                Vector3f uv = new Vector3f(image.uOffset + image.u / 2f, 0, 0);
                uv.rotateZ(uvRotation);
                bufferbuilder.vertex(matrix4f, image.u / 2f, 0, 0).uv(uv.x, uv.y).endVertex();
                uv.set(image.uOffset + image.u / 2f, image.vOffset + image.v / 2f, 0);
                uv.rotateZ(uvRotation);
                bufferbuilder.vertex(matrix4f, image.u / 2f, 0, 0).uv(uv.x, uv.y).endVertex();

                if (squareX == 1) {
                    uv.set(image.uOffset + image.u, image.vOffset + image.v / 2f * squareY, 0);
                    uv.rotateZ(uvRotation);
                    bufferbuilder.vertex(matrix4f, image.u, image.v / 2f * squareY, 0).uv(uv.x, uv.y).endVertex();

                    if (squareY == 1) {
                        bufferbuilder.vertex(matrix4f, image.u, image.v / 2f * squareY, 0).uv(uv.x, uv.y).endVertex();
                    } else {
                        uv.set(image.uOffset + image.u, image.vOffset, 0);
                        uv.rotateZ(uvRotation);
                        bufferbuilder.vertex(matrix4f, image.u, 0, 0).uv(uv.x, uv.y).endVertex();
                    }
                } else {//squareY must be 1
                    uv.set(image.uOffset + image.u / 2f + image.u / 2f * squareX, image.vOffset, 0);
                    uv.rotateZ(uvRotation);
                    bufferbuilder.vertex(matrix4f, image.u / 2f * squareX, 0, 0).uv(uv.x, uv.y).endVertex();
                    bufferbuilder.vertex(matrix4f, image.u / 2f * squareX, 0, 0).uv(uv.x, uv.y).endVertex();
                }

                BufferUploader.drawWithShader(bufferbuilder.end());
                stack.popPose();
                return;
            }
        });

        public final FillRenderer renderer;

        FillMode(FillRenderer renderer) {
            this.renderer = renderer;
        }
    }
}
