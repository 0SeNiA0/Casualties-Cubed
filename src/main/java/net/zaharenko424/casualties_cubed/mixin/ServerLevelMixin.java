package net.zaharenko424.casualties_cubed.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;wakeUpAllPlayers()V"), method = "tick")
    private boolean cancelWakeUp(ServerLevel instance) {
        return false;
    }
}
