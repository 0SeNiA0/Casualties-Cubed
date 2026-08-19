package net.zaharenko424.casualties_cubed.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WidgetHelper {

    private static final ThreadLocal<Vector3f> VEC3F = ThreadLocal.withInitial(Vector3f::new);

    public static void blit(ResourceLocation tex, PoseStack stack, float x, float y, float width, float height, float texWidth, float texHeight){
        blit(tex, stack, x, y, 0, width, height, 0, 0, texWidth, texHeight, texWidth, texHeight);
    }

    public static void blit(ResourceLocation tex, PoseStack stack, float x, float y, float width, float height, float uWidth, float vHeight, float texWidth, float texHeight){
        blit(tex, stack, x, y, 0, width, height, 0, 0, uWidth, vHeight, texWidth, texHeight);
    }

    public static void blit(ResourceLocation tex, PoseStack stack, float x, float y, float width, float height, float uOffset, float vOffset, float uWidth, float vHeight, float texWidth, float texHeight){
        blit(tex, stack, x, y, 0, width, height, uOffset, vOffset, uWidth, vHeight, texWidth, texHeight);
    }

    public static void blit(ResourceLocation tex, PoseStack stack, float x, float y, float z, float width, float height, float uOffset, float vOffset, float uWidth, float vHeight, float texWidth, float texHeight){
        RenderSystem.setShaderTexture(0, tex);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = stack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        float minU = uOffset / texWidth;
        float maxU = (uOffset + uWidth) / texWidth;
        float minV = vOffset / texHeight;
        float maxV = (vOffset + vHeight) / texHeight;

        bufferbuilder.vertex(matrix4f, x, y, z).uv(minU, minV).endVertex();
        bufferbuilder.vertex(matrix4f, x, y + height, z).uv(minU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y + height, z).uv(maxU, maxV).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y, z).uv(maxU, minV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public static void fill(PoseStack stack, float minX, float minY, float maxX, float maxY, float z, int color) {
        fill(RenderType.gui(), stack, minX, minY, maxX, maxY, z, color);
    }

    public static void fill(RenderType renderType, PoseStack stack, float minX, float minY, float maxX, float maxY, float z, int color) {
        if(minY == maxY || minX == maxX) return;

        Matrix4f matrix4f = stack.last().pose();
        if (minX < maxX) {
            float i = minX;
            minX = maxX;
            maxX = i;
        }

        if (minY < maxY) {
            float j = minY;
            minY = maxY;
            maxY = j;
        }

        VertexConsumer vertexconsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType);
        vertexconsumer.vertex(matrix4f, minX, minY, z).color(color);
        vertexconsumer.vertex(matrix4f, minX, maxY, z).color(color);
        vertexconsumer.vertex(matrix4f, maxX, maxY, z).color(color);
        vertexconsumer.vertex(matrix4f, maxX, minY, z).color(color);
    }

    public static int drawCenteredString(GuiGraphics graphics, Font font, String text, float x, float y, int color, boolean shadow) {
        return graphics.drawString(font, text, x - font.width(text) / 2f, y - 4, color, shadow);
    }

    public static void renderScrollingString(GuiGraphics guiGraphics, Font font, String text, float centerX, float minX, float minY, float maxX, float maxY, int color, boolean shadow) {
        int i = font.width(text);
        int k = (int) (maxX - minX);
        if(i <= k){
            drawCenteredString(guiGraphics, font, text, Mth.clamp(centerX, minX + i / 2f, maxX - i / 2f), minY + maxY, color, shadow);
            return;
        }

        int l = i - k;
        double d0 = (double) Util.getMillis() / 1000.0;
        double d1 = Math.max((double)l * 0.5, 3.0);
        double d2 = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * d0 / d1)) / 2.0 + 0.5;
        double d3 = Mth.lerp(d2, 0.0, l);
        //Transform rect to screen coordinates to apply scissors
        Matrix4f mat = guiGraphics.pose().last().pose();
        Vector3f vec = VEC3F.get();
        guiGraphics.enableScissor((int) vec.set(minX, minY, 0).mulPosition(mat).x, (int) vec.y,
                (int) vec.set(maxX, maxY, 0).mulPosition(mat).x, (int) vec.y);

        guiGraphics.drawString(font, text, (float) (minX - d3), minY, color, false);

        guiGraphics.disableScissor();
    }

    public static int drawCenteredComp(GuiGraphics graphics, Font font, Component comp, float x, float y, int color, boolean shadow) {
        FormattedCharSequence formatted = comp.getVisualOrderText();
        return graphics.drawString(font, formatted, x - font.width(formatted) / 2f, y - 4, color, shadow);
    }

    public static void renderScrollingComp(GuiGraphics guiGraphics, Font font, Component text, float centerX, float minX, float minY, float maxX, float maxY, int color, boolean shadow) {
        int i = font.width(text);
        int k = (int) (maxX - minX);
        if(i <= k){
            drawCenteredComp(guiGraphics, font, text, Mth.clamp(centerX, minX + i / 2f, maxX - i / 2f), minY + maxY, color, shadow);
            return;
        }

        int l = i - k;
        double d0 = (double) Util.getMillis() / 1000.0;
        double d1 = Math.max((double)l * 0.5, 3.0);
        double d2 = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * d0 / d1)) / 2.0 + 0.5;
        double d3 = Mth.lerp(d2, 0.0, l);
        //Transform rect to screen coordinates to apply scissors
        Matrix4f mat = guiGraphics.pose().last().pose();
        Vector3f vec = VEC3F.get();
        guiGraphics.enableScissor((int) vec.set(minX, minY, 0).mulPosition(mat).x, (int) vec.y,
                (int) vec.set(maxX, maxY, 0).mulPosition(mat).x, (int) vec.y);

        guiGraphics.drawString(font, text.getVisualOrderText(), (float) (minX - d3), minY, color, false);

        guiGraphics.disableScissor();
    }

    public static int color(int packed, int alpha) {
        return alpha << 24 | (packed & 0x00FFFFFF);
    }
}
