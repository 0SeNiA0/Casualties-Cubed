package net.zaharenko424.casualties_cubed.mixin.client;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.compat.prototype_physics.PhysicsUtil;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    private void disableMouseTurn(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!(PhysicsUtil.isPhysicsLoaded() && ServerConfig.SPEC.isLoaded() && ServerConfig.PHYS_INTEGRATION.get())) {
            mc.player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                if (h.getConsciousness() <= 10) {
                    ci.cancel();
                }
            });
        }
    }
}
