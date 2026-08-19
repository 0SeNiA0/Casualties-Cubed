package net.zaharenko424.casualties_cubed.mixin.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.fluid_system.MedicalEffects;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketMixin {

    @Inject(at = @At("HEAD"), method = "finishUsingItem")
    private void onDrink(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, CallbackInfoReturnable<ItemStack> cir) {
        if (!(pEntityLiving instanceof ServerPlayer player)) return;

        PlayerHealthData.of(player).ifPresent(data -> MedicalEffects.MILK.applyIngested(player, 1000));
    }
}
