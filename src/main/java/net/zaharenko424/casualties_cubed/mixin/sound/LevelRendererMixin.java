package net.zaharenko424.casualties_cubed.mixin.sound;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.casualties_cubed.blocks.CUBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SoundType;getVolume()F"), method = "levelEvent")
    private float modifyVolume(float original, @Local(argsOnly = true, ordinal = 1) int data) {
        BlockState state = Block.stateById(data);
        return state.getBlock() instanceof CUBlock ? -original : original;
    }
}
