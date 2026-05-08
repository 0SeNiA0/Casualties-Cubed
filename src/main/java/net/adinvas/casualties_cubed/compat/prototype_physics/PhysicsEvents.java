package net.adinvas.casualties_cubed.compat.prototype_physics;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.adinvas.prototype_physics.RagdollPart;
import net.adinvas.prototype_physics.events.PlayerPartHitEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PhysicsEvents {

    @SubscribeEvent
    public void onRagdollHit(PlayerPartHitEvent event){
        if (!PhysicsUtil.isPhysicsActivated(event.getPlayer())) return;

        RagdollPart part = event.getPartName();
        ServerPlayer player = event.getPlayer();
        float vel = event.getImpactForce();
        if (vel > 2) {
            Limb limb = switch (part) {
                case HEAD -> Limb.HEAD;
                case LEFT_ARM -> Math.random() > 0.5f ? Limb.LEFT_ARM : Limb.LEFT_HAND;
                case LEFT_LEG -> Math.random() > 0.5f ? Limb.LEFT_LEG : Limb.LEFT_FOOT;
                case RIGHT_LEG -> Math.random() > 0.5f ? Limb.RIGHT_FOOT : Limb.RIGHT_LEG;
                case RIGHT_ARM -> Math.random() > 0.5f ? Limb.RIGHT_HAND : Limb.RIGHT_ARM;
                default -> Limb.CHEST;
            };

            float damage = vel * 1.5f;
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h ->
                    h.handleBluntDamage(damage, player, limb));
        }
    }
}
