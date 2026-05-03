package net.adinvas.prototype_pain.client.overlays;


import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.adinvas.prototype_pain.client.overlays.exp.*;
import net.adinvas.prototype_pain.client.overlays.ovr.ContiousnessOverlay;
import net.adinvas.prototype_pain.client.overlays.ovr.IOverlay;
import net.adinvas.prototype_pain.client.overlays.ovr.PainOverlay;
import net.adinvas.prototype_pain.client.overlays.ovr.ReducedContiousnessOverlay;
import net.adinvas.prototype_pain.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class OverlayController {

    private static final List<IOverlay> overlays = new ArrayList<>();
    private static final List<IOverlay> EXoverlays = new ArrayList<>();
    private static final List<IShaderOverlay> shaderOverlays = new ArrayList<>();
    private static final List<IShaderOverlay> EXshaderOverlays = new ArrayList<>();

    public static void registerOverlay(IOverlay overlay) {
        overlays.add(overlay);
    }
    public static void registerEXOverlay(IOverlay overlay) {
        EXoverlays.add(overlay);
    }
    public static void registerOverlay(IShaderOverlay overlay) {
        shaderOverlays.add(overlay);
    }
    public static void registerEXOverlay(IShaderOverlay overlay) {
        EXshaderOverlays.add(overlay);
    }

    public static <T extends IOverlay> T getOverlay(Class<T> clazz) {
        for (IOverlay overlay : overlays) {
            if (clazz.isInstance(overlay)) {
                return clazz.cast(overlay);
            }
        }
        return null;
    }

    public static boolean isExperiment(){
        return ClientConfig.EXPERIMENTAL_VISUALS.get();
    }

    static {
        //normal Shader
        registerOverlay(new BlindnessShaderOverlay());
        registerOverlay(new BrainShaderOverlay());

        //experimental Shader
        registerEXOverlay(new BlindnessShaderOverlay());
        registerEXOverlay(new ConsciousnessShaderOverlayOverlay());
        registerEXOverlay(new HeatOverlay());
        registerEXOverlay(new ColdOverlay());
        //registerEXOverlay(new SicknessOverlay());
        registerEXOverlay(new PainShaderOverlay());
        registerEXOverlay(new BrainShaderOverlay());
    }

    static {
        //normal GUI
        registerOverlay(new PainOverlay());
        registerOverlay(new ContiousnessOverlay());

        //Experimental GUI
        registerEXOverlay(new ReducedContiousnessOverlay());
    }

//TODO make sure that overlays look correctly
    public static void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        ProfilerFiller profiler = minecraft.getProfiler();

        profiler.push("prototype_pain:overlay");
        // Render all registered overlays that should draw
        List<IOverlay> overlays_ = isExperiment() ? EXoverlays : overlays;
        Player player = minecraft.player;
        for (IOverlay overlay : overlays_) {
            overlay.calculate(player);
            if (overlay.shouldRender()) {
                overlay.render(graphics, partialTick);
            }
        }

        profiler.pop();
    }

    public static void renderShaderOverlay(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Minecraft mc = Minecraft.getInstance();
        int w = mc.getMainRenderTarget().width;
        int h = mc.getMainRenderTarget().height;
        if (w <= 0 || h <= 0) return;

        List<IShaderOverlay> ShaderList = isExperiment()?EXshaderOverlays:shaderOverlays;
        if (ShaderList.isEmpty()) return;
        ensureFbos(w, h,ShaderList.size()+1);

        // 1️⃣ Copy main framebuffer → input
        //renderTargets.set(0, mc.getMainRenderTarget());
        blit(mc.getMainRenderTarget(), renderTargets.get(0));

        int i =0;
        // 2️⃣ Run shaders sequentially
        for (IShaderOverlay shaderOverlay : ShaderList) {
            if (!shaderOverlay.shouldRender()) continue;
            RenderTarget input = renderTargets.get(i);
            RenderTarget output = renderTargets.get(i + 1);
            if (i+1==renderTargets.size()){
                output = mc.getMainRenderTarget();
            }
            shaderOverlay.render(event, input, output);

            i++;
        }

        // 3️⃣ Copy final result → main screen
        blit(renderTargets.get(i), mc.getMainRenderTarget());
        mc.getMainRenderTarget().bindWrite(false);
    }

    private static List<RenderTarget> renderTargets = new ArrayList<>();

    private static void ensureFbos(int width, int height,int size) {
        if (width <= 0 || height <= 0) {
            return; // skip creating until valid window size
        }

        while (renderTargets.size() < size) {
            renderTargets.add(null);
        }

        for (int i = 0; i < size; i++) {
            RenderTarget target = renderTargets.get(i);

            // If nonexistent or size mismatch → recreate
            if (target == null || target.width != width || target.height != height) {
                if (target != null) {
                    target.destroyBuffers();
                }

                TextureTarget newTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
                newTarget.setClearColor(1, 1, 1, 1);
                newTarget.createBuffers(width, height, true);
                //PrototypePain.LOGGER.info("TARGET MISMATCH: {}, {}",newTarget,i);

                // ✅ store it back into the list
                renderTargets.set(i, newTarget);
            }
        }
    }

    public static void blit(RenderTarget src, RenderTarget dst) {
        // Bind source framebuffer for reading
        RenderSystem.assertOnRenderThreadOrInit();
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, src.frameBufferId);
        // Bind destination framebuffer for writing
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, dst.frameBufferId);

        GL30.glBlitFramebuffer(
                0, 0, src.width, src.height,     // source rect
                0, 0, dst.width, dst.height,     // destination rect
                // 👇 ✅ THE FIX 👇
                GL30.GL_COLOR_BUFFER_BIT , // what to copy
                GL30.GL_NEAREST                  // copy mode
        );

        // unbind
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }
}
