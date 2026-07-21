package net.zaharenko424.casualties_cubed.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zaharenko424.casualties_cubed.util.Util;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"),
            method = "addEatEffect")
    private boolean replacePoisonAndHunger(LivingEntity instance, MobEffectInstance pEffectInstance, Operation<Boolean> original, ItemStack food) {
        if (!(instance instanceof ServerPlayer player)) return original.call(instance, pEffectInstance);

        MobEffect effect = pEffectInstance.getEffect();
        if (effect != MobEffects.POISON && effect != MobEffects.HUNGER) return original.call(instance, pEffectInstance);

        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return true;

        if (effect == MobEffects.POISON && !food.is(Items.POISONOUS_POTATO)) {
            data.addVenom((food.is(Items.SPIDER_EYE) ? 6 : 2) * pEffectInstance.getDuration() * Util.TICK_TO_SEC * (1 + pEffectInstance.getAmplifier()));
        } else data.addSickness((food.is(Items.POISONOUS_POTATO) ? 1200 : pEffectInstance.getDuration()) * Util.TICK_TO_SEC * 0.5f * (1 + pEffectInstance.getAmplifier()));
        return true;
    }
}
