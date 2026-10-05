package net.zaharenko424.casualties_cubed.mixin.mod.thirst;

import dev.ghen.thirst.foundation.gui.ThirstBarRenderer;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ThirstBarRenderer.class, remap = false)
public abstract class ThirstBarRendererMixin {
    
    @Inject(at = @At("HEAD"), method = "registerThirstOverlay", cancellable = true)
    private static void dontRegisterThirstBar(RegisterGuiOverlaysEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
