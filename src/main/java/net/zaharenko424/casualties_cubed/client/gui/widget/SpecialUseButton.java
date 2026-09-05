package net.zaharenko424.casualties_cubed.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.StatusSprites;
import net.zaharenko424.casualties_cubed.client.gui.WidgetHelper;
import net.zaharenko424.casualties_cubed.client.gui.minigames.DislocationMinigameScreen;
import net.zaharenko424.casualties_cubed.client.gui.minigames.ShrapnelMinigameScreen;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.network.MedicalAction;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundMedicalActionPacket;
import net.zaharenko424.casualties_cubed.registry.ModSounds;
import net.zaharenko424.casualties_cubed.util.Util;
import org.joml.Vector3f;

public class SpecialUseButton extends ImageButton {

    private static final ResourceLocation TEX = CasualtiesCubed.texLoc("gui/icons/ui_point");

    private final PlayerHealthData data;
    private LimbWidget limbWidget;

    public SpecialUseButton(Player localPlayer, Player target, PlayerHealthData data) {
        super(80, 30, new RenderableImage(TEX, 80, 30), button -> {
            Limb limb = ((SpecialUseButton)button).limbWidget.getLimb();
            LimbStatistics stats = data.getLimb(limb);
            if (stats.isTourniquet()) {
                ModNetwork.CHANNEL.sendToServer(new ServerboundMedicalActionPacket(target.getId(), limb, MedicalAction.REMOVE_TOURNIQUET));
            } else if (stats.getShrapnel() > 0) {
                Minecraft.getInstance().setScreen(new ShrapnelMinigameScreen(Minecraft.getInstance().screen, target, limb, false));
            } else if (stats.hasSplint()) {
                ModNetwork.CHANNEL.sendToServer(new ServerboundMedicalActionPacket(target.getId(), limb, MedicalAction.REMOVE_SPLINT));
            } else if (stats.getDislocationTimer() > 0) {
                Minecraft.getInstance().setScreen(new DislocationMinigameScreen(Minecraft.getInstance().screen, target, limb));
            } else return;
            localPlayer.playSound(ModSounds.SMALL_CLICK.get());
        });
        this.data = data;
        offset.set(Float.POSITIVE_INFINITY);
    }

    public void selectLimb(LimbWidget limb) {
        this.limbWidget = limb;
    }

    @Override
    public boolean visible() {
        if (limbWidget == null) return false;

        LimbStatistics stats = data.getLimb(limbWidget.getLimb());
        return stats.isTourniquet() || stats.getShrapnel() > 0 || stats.hasSplint() || stats.getDislocationTimer() > 0;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pPartialTick) {
        if (!visible()) return;

        float x = limbWidget.getX() + limbWidget.getWidth() / 2f;
        float y = limbWidget.getY();
        if (limbWidget.getHeight() / 2f < height / 2f) y -= height / 2f - limbWidget.getHeight() / 2f;
        if (!offset.isFinite()) {
            offset.set(x, y, 1);
        } else {
            offset.lerp(new Vector3f(x, y, 1), Util.TICK_TO_SEC * pPartialTick * 4);
        }

        hovering = isMouseOver(mouseX, mouseY);

        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(offset.x, offset.y, offset.z);

        int tint = image.tint;
        if (!active()) {
            image.tint = inactiveTint;
        } else if (holding) {
            image.tint = holdingTint;
        } else if (hovering) {
            image.tint = hoveringTint;
        }
        image.render(graphics, mouseX, mouseY, pPartialTick);
        image.tint = tint;

        Component comp;
        LimbStatistics stats = data.getLimb(limbWidget.getLimb());
        if (stats.isTourniquet()) {
            comp = StatusSprites.TOURNIQUET.comp;
        } else if (stats.getShrapnel() > 0) {
            comp = StatusSprites.SHRAPNEL.comp;
        } else if (stats.hasSplint()) {
            comp = StatusSprites.SPLINT.comp;
        } else comp = StatusSprites.DISLOCATION.comp;
        stack.translate(0, -5, 0);
        stack.scale(0.75f, 0.75f, 1);
        WidgetHelper.drawCenteredComp(graphics, Minecraft.getInstance().font, comp, 0, 0, -1, false);

        stack.popPose();

        if (hovering) graphics.renderComponentTooltip(Minecraft.getInstance().font, tooltip, mouseX, mouseY);
    }
}
