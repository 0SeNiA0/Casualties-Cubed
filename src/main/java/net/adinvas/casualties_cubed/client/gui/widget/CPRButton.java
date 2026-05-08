package net.adinvas.casualties_cubed.client.gui.widget;

import net.adinvas.casualties_cubed.CasualtiesCubed;
import net.adinvas.casualties_cubed.client.gui.minigames.CPRMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CPRButton extends AbstractWidget {

    private static final ResourceLocation tex = CasualtiesCubed.resourceLoc("textures/gui/cpr_button.png");

    private final Screen parent;
    private final Player target;

    public CPRButton(int pX, int pY, Screen parent, Player target) {
        super(pX, pY, 32, 32, Component.empty());
        this.parent = parent;
        this.target = target;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.blit(tex, getX(), getY(), 0, 0, 32, 32, 32, 32);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        if (!visible) return;
        Minecraft.getInstance().setScreen(new CPRMinigameScreen(parent, target));
    }
}
