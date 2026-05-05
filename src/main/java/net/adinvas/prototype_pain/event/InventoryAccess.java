package net.adinvas.prototype_pain.event;

import net.adinvas.prototype_pain.registry.ModGameRules;
import net.adinvas.prototype_pain.PlayerHealthProvider;
import net.adinvas.prototype_pain.PrototypePain;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PrototypePain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryAccess {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Player target)) return;

        if (!target.level().getGameRules().getBoolean(ModGameRules.INVENTORY_STEAL))return;
        Player actor = event.getEntity();
        if (!actor.isShiftKeyDown()) return; // only when sneaking

        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            if (h.getContiousness() <= 4) {
                // open target's inventory for the actor
                event.setCanceled(true);
            }
        });
    }
}