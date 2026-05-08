package net.adinvas.casualties_cubed.client.event;

import net.adinvas.casualties_cubed.client.Keybinds;
import net.adinvas.casualties_cubed.client.gui.*;
import net.adinvas.casualties_cubed.client.moodles.MoodleController;
import net.adinvas.casualties_cubed.client.overlays.OverlayController;
import net.adinvas.casualties_cubed.item.usable.ThermometerItem;
import net.adinvas.casualties_cubed.registry.ModMenus;
import net.adinvas.casualties_cubed.visual.particles.BloodParticle;
import net.adinvas.casualties_cubed.visual.particles.ModParticles;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientMod {

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("overlay", OverlayController::render);

        event.registerAboveAll("moodle", MoodleController::render);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenus.SMALL_MEDIBAG.get(), SmallMedibagScreen::new);
        MenuScreens.register(ModMenus.MEDIUM_MEDIBAG.get(), MediumMedibagScreen::new);
        MenuScreens.register(ModMenus.LARGE_MEDIBAG.get(), LargeMedibagScreen::new);
        MenuScreens.register(ModMenus.LOOT_PLAYER.get(), LootPlayerScreen::new);
        MenuScreens.register(ModMenus.MEDICAL_MIXER.get(), MedicalMixerScreen::new);
    }

    @SubscribeEvent
    public static void registerKeybindings(RegisterKeyMappingsEvent event){
        Keybinds.register(event);
    }

    @SubscribeEvent
    public static void registerParticleProvider(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(ModParticles.BLOOD_PARTICLE.get(), BloodParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(ThermometerItem.ClientThermoTooltip.class, Function.identity());
    }
}
