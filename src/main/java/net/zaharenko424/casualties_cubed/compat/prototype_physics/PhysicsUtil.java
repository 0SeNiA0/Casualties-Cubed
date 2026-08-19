package net.zaharenko424.casualties_cubed.compat.prototype_physics;

import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.adinvas.prototype_physics.JbulletWorld;
import net.adinvas.prototype_physics.PlayerPhysics;
import net.adinvas.prototype_physics.RagdollPart;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

public class PhysicsUtil {

    public static boolean isPhysicsLoaded() {
        return ModList.get().isLoaded("prototype_physics");
    }

    public static boolean isPhysicsActivated(Player player) {
        if (player != null) {
            return ServerConfig.PHYS_INTEGRATION.get();
        }

        return false;
    }

    public static void setPhysics(boolean state, ServerPlayer player, float randomRot, float randomVel) {
        if (!isPhysicsLoaded() || !isPhysicsActivated(player)) return;

        JbulletWorld world = JbulletWorld.get(player.serverLevel());
        PlayerPhysics physics = world.getPlayerPhys(player);
        PlayerPhysics.Mode current = physics.getMode();
        PlayerPhysics.Mode newmode = state ? PlayerPhysics.Mode.PRECISE : PlayerPhysics.Mode.SILENT;
        if (current != newmode) {
            physics.setMode(newmode);
            physics.applyRandomTorque(randomRot, randomRot, randomRot);
            physics.applyRandomVelocity(randomVel, randomVel, randomVel);
        }
    }

    public static Vec3 getVel(ServerPlayer player) {
        if (!isPhysicsLoaded() || !isPhysicsActivated(player)) return Vec3.ZERO;

        JbulletWorld world = JbulletWorld.get(player.serverLevel());
        PlayerPhysics physics = world.getPlayerPhys(player);
        if (physics.getMode() == PlayerPhysics.Mode.PRECISE) {
            Vec3 vel = physics.getVelocity(RagdollPart.TORSO);
            return vel == null ? Vec3.ZERO : vel;
        }
        return Vec3.ZERO;
    }

    public static Vec3 getVel(RagdollPart part, ServerPlayer player) {
        JbulletWorld world = JbulletWorld.get(player.serverLevel());
        PlayerPhysics physics = world.getPlayerPhys(player);
        if (physics.getMode() == PlayerPhysics.Mode.PRECISE) {
            Vec3 vel = physics.getVelocity(part);
            if (vel == null) {
                vel = new Vec3(0, 0, 0);
            }
            return vel;
        }
        return new Vec3(0, 0, 0);
    }

    public static void applyRandomRot(ServerPlayer player) {
        JbulletWorld world = JbulletWorld.get(player.serverLevel());
        PlayerPhysics physics = world.getPlayerPhys(player);
        if (physics.getMode() == PlayerPhysics.Mode.PRECISE) {
            physics.applyRandomTorque(50, 50, 50);
        }
    }
}
