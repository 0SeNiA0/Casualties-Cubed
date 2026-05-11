package net.zaharenko424.casualties_cubed.client.gui.widget;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.StatusSprites;
import net.zaharenko424.casualties_cubed.client.gui.minigames.DislocationMinigameScreen;
import net.zaharenko424.casualties_cubed.client.gui.minigames.ShrapnelMinigameScreen;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.network.MedicalAction;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundMedicalActionPacket;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CustomButton extends AbstractWidget {

    private static final ResourceLocation tex = CasualtiesCubed.resourceLoc("textures/gui/button.png");

    private final StatusSprites status;
    private final Limb limb;
    private final Player target;

    public CustomButton(int pX, int pY, StatusSprites status, Limb limb, Player target) {
        super(pX, pY, 128, 16, status.comp);
        this.status = status;
        this.limb = limb;
        this.target = target;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.blit(tex, getX(), getY(), 0, 0, 128, 16, 128, 16);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, getMessage(), getX() + width / 2, getY() + height / 2 - 2, 0xFFFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        MedicalAction action = switch (status) {
            case TOURNIQUET -> MedicalAction.REMOVE_TOURNIQUET;
            case DISLOCATION -> MedicalAction.FIX_DISLOCATION;
            case SHRAPNEL -> MedicalAction.TRY_SHRAPNEL;
            case SPLINT -> MedicalAction.REMOVE_SPLINT;
            default -> null;
        };

        if (action == MedicalAction.FIX_DISLOCATION) {
            Minecraft.getInstance().setScreen(new DislocationMinigameScreen(Minecraft.getInstance().screen, target, limb));
            return;
        }

        if (action == MedicalAction.TRY_SHRAPNEL) {
            Minecraft.getInstance().setScreen(new ShrapnelMinigameScreen(Minecraft.getInstance().screen, target, limb, false));
            return;
        }

        if (action != null) {
            ModNetwork.CHANNEL.sendToServer(new ServerboundMedicalActionPacket(target.getId(), limb, action));
        }
    }
}
