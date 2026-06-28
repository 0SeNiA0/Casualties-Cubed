package net.zaharenko424.casualties_cubed.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.zaharenko424.casualties_cubed.Util;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Arrow.class)
public abstract class ArrowMixin {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"),
            method = "doPostHurtEffects")
    private boolean replacePoison(LivingEntity instance, MobEffectInstance pEffectInstance, Entity pEntity, Operation<Boolean> original) {
        if (pEffectInstance.getEffect() != MobEffects.POISON || !(instance instanceof ServerPlayer player)) return original.call(instance, pEffectInstance, pEntity);

        PlayerHealthData.of(player).ifPresent(data -> data.addVenom(pEffectInstance.getDuration() * Util.TICK_TO_SEC * Math.max(1 ,pEffectInstance.getAmplifier() * 10)));
        return true;
    }
}
