package net.adinvas.casualties_cubed.menu;

import net.adinvas.casualties_cubed.registry.ModMenus;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class LargeMedibagMenu extends AbstractMedibagMenu {

    public LargeMedibagMenu(int id, Inventory playerInv, ItemStack bagStack){
        super(ModMenus.LARGE_MEDIBAG.get(), id, playerInv, bagStack, new SimpleContainer(12));
    }

    @Override
    protected void createBagSlots() {
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                this.addSlot(new MedicalSlot(medibagInventory, col + row * 6, 36 + col * 18, 24 + row * 18));
            }
        }
    }
}
