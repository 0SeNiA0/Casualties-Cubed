package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.registry.ModFluids;

public class CombatPenItem extends AutoInjectorItem {

    @Override
    public int getInjectAmount() {
        return 100;
    }

    @Override
    public ItemStack withDefFluid() {
        ItemStack stack = withFluid(ModFluids.HIGH_GRADE_STIMULANT, 60);
        addFluid(stack, ModFluids.EPINEPHRINE, 15);
        addFluid(stack, ModFluids.OXYLINE, 25);
        return stack;
    }
}
