package net.zaharenko424.casualties_cubed.mixin.mod.thirst;

import dev.ghen.thirst.content.purity.WaterPurity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WaterPurity.class, remap = false)
public abstract class WaterPurityMixin {

    @Inject(at = @At("HEAD"), method = "givePurityEffects(Lnet/minecraft/world/entity/player/Player;I)Z", cancellable = true)
    private static void replacePurityEffects(Player player, int purity, CallbackInfoReturnable<Boolean> cir) {
        PlayerHealthData.of(player).ifPresent(data -> {
            RandomSource random = player.getRandom();
            switch (purity) {
                case 0 -> {
                    if (random.nextFloat() > 0.5f) {
                        data.addSickness((player.getRandom().nextFloat() * 0.02f + 0.06f) * 100);//no context for how much fluid is consumed so use 100ml
                        data.addHappiness(-1f);
                    }
                }
                case 1, 2 -> {
                    if (random.nextFloat() > 0.5f) {
                        data.addSickness(player.getRandom().nextFloat() * 8 + 7);
                        data.addHappiness(-0.5f);
                    }
                }
            }
        });

        cir.setReturnValue(true);
    }
}
