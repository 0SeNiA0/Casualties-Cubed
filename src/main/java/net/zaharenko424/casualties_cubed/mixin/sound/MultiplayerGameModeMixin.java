package net.zaharenko424.casualties_cubed.mixin.sound;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.casualties_cubed.blocks.CUBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiPlayerGameMode.class)
public class MultiplayerGameModeMixin {

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SoundType;getVolume()F"), method = "continueDestroyBlock")
    private float modifyVolume(float original, @Local BlockState state) {
        return state.getBlock() instanceof CUBlock ? -original : original;
    }
}
