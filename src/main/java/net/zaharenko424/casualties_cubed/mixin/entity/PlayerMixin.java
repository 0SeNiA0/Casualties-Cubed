package net.zaharenko424.casualties_cubed.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.compat.prototype_physics.PhysicsUtil;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    private void forceLaydownPose(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        self.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            if (h.getConsciousness() <= 10) {
                if (!(PhysicsUtil.isPhysicsLoaded() && ServerConfig.PHYS_INTEGRATION.get())) {
                    self.setPose(Pose.SWIMMING);
                    ci.cancel(); // prevent vanilla from picking another pose
                }
            }
            if (h.isAmputated(Limb.UPPER_RIGHT_LEG) && h.isAmputated(Limb.UPPER_LEFT_LEG) && !self.isPassenger()) {
                self.setPose(Pose.SWIMMING);
                ci.cancel();
            }
        });
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "isSleepingLongEnough")
    private boolean modifySleepingLongEnough(boolean original) {
        Player thisPl = (Player) (Object)this;
        PlayerHealthData data = PlayerHealthData.of(thisPl).orElse(null);
        if (data == null) return original;

        return thisPl.isSleeping() && !thisPl.level().isDay() && data.getConsciousness() <= 10;
    }
}
