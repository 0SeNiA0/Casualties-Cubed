package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface TimedEffectFunction {

    void update(ServerPlayer player, PlayerHealthData data, float ml, @Nullable Limb limb, float duration);
}
