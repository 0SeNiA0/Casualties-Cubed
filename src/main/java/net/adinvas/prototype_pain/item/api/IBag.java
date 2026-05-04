package net.adinvas.prototype_pain.item.api;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IBag {

    List<ItemStack> getItems(ItemStack bagstack);

    void setItems(ItemStack bagstack,List<ItemStack> itemStacks);
}
