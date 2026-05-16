package net.zaharenko424.casualties_cubed.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<CreativeModeTab> YOUR_TAB = CREATIVE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.casualties_cubed_tab")) // lang key
                    .icon(() -> new ItemStack(ModItems.DRESSING.get())) // icon for the tab
                    .displayItems((parameters, output) -> {
                        for (RegistryObject<Item> itemRegistryObject : ModItems.ITEMS.getEntries()){
                            if (itemRegistryObject.get() instanceof MultiTankFluidItem multiTankFluidItem){
                                ItemStack stack = new ItemStack(itemRegistryObject.get());
                                multiTankFluidItem.setupDefault(stack);
                                output.accept(stack);

                                if (itemRegistryObject == ModItems.OPIUM_VIAL) {
                                    output.accept(((MultiTankFluidItem)ModItems.MEDICINE_VIAL.get()).withMedicalFluid(ModMedicalFluids.REACTION_LIQUID, 100));
                                }

                                continue;
                            }

                            output.accept(itemRegistryObject.get());
                        }
                    })
                    .build()
    );
}
