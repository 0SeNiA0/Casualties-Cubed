package net.zaharenko424.casualties_cubed.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundTransferFluidPacket;

import java.util.List;

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
        if (amount >= 1) {
            if (!minecraft.player.isCreative()) {
                ModNetwork.CHANNEL.sendToServer(new ServerboundTransferFluidPacket(toSlot, (int) amount));
            } else {
                creativeTransfer((int) amount);
            }
        }
        AbstractContainerScreenExt.setSkipNextRelease(screen);
        onClose();
    }

    private void creativeTransfer(int amount) {
        Player player = minecraft.player;
        AbstractContainerMenu menu = player.containerMenu;
        if (menu == null) return;

        ItemStack fromStack = menu.getCarried();
        ItemStack toStack = toItem;
        if (fromStack.isEmpty() || !(fromStack.getItem() instanceof MultiTankFluidItem fromTank)
                || toStack.isEmpty() || !(toStack.getItem() instanceof MultiTankFluidItem toTank)) return;

        float toTransfer = Math.min(amount, Math.min(fromTank.getHandler(fromStack).getTank().getTotalFluid(), toTank.getHandler(toStack).getTank().getFreeSpace()));

        if (toTransfer < 1) return;

        List<FluidStack> drained = MultiTankHelper.drain(fromStack, toTransfer);
        for (FluidStack stack : drained) {
            MultiTankHelper.addFluid(toStack, stack.getAmount(), stack);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
