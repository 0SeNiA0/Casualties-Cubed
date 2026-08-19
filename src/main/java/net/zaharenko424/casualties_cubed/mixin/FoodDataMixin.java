package net.zaharenko424.casualties_cubed.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @Inject(at = @At("HEAD"), method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", cancellable = true, remap = false)
    private void onEat(Item item, ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        if (!item.isEdible()) return;
        if (!(entity instanceof ServerPlayer player)) return;

        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return;

        FoodProperties food = item.getFoodProperties(stack, entity);
        if (food == null) return;

        ci.cancel();
        data.eat(player, food.getNutrition() * 2.25f, food.getSaturationModifier());
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"),
            method = "tick")
    private boolean cancelNaturalRegen(boolean original) {
        return false;
    }
}
