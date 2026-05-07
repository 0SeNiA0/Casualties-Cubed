package net.adinvas.prototype_pain.menu;

import net.adinvas.prototype_pain.registry.ModMenus;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class SmallMedibagMenu extends AbstractMedibagMenu {

    public SmallMedibagMenu(int id, Inventory playerInv,ItemStack bagStack){
        super(ModMenus.SMALL_MEDIBAG.get(), id, playerInv, bagStack, new SimpleContainer(4));
    }

    @Override
    protected void createBagSlots() {
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                this.addSlot(new MedicalSlot(medibagInventory, col + row * 2, 72 + col * 18, 24 + row * 18));
            }
        }
    }
}
