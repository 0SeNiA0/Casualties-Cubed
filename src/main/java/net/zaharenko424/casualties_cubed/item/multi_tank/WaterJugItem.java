package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.zaharenko424.casualties_cubed.item.api.FluidTint;

public class WaterJugItem extends BottleItem implements FluidTint {

    @Override
    public int getCapacity() {
        return 3000;
    }

    @Override
    public int defTint() {
        return 2132880449;
    }
}
