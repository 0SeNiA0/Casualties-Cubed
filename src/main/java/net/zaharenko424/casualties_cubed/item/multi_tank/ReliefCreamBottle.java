package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModMedicalFluids;

public class ReliefCreamBottle extends BottleItem {

    @Override
    public int getCapacity() {
        return 200;
    }

    @Override
    public int getOnSkinAmount() {
        return 10;
    }

    @Override
    public int getDrinkingAmount() {
        return 50;
    }

    @Override
    public ItemStack withDefFluid() {
        return withMedicalFluid(ModMedicalFluids.RELIEF_CREAM);
    }
}
