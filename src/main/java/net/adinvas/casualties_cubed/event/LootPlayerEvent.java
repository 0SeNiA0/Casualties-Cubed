package net.adinvas.casualties_cubed.event;

import net.adinvas.casualties_cubed.menu.LootPlayerMenu;
import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.registry.ModGameRules;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

@Mod.EventBusSubscriber
public class LootPlayerEvent {

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event){
        if (!(event.getTarget() instanceof Player target)) return;

        if (!target.level().getGameRules().getBoolean(ModGameRules.INVENTORY_STEAL))return;
        Player actor = event.getEntity();
        if (actor.isShiftKeyDown()) return;

        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
            if (h.getConsciousness() <= 4) {
                if (!actor.level().isClientSide) {
                    NetworkHooks.openScreen(
                            (ServerPlayer) actor,
                            new SimpleMenuProvider(
                                    (id, inv, p) -> new LootPlayerMenu(id, inv, target),
                                    Component.literal("Looting " + target.getName().getString())
                            ),
                            buf -> buf.writeVarInt(target.getId())
                    );
                }
                event.setCanceled(true);
            }
        });
    }
}
