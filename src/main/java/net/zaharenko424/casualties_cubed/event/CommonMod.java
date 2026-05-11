package net.zaharenko424.casualties_cubed.event;

import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.registry.ModGameRules;
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
