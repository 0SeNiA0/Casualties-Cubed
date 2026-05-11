package net.zaharenko424.casualties_cubed.item.multi_tank;

import net.zaharenko424.casualties_cubed.client.MinigameOpener;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.IMedicalMinigameUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

public class SyringeItem extends MultiTankFluidItem implements IMedicalMinigameUsable, IAllowInMedicBags {

    @Override
    public int getCapacity() {
        return 100;
    }

    @Override
    public void openMinigameScreen(Player target, ItemStack stack, @Nullable Limb limb, InteractionHand hand) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinigameOpener.OpenSyringeMinigame(target, stack, limb, hand);
        });
    }

    @Override
    public void openMinigameBagScreen(Player target, ItemStack stack, ItemStack bagStack, int slot, @Nullable Limb limb, InteractionHand hand) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinigameOpener.OpenSyringeMinigame(target, stack, bagStack, slot, limb, hand);
        });
    }
}
