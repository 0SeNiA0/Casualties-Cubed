package net.zaharenko424.casualties_cubed.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.WidgetHelper;
import net.zaharenko424.casualties_cubed.client.gui.screen.HealthScreen;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import org.joml.Vector2f;

public class HealthScreenItemWidget implements Renderable, GuiEventListener, NarratableEntry {

    protected static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/icons/item_bg.png");
    protected static final int width = 16, height = 16;

    public final Vector2f offset = new Vector2f();

    protected final HealthScreen screen;
    protected final MaybeBagItemWidget bag;

    protected HumanoidArm arm;
    protected int slot;
    protected ItemStack stack = ItemStack.EMPTY;

    protected boolean dragging;
    protected int stackX, stackY;

    public HealthScreenItemWidget(HealthScreen screen, MaybeBagItemWidget bag) {
        this.screen = screen;
        this.bag = bag;
    }

    public void set(HumanoidArm arm, int slot, ItemStack stack, int x, int y) {
        this.arm = arm;
        this.slot = slot;
        this.stack = stack;
        dragging = false;
        offset.set(x, y);
        stackX = x;
        stackY = y;
    }

    public HumanoidArm arm() {
        return arm;
    }

    public int slot() {
        return slot;
    }

    public ItemStack stack() {
        return stack;
    }

    public ItemStack bagStack() {
        return bag == null ? null : bag.stack();
    }

    public void init() {
        stackX = (int) offset.x;
        stackY = (int) offset.y;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {
        RenderSystem.enableBlend();
        WidgetHelper.blit(TEX, guiGraphics.pose(), offset.x, offset.y, 16, 16, 16, 16);
        guiGraphics.renderItem(this.stack, stackX, stackY);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, this.stack, stackX, stackY);
        if (!dragging && isMouseOver(mouseX, mouseY) && !screen.BGmode && !stack.isEmpty()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, this.stack, mouseX, mouseY);
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= offset.x && mouseY >= offset.y && mouseX < offset.x + width && mouseY < offset.y + height;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (isMouseOver(pMouseX, pMouseY) && !stack.isEmpty()) {
            dragging = true;
            Minecraft.getInstance().player.playSound(ModSounds.SMALL_CLICK.get());
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (dragging) {
            stackX = (int) pMouseX - 8;
            stackY = (int) pMouseY - 8;
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (dragging) {
            dragging = false;
            screen.useItem(this, pMouseX, pMouseY);
            Minecraft.getInstance().player.playSound(ModSounds.CLICK.get());
            stackX = (int) offset.x;
            stackY = (int) offset.y;
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
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {}
}
