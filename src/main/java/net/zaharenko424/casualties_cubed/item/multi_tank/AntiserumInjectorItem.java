package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class AntiserumInjectorItem extends AutoInjectorItem {

    @Override
    public int getInjectAmount() {
        return 50;
    }

    @Override
    public ItemStack withDefFluid() {
        return withFluid(ModFluids.ANTISERUM);
    }
}
