package net.zaharenko424.casualties_cubed.client.gui.widget;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ItemWidget extends AbstractWidget {

    private static final ResourceLocation TEX = CasualtiesCubed.resourceLoc("textures/gui/icons/item_bg.png");

    private ItemStack stack;
    private int stack_x, stack_y;
    private boolean dragging;
    private boolean BGMode = false;

    public void setBGMode(boolean BGMode) {
        this.BGMode = BGMode;
    }

    public boolean isDragging() {
        return dragging;
    }

    public ItemWidget(int x, int y, ItemStack itemstack) {
        super(x, y, 16, 16, Component.empty());
        setStack(itemstack);
        this.stack_x = x;
        this.stack_y = y;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack == null ? ItemStack.EMPTY : stack;
    }

    public void setStackPos(int stack_x, int stack_y) {
        this.stack_x = stack_x;
        this.stack_y = stack_y;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(TEX, this.getX(), getY(), 0, 0, 16, 16, 16, 16);
        guiGraphics.renderItem(this.stack, stack_x, stack_y);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, this.stack, stack_x, stack_y);
        if (!dragging && isHovered() && !BGMode && !stack.isEmpty()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, this.stack, mouseX, mouseY);
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, this.stack.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.NORMAL), mouseX, mouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
    }

    @Override
    protected void onDrag(double pMouseX, double pMouseY, double pDragX, double pDragY) {
        super.onDrag(pMouseX, pMouseY, pDragX, pDragY);
        if (dragging) {
            stack_x = (int) pMouseX - 8;
            stack_y = (int) pMouseY - 8;
        }
    }

    @Override
    public void onRelease(double pMouseX, double pMouseY) {
        super.onRelease(pMouseX, pMouseY);
        if (dragging) {
            dragging = false;
            stack_x = getX();
            stack_y = getY();
        }
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        dragging = true;
    }
}
