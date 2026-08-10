package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.server.level.ServerPlayer;

public interface TimedEffectFunction {

    void update(ServerPlayer player, PlayerHealthData data, TimedEffect effect);
}
