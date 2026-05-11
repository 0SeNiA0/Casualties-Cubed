package net.zaharenko424.casualties_cubed.mixin.client;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@Mixin(SplashManager.class)
public class MixinSplashManager {

    @Final
    @Shadow
    private List<String> splashes;

    /**
     * Inject AFTER vanilla loads splashes into the list.
     */
    @Inject(
            method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("TAIL")
    )
    private void prototype$addCustomSplashes(List<String> vanillaList, ResourceManager rm, ProfilerFiller profiler, CallbackInfo ci) {
        // Add your text file of splashes
        List<String> extraSplashes;

        try {
            ResourceLocation loc = CasualtiesCubed.resourceLoc("texts/splashes.txt");
            BufferedReader br = Minecraft.getInstance().getResourceManager().openAsReader(loc);

            extraSplashes = br.lines()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();

        } catch (IOException e) {
            return;
        }

        if (!extraSplashes.isEmpty()) {
            splashes.addAll(extraSplashes);
        }
    }
}
