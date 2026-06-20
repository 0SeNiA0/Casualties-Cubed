package net.zaharenko424.casualties_cubed.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.item.api.FluidTint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;FFFFIIZ)V"),
            method = "renderQuadList", index = 5)
    private float withAlphaTint(float original, @Local(argsOnly = true) ItemStack stack, @Local(name = "i") int tint) {
        return stack.getItem() instanceof FluidTint ? FastColor.ARGB32.alpha(tint) / 255f : original;
    }
}
