package net.zaharenko424.casualties_cubed.item.reusable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.client.MinigameOpener;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;
import net.zaharenko424.casualties_cubed.item.api.IAllowInMedicBags;
import net.zaharenko424.casualties_cubed.item.api.IMedicalMinigameUsable;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import org.jetbrains.annotations.Nullable;

public class TweezersItem extends ItemWithDescription implements IMedicalMinigameUsable, IAllowInMedicBags {

    public TweezersItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void openMinigameScreen(Player target, ItemStack stack, @Nullable Limb limb, InteractionHand hand) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.getLimb(limb).shrapnel() == 0) return;

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                MinigameOpener.OpenShrapnelMinigame(target, limb);
            });
        });
    }

    @Override
    public void openMinigameBagScreen(Player target, ItemStack stack, ItemStack bagStack, int lost, @Nullable Limb limb, InteractionHand hand) {
        this.openMinigameScreen(target, stack, limb, hand);
    }
}
