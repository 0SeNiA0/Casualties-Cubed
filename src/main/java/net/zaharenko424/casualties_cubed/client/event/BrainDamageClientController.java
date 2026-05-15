package net.zaharenko424.casualties_cubed.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BrainDamageClientController {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        float brain = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getBrainHealth).orElse(100f);

        if (brain < 95 && minecraft.level != null && minecraft.player != null) {
            if (minecraft.level.getGameTime() % 400 == 0 && Math.random() > 1 - brain / 120f) { // every 20s roughly
                float alpha = (float) (0.2f + Math.random() * 0.3f);
                GuiGraphics g = event.getGuiGraphics();
                g.fill(0, 0, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight(),
                        ((int) (alpha * 255) << 24));
            }
        }
    }


    private static float time = 0f;
    private static float currentPitchOffset = 0f;
    private static float currentYawOffset = 0f;

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return;

        float brain = minecraft.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getBrainHealth).orElse(100f);
        if (brain >= 95) return;

        // Delta time-like increment (ensures consistent speed)
        time += 0.01f;

        // Base amplitude grows as brain health drops
        float strength = (1f - brain / 100f); // 0..1
        float maxAngle = 5f * strength;       // up to 3 degrees at very low brain

        // Combine two gentle sine waves of different frequencies for organic motion
        float targetPitch = (float) (
                Math.sin(time * 0.6f) * 0.7f +   // slow, large wave
                        Math.sin(time * 1.7f) * 0.3f     // faster, smaller wave
        ) * maxAngle;

        float targetYaw = (float) (
                Math.cos(time * 0.4f) * 0.5f +
                        Math.sin(time * 1.3f) * 0.3f
        ) * maxAngle * 0.7f;

        // Smoothly interpolate (lerp) from current to target
        float smooth = 0.05f; // smaller = smoother
        currentPitchOffset += (targetPitch - currentPitchOffset) * smooth;
        currentYawOffset += (targetYaw - currentYawOffset) * smooth;

        // Apply to camera
        event.setPitch(event.getPitch() + currentPitchOffset);
        event.setYaw(event.getYaw() + currentYawOffset);

        // Optional: a very subtle micro jitter when brain <80
        if (brain < 80) {
            float intensity = (80f - brain) / 80f * 0.2f; // subtle
            float jitterPitch = (float) ((Math.random() - 0.5f) * intensity);
            float jitterYaw = (float) ((Math.random() - 0.5f) * intensity);
            event.setPitch(event.getPitch() + jitterPitch);
            event.setYaw(event.getYaw() + jitterYaw);
        }
    }

    public static float sensitivityScale = 1;

    @SubscribeEvent
    public static void onclientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        float brain = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getBrainHealth).orElse(100f);

        if (brain < 80) {
            sensitivityScale = 1 + Mth.sin(minecraft.level.getGameTime() * 0.1f) * 0.05f; // ±5%
        } else {
            sensitivityScale = 1;
        }
    }

    @SubscribeEvent
    public static void onTooltip(RenderTooltipEvent.GatherComponents event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).map(PlayerHealthData::getBrainHealth).orElse(100f) < 60) {
            event.getTooltipElements().clear();
        }
    }
}
