package net.zaharenko424.casualties_cubed.mixin.sound;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.zaharenko424.casualties_cubed.blocks.CUBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Shadow
    public abstract Block getBlock();

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SoundType;getVolume()F"), method = "place")
    private float modifyVolume(float original) {
        return getBlock() instanceof CUBlock ? -original : original;
    }
}
