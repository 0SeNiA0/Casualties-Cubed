package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.zaharenko424.casualties_cubed.event.RegisterMedicalEffectsEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExtraMedFluids {

    private static Map<Fluid, Data> EXTRA_MED_FLUIDS = null;
    private static final Data EMPTY = new Data(MedicalEffect.EMPTY, -1, Component.empty());

    static Data getOrDef(Fluid fluid) {
        return EXTRA_MED_FLUIDS == null ? EMPTY : EXTRA_MED_FLUIDS.getOrDefault(fluid, EMPTY);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Map<Fluid, ExtraMedFluids.Data> map = new HashMap<>();
            MinecraftForge.EVENT_BUS.post(new RegisterMedicalEffectsEvent(map));
            EXTRA_MED_FLUIDS = Map.copyOf(map);
        });
    }

    public record Data(MedicalEffect effect, int color, Component description) {}
}
