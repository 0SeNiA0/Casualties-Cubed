package net.adinvas.prototype_pain.menu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMedibagMenu extends AbstractContainerMenu {

    protected final ItemStack bagStack;
    protected final Container medibagInventory;

    public AbstractMedibagMenu(MenuType<?> type, int id, Inventory playerInv, ItemStack bagStack, Container medibagInventory){
        super(type, id);
        this.bagStack = bagStack;
        this.medibagInventory = medibagInventory;

        loadConfigFromItem(bagStack);
        createBagSlots();

        createPlayerInventory(playerInv);
        createPlayerHotbar(playerInv);
    }

    protected abstract void createBagSlots();

    protected void createPlayerHotbar(Inventory playerInv) {
        for (int column = 0; column < 9; column++) {
            if(playerInv.getItem(column).equals(bagStack)) {
                addSlot(new Slot(playerInv, column, 8 + (column * 18), 142){
                    @Override
                    public boolean mayPickup(@NotNull Player pPlayer) {
                        return false;
                    }
                });
                continue;
            }
            addSlot(new Slot(playerInv, column, 8 + (column * 18), 142));
        }
    }

    protected void createPlayerInventory(Inventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + column + (row * 9), 8 + (column * 18), 84 + (row * 18)));
            }
        }
    }

    @Override
    public void clicked(int pSlotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if(clickType == ClickType.SWAP) {
            Inventory plInv = player.getInventory();
            if(plInv.getItem(button).equals(bagStack) || plInv.getItem(pSlotId).equals(bagStack)) return;
        }
        super.clicked(pSlotId, button, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            int containerSlots = medibagInventory.getContainerSize(); // number of chest slots
            int totalSlots = this.slots.size();

            if (i < containerSlots) {
                // Moving from chest -> player inventory
                if (!this.moveItemStackTo(stackInSlot, containerSlots, totalSlots, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Moving from player inventory -> chest
                if (!this.moveItemStackTo(stackInSlot, 0, containerSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private void saveConfigToItem() {
        CompoundTag rootTag = bagStack.getOrCreateTag();
        CompoundTag configTag = new CompoundTag();

        for (int i = 0; i < medibagInventory.getContainerSize(); i++) {
            ItemStack stack = medibagInventory.getItem(i);
            if (!stack.isEmpty()) {
                configTag.put("Slot"+i, stack.save(new CompoundTag()));
            }
        }

        rootTag.put("StoredItems", configTag);
    }

    private void loadConfigFromItem(ItemStack droneStack) {
        CompoundTag rootTag = droneStack.getTag();
        if (rootTag == null || !rootTag.contains("StoredItems", Tag.TAG_COMPOUND)) return;

        CompoundTag configTag = rootTag.getCompound("StoredItems");
        for (int i = 0; i < medibagInventory.getContainerSize(); i++) {
            if (configTag.contains("Slot"+i, Tag.TAG_COMPOUND)) {
                ItemStack loaded = ItemStack.of(configTag.getCompound("Slot"+i));
                medibagInventory.setItem(i, loaded);
            }
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        saveConfigToItem();
    }
}
