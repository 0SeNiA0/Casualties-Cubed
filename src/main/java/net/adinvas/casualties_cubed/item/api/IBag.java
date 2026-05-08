package net.adinvas.casualties_cubed.item.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface IBag {

    int size();

    default ItemStack getItem(ItemStack bag, int slot) {
        if (slot >= size() || !bag.hasTag()) return ItemStack.EMPTY;

        CompoundTag tag = bag.getTag();
        assert tag != null;
        if (!tag.contains("StoredItems", Tag.TAG_COMPOUND)) return ItemStack.EMPTY;

        CompoundTag items = tag.getCompound("StoredItems");
        String key = "Slot" + slot;
        if (!items.contains(key, Tag.TAG_COMPOUND)) return ItemStack.EMPTY;

        return ItemStack.of(items.getCompound(key));
    }

    default List<ItemStack> getItems(ItemStack bag) {
        List<ItemStack> items = new ArrayList<>();

        if (!bag.hasTag()) return items;
        CompoundTag rootTag = bag.getTag();
        if (rootTag == null || !rootTag.contains("StoredItems", Tag.TAG_COMPOUND)) return items;

        CompoundTag configTag = rootTag.getCompound("StoredItems");

        // --- Determine how many slots this bag has ---
        int slotCount = size();

        // --- Read in order ---
        for (int i = 0; i < slotCount; i++) {
            String key = "Slot" + i;
            ItemStack stack = ItemStack.EMPTY;
            if (configTag.contains(key, Tag.TAG_COMPOUND)) {
                CompoundTag slotTag = configTag.getCompound(key);
                stack = ItemStack.of(slotTag);
            }
            items.add(stack);
        }

        return items;
    }

    default void setItem(ItemStack bag, int slot, ItemStack item) {
        if (slot >= size()) return;

        CompoundTag tag = bag.getOrCreateTag();
        CompoundTag items = tag.getCompound("StoredItems");
        tag.put("StoredItems", items);//in case a new tag was returned put it in

        if (item.isEmpty()) {
            items.remove("Slot" + slot);
            return;
        }

        CompoundTag itemTag = new CompoundTag();
        item.save(itemTag);
        items.put("Slot" + slot, itemTag);
    }

    default void setItems(ItemStack bag, List<ItemStack> items) {
        CompoundTag tag = bag.getOrCreateTag();
        CompoundTag itemsTag = new CompoundTag();

        int size = Math.min(size(), items.size());
        // --- Always write all slots, even if empty --- //     Nope, don't.
        CompoundTag stackTag;
        for (int i = 0; i < size; i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) continue;

            stackTag = new CompoundTag();
            stack.save(stackTag);
            itemsTag.put("Slot" + i, stackTag);
        }

        tag.put("StoredItems", itemsTag);
        bag.setTag(tag);
    }
}
