package net.zaharenko424.casualties_cubed.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRules.class)
public abstract class GameRulesMixin {

    //Should only impact vanilla player healing which is ignored by healthData
    @ModifyReturnValue(at = @At("RETURN"), method = "getBoolean")
    private boolean ignoreNaturalRegenRule(boolean original, @Local(argsOnly = true) GameRules.Key<GameRules.BooleanValue> key) {
        return key != GameRules.RULE_NATURAL_REGENERATION && original;
    }
}
