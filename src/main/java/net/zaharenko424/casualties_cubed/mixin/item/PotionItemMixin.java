package net.zaharenko424.casualties_cubed.mixin.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/ConsumeItemTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;)V"),
            method = "finishUsingItem")
    private void drink(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, CallbackInfoReturnable<ItemStack> cir) {
        if (!(pEntityLiving instanceof ServerPlayer player) || player.getAbilities().invulnerable) return;

        PlayerHealthData.of(player).ifPresent(data -> {
            data.drink(250 * 0.09f * (PotionUtils.getPotion(pStack) == Potions.WATER ? 1 : 0.5f));
            if (!PotionUtils.getMobEffects(pStack).isEmpty()) data.addSickness(10);
        });
    }
}
