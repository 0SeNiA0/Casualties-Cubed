package net.zaharenko424.casualties_cubed.client.overlays.ovr;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ConsciousnessOverlay implements IOverlay {

    private static final ResourceLocation VIGNETTE_LOCATION = CasualtiesCubed.resourceLoc("textures/consciousness_vignette.png");

    private float intensity = 1;
    private float brain = 100;
    private boolean dying = false;

    private float lastInt = 0;

    @Override
    public void render(GuiGraphics ms, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        intensity = (float) Mth.lerp(0.25, lastInt, intensity);
        lastInt = intensity;

        float b = intensity <= .2 ? Mth.lerp(intensity * 5, 0, 1) : 1;

        if (intensity < 1) {

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();

            // Use multiplicative blending so white is transparent, black darkens screen
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
            float intensity2 = b * .8f + intensity * .2f;
            ms.setColor(intensity2, intensity2, intensity2, 1f);
            ms.blit(
                    VIGNETTE_LOCATION,
                    0, 0,
                    0, 0,
                    width, height,
                    width, height
            );
            RenderSystem.defaultBlendFunc();
        }
        ms.setColor(b, b, b, intensity);
        ms.fill(RenderType.guiOverlay(), 0, 0, width, height, 0xFF000000);
        ms.flush();
        ms.setColor(1F, 1F, 1F, 1);
        if (intensity > 0.95) {
            Component text = Component.translatable("casualties_cubed.gui.give_up", Component.keybind("key.casualties_cubed.give_up"));
            ms.drawCenteredString(mc.font, text, width / 2, height / 2, 0xFFFFFF);
        }

        if (dying) {
            ms.blit(CasualtiesCubed.resourceLoc("textures/gui/icons/brain.png"), width / 2 - 16, height / 4 - 40, 0, 0, 32, 32, 32, 32);
            ms.fill(width / 2 - 50, height / 4, (int) (width / 2 - 50 + brain), height / 4 + 10, 0xFFFF4444);
        }
    }

    @Override
    public boolean shouldRender() {
        return intensity > 0;
    }

    public void calculate(Player player) {
        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            intensity = (100 - data.getConsciousness()) / 100;
            dying = data.getBloodOxygen() < 4;
            brain = data.getBrainHealth();
        });
    }
}
