package net.zaharenko424.casualties_cubed.fluid_system;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.zaharenko424.casualties_cubed.event.RegisterMedicalEffectsEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExtraMedFluids {

    private static Map<FluidType, Data> EXTRA_MED_FLUIDS = null;
    private static final Data EMPTY = new Data(MedicalEffect.EMPTY, -1, Component.empty());

    static Data getOrDef(Fluid fluid) {
        return EXTRA_MED_FLUIDS == null ? EMPTY : EXTRA_MED_FLUIDS.getOrDefault(fluid.getFluidType(), EMPTY);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Map<FluidType, ExtraMedFluids.Data> map = new HashMap<>();
            map.put(ForgeMod.WATER_TYPE.get(), new Data(MedicalEffects.GROUNDWATER, 0x5276d1, Component.translatable("medical_fluid.casualties_cubed.groundwater.description")));
            if (ForgeMod.MILK_TYPE.isPresent()) map.put(ForgeMod.MILK_TYPE.get(), new Data(MedicalEffects.MILK, -1, Component.translatable("medical_fluid.casualties_cubed.milk.description")));
            map.put(ForgeMod.LAVA_TYPE.get(), new Data(MedicalEffects.LAVA, 0xff6600, Component.empty()));
            MinecraftForge.EVENT_BUS.post(new RegisterMedicalEffectsEvent(map));
            EXTRA_MED_FLUIDS = Map.copyOf(map);
        });
    }

    public record Data(MedicalEffect effect, int color, Component description) {}
}
