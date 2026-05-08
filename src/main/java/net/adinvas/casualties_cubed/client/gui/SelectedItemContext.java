package net.adinvas.casualties_cubed.client.gui;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record SelectedItemContext(ItemStack stack, InteractionHand hand, boolean fromBag, int bagSlot) {
}
