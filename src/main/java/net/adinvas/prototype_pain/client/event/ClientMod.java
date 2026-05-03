package net.adinvas.prototype_pain.client.event;

import net.adinvas.prototype_pain.client.moodles.MoodleController;
import net.adinvas.prototype_pain.client.overlays.OverlayController;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientMod {

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("overlay", OverlayController::render);

        event.registerAboveAll("moodle", MoodleController::render);
    }
}
