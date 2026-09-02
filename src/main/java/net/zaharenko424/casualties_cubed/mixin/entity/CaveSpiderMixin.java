package net.zaharenko424.casualties_cubed.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CaveSpider.class)
public abstract class CaveSpiderMixin {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"),
            method = "doHurtTarget")
    private boolean replacePoisonWithVenom(LivingEntity instance, MobEffectInstance pEffectInstance, Entity pEntity, Operation<Boolean> original) {
        if (!(instance instanceof ServerPlayer player)) return original.call(instance, pEffectInstance, pEntity);

        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return true;

        data.addVenomTotal(switch (instance.level().getDifficulty()) {
            case PEACEFUL -> 0;
            case EASY -> 10;
            case NORMAL -> 15;
            case HARD -> 20;
        });
        return true;
    }
}
