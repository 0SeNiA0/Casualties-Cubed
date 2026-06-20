package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class WoundGlueSprayItem extends SprayBottleItem implements IAllowInMedicBags {

    @Override
    public int getOnSkinAmount() {
        return 20;
    }

    @Override
    public int getCapacity() {
        return 80;
    }

    @Override
    public ItemStack withDefFluid() {
        return withFluid(ModFluids.WOUND_GLUE);
    }
}
