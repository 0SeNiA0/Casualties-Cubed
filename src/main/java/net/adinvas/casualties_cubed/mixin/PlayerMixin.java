package net.adinvas.casualties_cubed.mixin;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.compat.prototype_physics.PhysicsUtil;
import net.adinvas.casualties_cubed.config.ServerConfig;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "updatePlayerPose",at = @At("HEAD"), cancellable = true)
    private void pp$forceLaydownPose(CallbackInfo ci) {
        Player self = (Player)(Object)this;
        self.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            if (h.getConsciousness() <= 10) {
                if (!(PhysicsUtil.isPhysicsLoaded()&& ServerConfig.PHYS_INTEGRATION.get())) {
                    self.setPose(Pose.SWIMMING);
                    ci.cancel(); // prevent vanilla from picking another pose
                }
            }
            if (h.isAmputated(Limb.RIGHT_LEG)&&h.isAmputated(Limb.LEFT_LEG)&&!self.isPassenger()){
                self.setPose(Pose.SWIMMING);
                ci.cancel();
            }
        });
    }
}
