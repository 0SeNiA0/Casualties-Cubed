package net.zaharenko424.casualties_cubed.compat.prototype_physics;

import net.minecraft.Util;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.hitbox.HitboxEvents;
import net.zaharenko424.casualties_cubed.limbs.Limb;
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
                case LEFT_ARM -> Util.getRandom(Limb.ARM_LIMBS_LEFT, player.getRandom());
                case LEFT_LEG -> Util.getRandom(Limb.LEG_LIMBS_LEFT, player.getRandom());
                case RIGHT_LEG -> Util.getRandom(Limb.LEG_LIMBS_RIGHT, player.getRandom());
                case RIGHT_ARM -> Util.getRandom(Limb.ARM_LIMBS_RIGHT, player.getRandom());
                default -> Limb.THORAX;
            };

            float damage = vel * 1.5f;
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h ->
                    HitboxEvents.handleBluntDamage(h, damage, player, limb));
        }
    }
}
