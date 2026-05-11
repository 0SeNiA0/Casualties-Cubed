package net.zaharenko424.casualties_cubed.menu;

import net.zaharenko424.casualties_cubed.registry.ModMenus;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class MediumMedibagMenu extends AbstractMedibagMenu {

    public MediumMedibagMenu(int id, Inventory playerInv, ItemStack bagStack){
        super(ModMenus.MEDIUM_MEDIBAG.get(), id, playerInv, bagStack, new SimpleContainer(8));
    }

    @Override
    protected void createBagSlots() {
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                this.addSlot(new MedicalSlot(medibagInventory, col + row * 4, 54 + col * 18, 24 + row * 18));
            }
        }
    }
}
