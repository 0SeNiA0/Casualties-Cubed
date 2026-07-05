package net.zaharenko424.casualties_cubed.client.event;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.Util;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.api.FluidTint;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CasualtiesCubed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorsEvent {

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> {

                    if (tintIndex == 1) { // only tint the syringe liquid part
                        if (stack.getItem() instanceof MultiTankFluidItem) {
                            if (MultiTankHelper.getFilledTotal(stack) <= 0) {
                                return 0x00FFFFFF;
                            }
                            return Util.mixColors(MultiTankHelper.getColorRatios(stack)); // return full ARGB or RGB color
                        }
                    }
                    return 0xFFFFFFFF; // white = no tint
                },
                ModItems.MEDICINE_VIAL.get(),
                ModItems.WATER_BOTTLE.get(),
                ModItems.AUTO_INJECTOR.get(),
                ModItems.PROCOAGULANT_INJECTOR.get(),
                ModItems.STREPTOKINASE_INJECTOR.get(),
                ModItems.CEFTRIAXONE_VIAL.get(),
                ModItems.FENTANYL_VIAL.get(),
                ModItems.MORPHINE_VIAL.get(),
                ModItems.NALOXONE_VIAL.get(),
                ModItems.OPIUM_VIAL.get(),
                ModItems.ANTISERUM_INJECTOR.get(),
                ModItems.PILL_BOTTLE.get()
        );

        event.register(FluidTint.COLOR, ModItems.WATER_JUG.get(), ModItems.BLOOD_BAG.get());
    }
}
