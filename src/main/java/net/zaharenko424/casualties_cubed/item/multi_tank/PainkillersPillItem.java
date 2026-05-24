package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class PainkillersPillItem extends PillContainerItem {

    @Override
    public ItemStack withDefFluid() {
        return withFluid(ModFluids.PAINKILLERS);
    }
}
