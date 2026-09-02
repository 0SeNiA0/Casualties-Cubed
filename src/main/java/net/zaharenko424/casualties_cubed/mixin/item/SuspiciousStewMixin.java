package net.zaharenko424.casualties_cubed.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SuspiciousStewItem;
import net.zaharenko424.casualties_cubed.util.Util;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Mixin(SuspiciousStewItem.class)
public abstract class SuspiciousStewMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/SuspiciousStewItem;listPotionEffects(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;)V"),
            method = "finishUsingItem", index = 1)
    private Consumer<MobEffectInstance> replacePoisonAndHunger(Consumer<MobEffectInstance> pOutput, @Local(argsOnly = true) LivingEntity target) {
        if (!(target instanceof ServerPlayer player)) return pOutput;
        return effect -> {
            MobEffect eff = effect.getEffect();
            if (eff != MobEffects.POISON && eff != MobEffects.HUNGER) {
                pOutput.accept(effect);
                return;
            }

            PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
            if (data == null) return;

            if (eff == MobEffects.POISON) {
                data.addVenomTotal(Math.max(10, effect.getDuration() * Util.TICK_TO_SEC * (1 + effect.getAmplifier())));
            } else data.addSickness(effect.getDuration() * Util.TICK_TO_SEC * 0.5f * (1 + effect.getAmplifier()));
        };
    }
}
