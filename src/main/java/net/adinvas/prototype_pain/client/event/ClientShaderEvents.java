package net.adinvas.prototype_pain.client.event;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.adinvas.prototype_pain.PrototypePain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = PrototypePain.MOD_ID, value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientShaderEvents {

    public static ShaderInstance CONSCIOUSNESS_SHADER;
    public static ShaderInstance PAIN_SHADER;
    public static ShaderInstance LEFT_EYE_BLIND;
    public static ShaderInstance RIGHT_EYE_BLIND;
    public static ShaderInstance FULL_BLIND;
    public static ShaderInstance WARP_CHROMAABB;
    public static ShaderInstance SICKNESS;
    public static ShaderInstance HEAT;
    public static ShaderInstance COLD;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("consciousness"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> CONSCIOUSNESS_SHADER = shader
        );
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("pain_vignette"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> PAIN_SHADER = shader
        );
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("leftblind"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> LEFT_EYE_BLIND = shader
        );event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("rightblind"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> RIGHT_EYE_BLIND = shader
        );
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("fullblind"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> FULL_BLIND = shader
        );
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("warp_chromaabb"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> WARP_CHROMAABB = shader
        );
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("sickness"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> SICKNESS = shader
        );
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("heat"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> HEAT = shader
        );
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        PrototypePain.resourceLoc("cold"),
                        DefaultVertexFormat.POSITION_TEX),
                shader -> COLD = shader
        );
    }
}
