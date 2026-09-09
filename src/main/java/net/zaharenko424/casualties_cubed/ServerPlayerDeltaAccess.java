package net.zaharenko424.casualties_cubed;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public interface ServerPlayerDeltaAccess {

    static Vec3 deltaMovement(ServerPlayer player) {
        return  ((ServerPlayerDeltaAccess) player.connection).ccu$deltaMovement();
    }

    Vec3 ccu$deltaMovement();
}
