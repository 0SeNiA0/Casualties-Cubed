package net.zaharenko424.casualties_cubed.client.gui.minigames;

import com.mojang.math.Axis;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ServerboundCPRPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class CPRTimer {
    
    protected double x, y; // logical position
    private float angle = 0;
    private float newangle=0;

    public CPRTimer(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void render(GuiGraphics guiGraphics,float partial) {
        var pose = guiGraphics.pose();
        pose.pushPose();
        float time = Minecraft.getInstance().gui.getGuiTicks()+partial;

        newangle = (float) (Math.sin(time/20*4)*Math.toRadians(55));
        // Translate to object position
        angle = (float) Mth.lerp(0.25,angle,newangle);
        pose.translate(x, y, 0);
        pose.translate(-128 / 2f, -128 / 2f, 0);
        guiGraphics.blit(CasualtiesCubed.resourceLoc("textures/gui/cpr_timer.png")
                ,0,0,0,0,128,128,128,128);
        pose.popPose();
        pose.pushPose();
        pose.translate(x, y, 0);
        pose.mulPose(Axis.ZP.rotation(angle));
        pose.translate(-128 / 2f, -128 / 2f, 0);
        guiGraphics.blit(CasualtiesCubed.resourceLoc("textures/gui/cpr_marker.png")
                ,0,0,0,0,128,128,128,128);
        pose.popPose();
    }

    public void mouseClicked(float distance, Player target){
        Player player = Minecraft.getInstance().player;
        float distanceFromMax = distance/50f;
        float tempAngle = (float) Math.abs(Math.toDegrees(angle));
        tempAngle += distanceFromMax *25;
        if (tempAngle<20){
            player.playSound(SoundEvents.PLAYER_ATTACK_CRIT);
            ModNetwork.CHANNEL.sendToServer(new ServerboundCPRPacket(target.getId(), ServerboundCPRPacket.Success.HIGH));
        } else if (tempAngle<45) {
            ModNetwork.CHANNEL.sendToServer(new ServerboundCPRPacket(target.getId(), ServerboundCPRPacket.Success.MEDIUM));
            player.playSound(SoundEvents.PLAYER_ATTACK_STRONG);
        } else {
            ModNetwork.CHANNEL.sendToServer(new ServerboundCPRPacket(target.getId(), ServerboundCPRPacket.Success.LOW));
            player.playSound(SoundEvents.PLAYER_ATTACK_NODAMAGE);
        }
    }

}
