package net.adinvas.prototype_pain.event;

import net.adinvas.prototype_pain.network.ModNetwork;
import net.adinvas.prototype_pain.registry.ModGameRules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonMod {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        ModNetwork.registerPackets();
        ModGameRules.registerGamerules(event);
    }
}
