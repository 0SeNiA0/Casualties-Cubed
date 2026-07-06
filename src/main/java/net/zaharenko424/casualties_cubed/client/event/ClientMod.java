package net.zaharenko424.casualties_cubed.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.zaharenko424.casualties_cubed.client.Keybinds;
import net.zaharenko424.casualties_cubed.client.gui.*;
import net.zaharenko424.casualties_cubed.client.moodles.MoodleController;
import net.zaharenko424.casualties_cubed.client.overlays.OverlayController;
import net.zaharenko424.casualties_cubed.item.usable.ThermometerItem;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.registry.ModMenus;
import net.zaharenko424.casualties_cubed.visual.particles.BloodParticle;
import net.zaharenko424.casualties_cubed.visual.particles.ModParticles;

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

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(ModItems.CHEST_DRAIN.get(), (guiGraphics, font, stack, xOffset, yOffset) -> {
            int j = xOffset + 2, k = yOffset + 13;
            float rechargePercentage = ModItems.CHEST_DRAIN.get().rechargePercentage(Minecraft.getInstance().level, stack);
            int color = Mth.hsvToRgb(rechargePercentage / 3.0F, 1.0F, 1.0F);
            guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
            guiGraphics.fill(RenderType.guiOverlay(), j, k, j + Math.round(rechargePercentage * 13), k + 1, color | -16777216);
            return false;
        });
    }
}
