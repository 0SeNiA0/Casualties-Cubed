package net.zaharenko424.casualties_cubed.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ImageButton implements Renderable, GuiEventListener, NarratableEntry {

    private final Runnable onClick;
    public final Vector3f offset = new Vector3f();

    private int width, height;
    private RenderableImage image;
    private boolean active = true;
    private List<Component> tooltip = List.of();

    private boolean hovering;
    private boolean holding;

    public ImageButton(int width, int height, RenderableImage img, Runnable onClick) {
        this.image = img;
        this.onClick = onClick;
        this.width = width;
        this.height = height;
    }

    public RenderableImage image() {
        return image;
    }

    public void image(RenderableImage image) {
        this.image = image;
    }

    public void size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean active() {
        return active;
    }

    public void active(boolean active) {
        this.active = active;
        if (!active) {
            hovering = holding = false;
        }
    }

    public List<Component> tooltip() {
        return tooltip;
    }

    public void tooltip(Component title) {
        tooltip = List.of(title);
    }

    public void tooltip(Component title, Component description) {
        String[] lines = description.getString().split("\n");
        List<Component> list = new ArrayList<>();
        list.add(title);
        for (String str : lines) {
            list.add(Component.literal(str).withStyle(ChatFormatting.GRAY));
        }

        tooltip = List.copyOf(list);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pPartialTick) {
        hovering = isMouseOver(mouseX, mouseY);

        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(offset.x, offset.y, offset.z);

        if (!active) {
            image.tint = FastColor.ARGB32.color(255, 102, 102, 102);
        } else if (holding) {
            image.tint = FastColor.ARGB32.color(255, 168, 168, 168);
        } else if (hovering) {
            image.tint = FastColor.ARGB32.color(255, 214, 214, 214);
        } else image.tint = -1;
        image.render(graphics, mouseX, mouseY, pPartialTick);

        stack.popPose();

        if (hovering) graphics.renderComponentTooltip(Minecraft.getInstance().font, tooltip, mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        return mouseX >= offset.x - halfWidth && mouseX <= offset.x + halfWidth
                && mouseY >= offset.y - halfHeight && mouseY <= offset.y + halfHeight;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (active && hovering) {
            holding = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        holding = false;

        if (active && hovering) {
            onClick.run();
            return true;
        }

        return false;
    }



    @Override
    public void setFocused(boolean pFocused) {}

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return hovering ? NarrationPriority.HOVERED : NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        if (!tooltip.isEmpty()) pNarrationElementOutput.add(NarratedElementType.HINT, tooltip.get(0));
    }
}
