package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;

public class BrainGrowPillItem extends PillContainerItem {

    @Override
    public int getCapacity() {
        return 50;
    }

    @Override
    public ItemStack withDefFluid() {
        return withMedicalFluid(ModMedicalFluids.BRAINGROW);
    }
}
