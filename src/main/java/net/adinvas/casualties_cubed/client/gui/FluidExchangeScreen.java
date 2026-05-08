package net.adinvas.casualties_cubed.client.gui;

import net.adinvas.casualties_cubed.fluid_system.MultiTankHelper;
import net.adinvas.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.adinvas.casualties_cubed.network.ModNetwork;
import net.adinvas.casualties_cubed.network.packet.ServerboundTransferFluidPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FluidExchangeScreen extends Screen {

    private final AbstractContainerScreen<?> screen;
    private final ItemStack toItem;
    private final ItemStack fromItem;
    private final int toSlot;
    private final Button button;

    private float sliderValue = 1f;
    private float sourceAmount;
    private float targetCapacity;
    private float MaxSlider;

    public FluidExchangeScreen(AbstractContainerScreen<?> screen, ItemStack toItem, ItemStack fromItem, int toSlot) {
        super(Component.literal("Fluid Exchange"));
        this.screen = screen;
        this.toItem = toItem;
        this.fromItem = fromItem;
        this.toSlot = toSlot;
        button = Button.builder(Component.literal("Transfer"), b -> confirmTransfer()).bounds(width / 2 - 40, height / 2 + 30, 80, 20).build();
    }

    @Override
    protected void init() {
        button.setX(width / 2 - 40);
        button.setY(height / 2 + 30);
        addRenderableWidget(button);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderBackground(pGuiGraphics);
        pGuiGraphics.drawCenteredString(font, "Fluid Transfer", width / 2, height / 2 - 60, 0xFFFFFF);

        int barWidth = 120;
        int barX = width / 2 - barWidth / 2;
        int barY = height / 2;

        if (fromItem.getItem() instanceof MultiTankFluidItem) {
            sourceAmount = MultiTankHelper.getFilledTotal(fromItem);
        }
        if (toItem.getItem() instanceof MultiTankFluidItem) {
            float cap = MultiTankHelper.getCapacity(toItem);
            float filled = MultiTankHelper.getFilledTotal(toItem);
            targetCapacity = cap - filled;
        }

        int color = 0xFFFFFFFF;
        MaxSlider = Math.min(sourceAmount, targetCapacity);

        // draw slider bar
        pGuiGraphics.fill(barX, barY, barX + barWidth, barY + 8, 0xFF444444);
        pGuiGraphics.fill(barX, barY, barX + (int) (barWidth * sliderValue), barY + 8, (255 << 24) | color);

        pGuiGraphics.drawCenteredString(font, (int) (sliderValue * MaxSlider) + "ml", width / 2, barY + 12, 0xAAAAAA);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        int barWidth = 120;
        int barX = width / 2 - barWidth / 2;
        int barY = height / 2;
        if (pMouseY >= barY && pMouseY <= barY + 8 && pMouseX >= barX && pMouseX <= barX + barWidth) {
            sliderValue = (float) ((pMouseX - barX) / barWidth);
            sliderValue = Math.max(0f, Math.min(1f, sliderValue));
            return true;
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    public void confirmTransfer() {
        float amount = sliderValue * MaxSlider;
        if (amount >= 1) ModNetwork.CHANNEL.sendToServer(new ServerboundTransferFluidPacket(toSlot, (int) amount));
        AbstractContainerScreenExt.setSkipNextRelease(screen);
        onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
