package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;

public class AlcoholBottleItem extends BottleItem {

    @Override
    public int getCapacity() {
        return 500;
    }

    @Override
    public ItemStack withDefFluid() {
        return withMedicalFluid(ModMedicalFluids.ALCOHOL);
    }
}
