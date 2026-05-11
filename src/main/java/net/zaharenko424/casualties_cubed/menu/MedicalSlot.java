package net.zaharenko424.casualties_cubed.menu;

import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MedicalSlot extends Slot {

    public MedicalSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return pStack.getItem() instanceof IAllowInMedicBags;
    }
}
