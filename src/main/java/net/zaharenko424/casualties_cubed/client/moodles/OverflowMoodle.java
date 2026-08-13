package net.zaharenko424.casualties_cubed.client.moodles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

public class OverflowMoodle extends AbstractMoodle {

    public int leftover = 0;

    @Override
    public void update(Player player, PlayerHealthData data) {
        setStatus(MoodleStatus.LIGHT_NEG);
    }

    public void setLeftover(int leftover) {
        this.leftover = leftover;
    }

    @Override
    protected void renderIcon(GuiGraphics ms, float partialTicks, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        ms.pose().pushPose();
        ms.pose().scale(0.8f,0.8f,0.8f);
        ms.drawCenteredString(mc.font,"+"+leftover, (int) ((x+8)*1.25), (int) ((y+5)*1.25),0xFFFFFF);
        ms.pose().popPose();
    }
}
