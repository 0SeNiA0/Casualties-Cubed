package net.zaharenko424.casualties_cubed.item.api;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IMedicalMinigameUsable {

   void openMinigameScreen(Player target, ItemStack stack, @Nullable Limb limb, InteractionHand hand);

   void openMinigameBagScreen(Player target, ItemStack stack,ItemStack bagStack,int slot, @Nullable Limb limb, InteractionHand hand);
}
