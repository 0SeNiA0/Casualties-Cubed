package net.zaharenko424.casualties_cubed.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.compat.prototype_physics.PhysicsUtil;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.zaharenko424.casualties_cubed.limbs.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Shadow
    protected boolean wasUnderwater;

    @Inject(method = "updatePlayerPose", at = @At("HEAD"), cancellable = true)
    private void forceLaydownPose(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        self.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (!data.isConscious()) {
                if (!(PhysicsUtil.isPhysicsLoaded() && ServerConfig.PHYS_INTEGRATION.get())) {
                    self.setPose(Pose.SWIMMING);
                    ci.cancel(); // prevent vanilla from picking another pose
                }
            }
            if (data.isAmputated(Limb.UPPER_RIGHT_LEG) && data.isAmputated(Limb.UPPER_LEFT_LEG) && !self.isPassenger()) {
                self.setPose(Pose.SWIMMING);
                ci.cancel();
            }
        });
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "isSleepingLongEnough")
    private boolean modifySleepingLongEnough(boolean original) {
        if (!((Player)(Object)this instanceof ServerPlayer player)) return original;
        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return original;

        return player.isSleeping() && !player.level().isDay() && data.consciousness() <= 10;
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "canEat")
    private boolean alwaysCanEat(boolean original) {
        return true;
    }

    @Inject(at = @At("RETURN"), method = "jumpFromGround")
    private void onJump(CallbackInfo ci) {
        if (!((Player)(Object)this instanceof ServerPlayer player)) return;
        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return;

        data.addStamina(-1f /* (1f + this.overEncumberance)*/ * (wasUnderwater ? 0.35f : 1f));
        data.addTemperature(0.045f);
        data.skills.addExp(player, Stat.RES, 0.1f);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;setFoodLevel(I)V"), method = "aiStep")
    private void redirectRestoreFood(FoodData instance, int pFoodLevel, Operation<Void> original) {
        if (!((Player)(Object)this instanceof ServerPlayer player)) return;
        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data != null) {
            if (data.hunger() < 100) data.addHunger(100 - data.hunger());
            if (data.thirst() < 100) data.drink(100 - data.thirst());
        }
    }
}
