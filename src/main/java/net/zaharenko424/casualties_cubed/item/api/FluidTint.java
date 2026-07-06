package net.zaharenko424.casualties_cubed.item.api;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.Util;
import net.zaharenko424.casualties_cubed.fluid_system.MultiTankHelper;
import net.zaharenko424.casualties_cubed.item.multi_tank.MultiTankFluidItem;

public interface FluidTint {

    default int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 1 || !(stack.getItem() instanceof MultiTankFluidItem)) return -1;

        if (MultiTankHelper.getFilledTotal(stack) <= 0) {
            return defTint();
        }

        return Util.mixColors(MultiTankHelper.getColorRatios(stack)) | 0xFF000000;//TODO add transparency to fluids & mix it
    }

    default int defTint() {
        return -1;
    }
}
