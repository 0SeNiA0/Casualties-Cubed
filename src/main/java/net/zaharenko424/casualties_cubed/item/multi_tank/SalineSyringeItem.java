package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class SalineSyringeItem extends SyringeItem {

    @Override
    public int getCapacity() {
        return 750;
    }

    @Override
    public ItemStack withDefFluid() {
        return withFluid(ModFluids.SALINE);
    }
}
