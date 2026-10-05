package net.zaharenko424.casualties_cubed.mixin.mod.thirst;

import dev.ghen.thirst.content.thirst.PlayerThirst;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerThirst.class, remap = false)
public abstract class PlayerThirstMixin {

    @Inject(at = @At("HEAD"), method = "drink(Lnet/minecraft/world/entity/player/Player;II)V", cancellable = true)
    private void replaceDrink(Player player, int thirst, int quenched, CallbackInfo ci) {
        PlayerHealthData.of(player).ifPresent(data -> data.drink(thirst + quenched));
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void cancelTick(Player player, CallbackInfo ci) {
        ci.cancel();
    }
}
