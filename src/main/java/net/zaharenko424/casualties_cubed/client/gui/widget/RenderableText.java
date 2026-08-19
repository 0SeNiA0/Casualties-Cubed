package net.zaharenko424.casualties_cubed.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.BiConsumer;

public class RenderableText implements Renderable {

    public final Vector3f offset = new Vector3f();
    public final Quaternionf rotation = new Quaternionf();
    public final Vector3f scale = new Vector3f(1);

    public Font font = Minecraft.getInstance().font;
    public int maxWidth = 200;
    public List<FormattedCharSequence> text = List.of();
    public TextAlignment alignment = TextAlignment.LEFT;
    public int color = -1;
    public boolean shadow = false;

    public RenderableText offset(float x, float y, float z) {
        offset.set(x, y, z);
        return this;
    }

    public RenderableText scale(float x, float y, float z) {
        scale.set(x, y, z);
        return this;
    }

    public RenderableText component(Component comp) {
        text = font.split(comp, maxWidth);
        return this;
    }

    public RenderableText alignment(TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public void render(GuiGraphics graphics) {
        if (text.isEmpty()) return;

        PoseStack stack = graphics.pose();
        stack.pushPose();

        stack.translate(offset.x, offset.y, offset.z);
        stack.mulPose(rotation);
        stack.scale(scale.x, scale.y, scale.z);

        alignment.renderer.accept(graphics, this);

        stack.popPose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        render(graphics);
    }

    public enum TextAlignment {
        LEFT((graphics, renderableText) -> {
            int i = 0;
            for (FormattedCharSequence sequence : renderableText.text) {
                graphics.drawString(renderableText.font, sequence, 0, i * renderableText.font.lineHeight, renderableText.color, renderableText.shadow);
                i++;
            }
        }),
        CENTER((graphics, renderableText) -> {
            int i = 0;
            for (FormattedCharSequence sequence : renderableText.text) {
                graphics.drawString(renderableText.font, sequence, -renderableText.font.width(sequence) / 2f, i * renderableText.font.lineHeight, renderableText.color, renderableText.shadow);
                i++;
            }
        }),
        RIGHT((graphics, renderableText) -> {
            int i = 0;
            for (FormattedCharSequence sequence : renderableText.text) {
                graphics.drawString(renderableText.font, sequence, -renderableText.font.width(sequence), i * renderableText.font.lineHeight, renderableText.color, renderableText.shadow);
                i++;
            }
        });

        public final BiConsumer<GuiGraphics, RenderableText> renderer;

        TextAlignment(BiConsumer<GuiGraphics, RenderableText> renderer) {
            this.renderer = renderer;
        }
    }
}
